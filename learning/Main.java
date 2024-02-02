package learning;
import java.awt.*;
import java.awt.event.ActionEvent;
// Java's Abstract Window Toolkit package.
import java.awt.event.ActionListener;


public class Main {
    
    public static void main(String[] args) {
        // top level container.
        ExampleFrame frame = new ExampleFrame();
    }
}

class ExampleFrame extends Frame{
    ExampleFrame() {
        Button button = new Button("Button");
        //button.setLabel("Sample button");
        button.setBounds(30, 100, 80, 30);
        add(button);
        
        final Label label = new Label();
        label.setName("Label");
        label.setText("getName()");
        add(label);
        
        setSize(300, 300);
        GridLayout grid = new GridLayout(10, 10);
        setLayout(grid);
        setVisible(true);
        
        //button.addActionListener(new ButtonAction(label));
        
        button.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent arg0) {
            label.setText("Anon");
        }
        });
    }
}

class AClass {

}

class ButtonAction implements ActionListener {

    private Label label;

    ButtonAction(Label label) {
        this.label = label;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if(label.getText() == "null") {
            label.setText("yeho");
        }
        else{
            label.setText("null");
        }
    }

}