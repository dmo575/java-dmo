package learning;

// following tutorials from:
// https://www.youtube.com/watch?v=s5srQHN_lnI&list=PL656DADE0DA25ADBB&index=3

import java.awt.Canvas;
import javax.swing.JFrame;

public class Main extends Canvas implements Runnable{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private Thread thread;
    private boolean running = false;

    // start a thread and give it the Runnable object we want the thread to run.
    private void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();

        System.out.println("thread started");
    }

    private void stop() {
        if (!running) return;
        running = false;

        try {
            thread.join();
        } catch(InterruptedException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        System.out.println("thread stopped");
    }

    // what the thread will run (this method comes from the Runnable interface)
    public void run() {
        while (running) {
            
        }
    }

    public static void main(String[] args) {
        
        Main game = new Main();
        // create a frame and pass in a display
        JFrame frame = new JFrame();
        frame.add(game);

        // window set up
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Window-title");

        game.start();
    }
}
