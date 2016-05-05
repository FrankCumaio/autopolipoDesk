
package autopolipo1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;


public class Main {
    boolean logSucess;
  
     public static void main(String args[]) {
         new Splash();
         
     try {
            for (LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                  setLookAndFeel(info.getClassName());
                  break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DesktopMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    
     }
}
