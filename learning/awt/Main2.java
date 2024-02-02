package learning.awt;
import java.awt.*;

public class Main2 {

    public static void main(String[] args) {

        Frame frame = new Frame();
        frame.setSize(800, 400);
        frame.setVisible(true);
        frame.add(new MyCanvas());
        frame.setLayout(new GridLayout());
        frame.add(new Btn_close());
        
    }
}

class MyCanvas extends Canvas {

    MyCanvas() {
        setSize(300, 300);
        setBackground(Color.LIGHT_GRAY);
    }
}