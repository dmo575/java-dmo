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
        Color color;
        Tri(Vec a, Vec b, Vec c, Color color) {
            vecs = new Vec[] { a, b, c };
            this.color = color;
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
        Tri south_1 = new Tri(new Vec (0,0,0), new Vec (0,1,0), new Vec (1,1,0), Color.BLUE);
        Tri south_2 = new Tri(new Vec (0,1,0), new Vec (1,1,0), new Vec (1,0,0), Color.BLUE);

        // NORTH
        Tri north_1 = new Tri(new Vec (0,0,1), new Vec (1,0,1), new Vec (0,1,1), Color.RED);
        Tri north_2 = new Tri(new Vec (0,1,1), new Vec (1,0,1), new Vec (1,1,1), Color.RED);

        // WEST
        Tri west_1 = new Tri(new Vec (0,0,0), new Vec (0,0,1), new Vec (0,1,0), Color.YELLOW);
        Tri west_2 = new Tri(new Vec (0,0,1), new Vec (0,1,1), new Vec (0,1,0), Color.YELLOW);

        // EAST
        Tri east_1 = new Tri(new Vec (1,0,0), new Vec (1,1,0), new Vec (1,0,1), Color.GREEN);
        Tri east_2 = new Tri(new Vec (1,1,0), new Vec (1,0,1), new Vec (1,0,0), Color.GREEN);

        // TOP
        Tri top_1 = new Tri(new Vec (0,1,0), new Vec (0,1,1), new Vec (1,1,0), Color.ORANGE);
        Tri top_2 = new Tri(new Vec (0,1,1), new Vec (1,1,1), new Vec (1,1,0), Color.ORANGE);

        // BOTTOM
        Tri bottom_1 = new Tri(new Vec (0,0,0), new Vec (1,0,0), new Vec (0,0,1), Color.MAGENTA);
        Tri bottom_2 = new Tri(new Vec (0,0,1), new Vec (1,0,0), new Vec (1,0,1), Color.MAGENTA);

        Tri extra_1 = new Tri(new Vec (50,-50,1), new Vec (100,-50,0), new Vec (50,-100,1), Color.WHITE);


        return new Mesh("Unix cube", new Tri[]{south_1, south_2, north_1, north_2, west_1, west_2, east_1, east_2, top_1, top_2, bottom_1, bottom_2, extra_1});
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

        CustomCanvas canvas = new CustomCanvas(frame, 300, 200, true);
        Camera camera = new Camera(canvas, CameraType.Isometric, AspectRatio.Adaptive);
        Mesh mesh = main.GetUnixCube();

        canvas.SetShape(mesh);
        canvas.SetCamera(camera);
        
        camera.far_plane = 10;
        camera.AddPosition(new Vec(150, -100, -5));
        camera.Print();


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
        g.setColor(Color.BLUE);


        // we first check if the point is within the range (mind you for this we need the point's FINAL position after rotation and scale modifications)
        // and in reality we would include the whole triangle if a single point were to enter the range

        Main.Vec cam_center_world = camera.GetPositionWorld();
        Main.Vec cam_TL_world = camera.GetTLWorld();

        // stores the point position RELATIVE to TL (World space)
        Main.Vec point_pos_TL = new Main.Vec(0,0,0);

        // we need to make the world point coord relative to the TL world point first, then we print it with Y inverted


        for(Main.Tri t : mesh.tris) {

            for(Main.Vec v : t.vecs) {

                // check if one of the points of the triangle exists within the camera's FOV
                if(v.x >= cam_TL_world.x && v.x <= cam_TL_world.x + width &&
                v.y <= cam_TL_world.y && v.y >= cam_TL_world.y - height &&
                v.z >= camera.position.z && v.z <= camera.far_plane) {

                    g.setColor(t.color);

                    g.drawLine((int)(t.vecs[0].x - cam_TL_world.x), -(int)(t.vecs[0].y - (cam_TL_world.y * Float.compare(cam_TL_world.y, 0))),
                    (int)(t.vecs[1].x - cam_TL_world.x), -(int)(t.vecs[1].y - (cam_TL_world.y * Float.compare(cam_TL_world.y, 0))));
                    g.drawLine((int)(t.vecs[1].x - cam_TL_world.x), -(int)(t.vecs[1].y - (cam_TL_world.y * Float.compare(cam_TL_world.y, 0))),
                    (int)(t.vecs[2].x - cam_TL_world.x), -(int)(t.vecs[2].y - (cam_TL_world.y * Float.compare(cam_TL_world.y, 0))));
                    g.drawLine((int)(t.vecs[2].x - cam_TL_world.x), -(int)(t.vecs[2].y - (cam_TL_world.y * Float.compare(cam_TL_world.y, 0))),
                    (int)(t.vecs[0].x - cam_TL_world.x), -(int)(t.vecs[0].y - (cam_TL_world.y * Float.compare(cam_TL_world.y, 0))));


                    // project all points of the triangle into the canvas
                    for(int i = 0; i < 3; i++) {

                        // get position of point with TL as origin, in world space
                        point_pos_TL.x = t.vecs[i].x - cam_TL_world.x;
                        point_pos_TL.y = t.vecs[i].y - (cam_TL_world.y * Float.compare(cam_TL_world.y, 0));

                        // draw point in canvas
                        g.drawRect((int)point_pos_TL.x, -(int)point_pos_TL.y, 10, 10);

                    }

                    break;
                }
            }
        }
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

class Camera implements Printable {
    public enum CameraType { Isometric, Perspective };
    public enum AspectRatio { Adaptive, R2_6, R6_4 };

    public CameraType camera_type;
    public AspectRatio aspect_ratio;
    public Canvas canvas;
    public Main.Vec position;
    public Main.Vec scale; // position in the scene, world units
    public float far_plane = 0; // we take the near plane as the position.z coord

    Camera(Canvas c, CameraType t, AspectRatio r) {
        canvas = c;
        camera_type = t;
        aspect_ratio = r;
        scale = new Main.Vec(1.0f, 1.0f, 0.0f);
        position = new Main.Vec(0.0f, 0.0f, 0.0f);
        SetPosition(new Main.Vec(0, 0, 0));
    }

    public void SetPosition(Main.Vec v) {
        position.x = v.x;
        position.y = v.y;
        position.z = v.z;
    }

    public Main.Vec GetPositionWorld() {
        // we return a new object to make sure users cannot edit it unless they use the SetPosition()
        return new Main.Vec(position.x, position.y, position.z);
    }

    public Main.Vec GetTLWorld() {
        // we return a new object to make sure users cannot edit it unless they use the SetPosition()
        return new Main.Vec(position.x - (canvas.getWidth() / 2), position.y + (canvas.getHeight() / 2), position.z);
    }

    public void AddPosition(Main.Vec v) {
        position.x += v.x;
        position.y += v.y;
        position.z += v.z;
    }

    @Override
    public void Print() {
        System.out.println("Cam TL (World): (" + (position.x - (canvas.getWidth() / 2)) + ", " + (position.y + (canvas.getHeight() / 2)) + ", " + position.z + ")");
        System.out.println("Cam center (World): (" + position.x + ", " + position.y + ", " + position.z + ")");
    }
}