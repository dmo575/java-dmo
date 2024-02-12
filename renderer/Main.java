package renderer;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Shape;

import renderer.Camera.AspectRatio;
import renderer.Camera.CameraType;

public class Main {

    public static class Vec implements Printable {
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

        Main main = new Main();

        // frame set up
        Frame frame = new Frame();
        frame.setSize(800, 800);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setLocationRelativeTo(null);

        CustomCanvas canvas = new CustomCanvas(frame, 600, 400, true);
        Camera camera = new Camera(canvas, CameraType.Isometric, AspectRatio.Adaptive);
        Mesh mesh = main.GetUnixCube();

        canvas.SetShape(mesh);
        canvas.SetCamera(camera);

        camera.position.z = -5.0f;
        camera.far_plane = 5.0f;


        frame.add(canvas);
    }

    static public void WorldToCanvas(Vec p, int w, int h) {
        // w and h are the width and height of the canvas
        p.x = (w/2) + p.x;
        p.y = (h/2) + p.y;
    }
}

class CustomCanvas extends Canvas {

    int width, height = 0;
    private Camera camera = null;
    private Main.Mesh mesh = null; // this will be replaced with a scene in the future

    CustomCanvas(Frame frame, int width, int height, boolean center) {
        setSize(width, height);
        this.width = width;
        this.height = height;
        setBackground(Color.BLACK);

        if(!center) return;

        int pos_x = frame.getSize().width / 2 - (getSize().width / 2);
        int pos_y = frame.getSize().height / 2 - (getSize().height / 2);
        setLocation(pos_x, pos_y);
    }

    @Override
    public void paint(Graphics g) {

        if(camera == null || mesh == null) return;

        // based on the position of the camera, we offset

        //g.setColor(Color.BLUE);
        //g.drawRect(10, 10, 10, 10);
    }

    public void SetCamera(Camera c) {
        camera = c;
    }

    public void SetShape(Main.Mesh s) {
        mesh = s;
    }
}

interface Printable {
    public void Print();
}

class Camera_isometric extends CustomCanvas {

    // skip this
    Camera_isometric(Frame frame, int width, int height, boolean center) {
        super(frame, width, height, center);
    }


    @Override
    public void paint(Graphics g) {
        // we init two points, with same X and Y but different Z values
        Main.Vec[] points = new Main.Vec[2];
        points[0] = new Main.Vec(-100, 100, 3);
        points[1] = new Main.Vec(100, -100, 20);

        // we offset the values of those points so that we can give the illusion that the canvas is aiming at the origin of the world.
        for (Main.Vec v : points) {
            Main.WorldToCanvas(v, width, height);
        }

        // we draw both points. You can only see the red one because its drawn on top of the blue one.
        //g.setColor(Color.BLUE);
        g.setColor(Color.RED);
        g.fillOval((int)points[0].x - 5, (int)points[0].y - 5, 10, 10);
        g.fillOval((int)points[1].x - 5, (int)points[1].y - 5, 10, 10);
    }

    public void paint_old(Graphics g) {
        // we init two points, with same X and Y but different Z values
        Main.Vec[] points = new Main.Vec[2];
        points[0] = new Main.Vec(-100, 100, 3);
        points[1] = new Main.Vec(100, -100, 20);

        // we offset the values of those points so that we can give the illusion that the canvas is aiming at the origin of the world.
        for (Main.Vec v : points) {
            Main.WorldToCanvas(v, width, height);
        }

        // we draw both points. You can only see the red one because its drawn on top of the blue one.
        //g.setColor(Color.BLUE);
        g.setColor(Color.RED);
        g.fillOval((int)points[0].x - 5, (int)points[0].y - 5, 10, 10);
        g.fillOval((int)points[1].x - 5, (int)points[1].y - 5, 10, 10);
    }
}

class Camera {
    public enum CameraType { Isometric, Perspective };
    public enum AspectRatio { Adaptive, R2_6, R6_4 };

    public CameraType camera_type;
    public AspectRatio aspect_ratio;
    public Canvas canvas;
    public Main.Vec position; // position in the scene, world units
    public Main.Vec scale; // position in the scene, world units
    public float far_plane = 0; // we take the near plane as the position.z coord

    Camera(Canvas c, CameraType t, AspectRatio r) {
        canvas = c;
        camera_type = t;
        aspect_ratio = r;
        scale = new Main.Vec(1.0f, 1.0f, 0.0f);
        position = new Main.Vec(0.0f, 0.0f, 0.0f);
    }
}