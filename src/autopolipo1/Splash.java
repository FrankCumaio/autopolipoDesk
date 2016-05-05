/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autopolipo1;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author Mr. Belton
 */
public class Splash extends JWindow{
    
    AbsoluteLayout absoluto;
    AbsoluteConstraints absImage, absBarra; 
    ImageIcon image;
    JLabel lbimagem;
    JProgressBar barra;
    ClassLoader loader = getClass().getClassLoader();
    String LOG="Erro: ";
    
    public Splash(){
        setBounds(370, 170, 250, 250);
        
        absoluto = new AbsoluteLayout();  
        absImage = new AbsoluteConstraints(0,0);
        absBarra = new AbsoluteConstraints(0,0);
        lbimagem = new JLabel();
        image = new ImageIcon(loader.getResource("Icons/logo21.png"));
        lbimagem.setIcon(image);
        barra = new JProgressBar();
        barra.setPreferredSize(new Dimension(544,10));
        this.getContentPane().setLayout(absoluto);
        this.getContentPane().add(lbimagem, absImage);
        this.getContentPane().add(barra,absBarra);
      
        
        new Thread(){
            public void run(){
                int i=0;
                while(i<101){
                    barra.setValue(i);
                    i++;
                    try {
                        sleep(30);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
                    
                    }
                    }
                dispose();
                try {
                    new FmLogin();
                } catch (IOException ex) {
                    Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                }
            
        }.start();
        
        
        this.pack();
        this.setVisible(true);
        
    }
    
    public String Log(){
        return LOG;
    }
}