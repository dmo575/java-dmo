package learning;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.border.Border;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;


public class Main {
    
    public static void main(String[] args) {
        // top level container.
        JFrame frame = new JFrame();
        JPanel contentPane = new JPanel(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();

        //contentPane.setBorder( -some border- );
        //contentPane.add( -some component-, BorderLayout.CENTER);
        //contentPane.add( -another component-, BorderLayout.PAGE_END);

        frame.setContentPane(contentPane);
        frame.setJMenuBar(menuBar);
    }
}
