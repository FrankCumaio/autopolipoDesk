
package autopolipo1;


import java.awt.event.*;
import java.text.*;
import java.util.Date;
import javax.swing.*;


public class ClockLabel extends JLabel implements ActionListener {
    private static final DateFormat FORMATO = new SimpleDateFormat("HH:mm:ss");
            public ClockLabel() {
            super("" + new Date());
            Timer t = new Timer(1000, this);
            t.start();
             }

           public void actionPerformed(ActionEvent ae) {
           setText(FORMATO.format(new Date()));
            }
        }
