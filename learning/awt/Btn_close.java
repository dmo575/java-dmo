package learning.awt;
import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Btn_close extends Button {
    Btn_close() {
        setLabel("CLOSE");
        setBounds(30, 100, 80, 30);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((Frame)getParent()).dispose();
            }
        });
    }
}
