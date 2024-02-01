package learning;
import java.awt.*;


public class Main {
    
    public static void main(String[] args) {
        // top level container.

        ExampleFrame frame = new ExampleFrame();
    }
}

class ExampleFrame extends Frame{
    ExampleFrame() {
        Button button = new Button();
        button.setLabel("Sample button");
        button.setBounds(30, 100, 80, 30);

        add(button);
        setSize(300, 300);
        setLayout(null);
        setVisible(true);
    }
}