package learning.awt;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

public class Main2 {

    public static void main(String[] args) {

        Frame frame = new Frame();
        frame.setSize(800, 400);
        frame.setVisible(true);
        frame.setLayout(new GridLayout());
        frame.setLocationRelativeTo(null);

        Canvas canvas = new MyCanvas();

        frame.add(canvas);
        canvas.createBufferStrategy(2);
        frame.add(new Btn_close());
        frame.add(new Btn_paint(canvas));
    }
}

class MyCanvas extends Canvas {

    BufferStrategy bufferStrat;
    MyCanvas() {
        // canvas set up
        setSize(300, 300);
        setBackground(Color.LIGHT_GRAY);
    }

    @Override
    public void paint(Graphics g) {

        System.out.println("paint");
        BufferStrategy bufferStrat = getBufferStrategy();
        Graphics grap = bufferStrat.getDrawGraphics();

        bufferStrat.show();
    }
}

class Btn_paint extends Button {

    boolean swap = false;
    Canvas canvas;

    Btn_paint(Canvas canvas) {
        this.canvas = canvas;
        setLabel("PAINT");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prep_img();
                canvas.repaint();
            }
        });
    }

    private void prep_img() {

        swap = !swap;
        BufferStrategy st = canvas.getBufferStrategy();
        Graphics grap = st.getDrawGraphics();

        if(swap) {
            grap.setColor(Color.BLUE);
            grap.drawRect(0, 0, 25, 50);
            grap.setColor(Color.RED);
            grap.fillRect(0, 0, 15, 25);
            return;
        }
        grap.setColor(Color.RED);
        grap.drawRect(0, 0, 25, 50);
        grap.setColor(Color.BLUE);
        grap.fillRect(20, 20, 15, 25);
    }
}