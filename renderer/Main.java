package renderer;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;

public class Main {


    class Vec implements Printable {
        float x, y, z;
        Vec(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void Print() {
            System.out.print("( "+ x + ", " + y + ", " + z + " )");
        }
    }

    class Tri  implements Printable {
        Vec[] vecs;
        Tri(Vec a, Vec b, Vec c) {
            vecs = new Vec[] { a, b, c };
        }

        public void Print() {
            System.out.print("t[ ");
            for(Vec t : vecs) {
                t.Print();
                System.out.print(", ");
            }
            System.out.print("]");
        }
    }

    class Mesh implements Printable {
        String name;
        Tri[] tris;
        Mesh(String name, Tri[] tris) {
            this.name = name;
            this.tris = tris;
        }

        public void Print() {
            System.out.println( name + "{ ");
            for(Tri t : tris) {
                t.Print();
                System.out.println("");
            }
            System.out.print("}");
        }
    }

    private Mesh GetUnixCube() {

        // SOUTH
        Tri south_1 = new Tri(new Vec (0,0,0), new Vec (0,1,0), new Vec (1,1,0));
        Tri south_2 = new Tri(new Vec (0,1,0), new Vec (1,1,0), new Vec (1,0,0));

        // NORTH
        Tri north_1 = new Tri(new Vec (0,0,1), new Vec (1,0,1), new Vec (0,1,1));
        Tri north_2 = new Tri(new Vec (0,1,1), new Vec (1,0,1), new Vec (1,1,1));

        // WEST
        Tri west_1 = new Tri(new Vec (0,0,0), new Vec (0,0,1), new Vec (0,1,0));
        Tri west_2 = new Tri(new Vec (0,0,1), new Vec (0,1,1), new Vec (0,1,0));

        // EAST
        Tri east_1 = new Tri(new Vec (1,0,0), new Vec (1,1,0), new Vec (1,0,1));
        Tri east_2 = new Tri(new Vec (1,1,0), new Vec (1,0,1), new Vec (1,0,0));

        // TOP
        Tri top_1 = new Tri(new Vec (0,1,0), new Vec (0,1,1), new Vec (1,1,0));
        Tri top_2 = new Tri(new Vec (0,1,1), new Vec (1,1,1), new Vec (1,1,0));

        // BOTTOM
        Tri bottom_1 = new Tri(new Vec (0,0,0), new Vec (1,0,0), new Vec (0,0,1));
        Tri bottom_2 = new Tri(new Vec (0,0,1), new Vec (1,0,0), new Vec (1,0,1));

        return new Mesh("Unix cube", new Tri[]{south_1, south_2, north_1, north_2, west_1, west_2, east_1, east_2, top_1, top_2, bottom_1, bottom_2});
    }

    public static void main(String[] args) {

        // frame set up
        Frame frame = new Frame();
        frame.setSize(800, 800);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setBackground(Color.LIGHT_GRAY);

        // load canvas
        Canvas canvas = new UnixCanvas(frame, 400);

        // add components to frame
        frame.add(canvas);

        Main main = new Main();
        Mesh mesh = main.GetUnixCube();
        mesh.Print();

    }
}

class UnixCanvas extends Canvas {

    UnixCanvas(Frame frame, int size) {
        setSize(size, size);
        int pos_x = frame.getSize().width - getSize().width - (getSize().width / 2);
        int pos_y = frame.getSize().height - getSize().height - (getSize().height / 2);

        setLocation(pos_x, pos_y);
        setBackground(Color.BLACK);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawRect(10, 10, 10, 10);
    }
}

interface Printable {
    public void Print();
}