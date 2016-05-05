/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autopolipo1;

import java.awt.Dimension;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Mr. Belton
 */
public class Info extends JInternalFrame {
    JLabel logo = new JLabel();
    JTextArea info = new JTextArea();
    JPanel jpLogo = new JPanel();
    JScrollPane sp1;
    ClassLoader loader = getClass().getClassLoader();
    String texto;
    
    public Info(){
        super("Sistema de Gestao de Oficina AutoPolipo v1.1");
         setContentPane(new JPanel());
        setLayout(null);
        setResizable(false);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        jpLogo = new JPanel(new MigLayout());
        jpLogo.setBorder(BorderFactory.createEtchedBorder());
        jpLogo.setBounds(10,5,430,540);
        logo.setBounds(5,5,355,400);
        logo.setIcon(new ImageIcon(loader.getResource("Icons/logo2.png")));
        jpLogo.add(logo,"span");
        
        texto = "\n\tSistema de Gestao de Oficina AutoPolipo v1.1 \n \n AutoPolipo v1.1 e um sistema desenvolvido pelos estudantes Frank \n Salvador Cumaio e Inercio Belton Simao Fanequico, com o objectivo\n"
                + " de melhorar os servicos prestados pela Oficina Auto Polipo no que \n diz  respeito a gestao e organizacao das tarefas e actividades que \n sao  desenvolvidas por esta empresa. \n"
                + "\n O IDE usado para o desenvolvimento desta aplicacao foi o \n NetBeans IDE 8.0.2"
                + "\n \n \n Versao do produto:   AutoPolipo V1.1 (versao inicial AutoPolipo v1.0)"
                + "\n Runtime:   Java(TM) SE Runtime Environment 1.8.0_31-b13" 
                + "\n Sistema:   Windows 10 Technical Preview build 9926 \n                  & Windows 7  correndo em Intel(R) core(TM) i3-3110M CPU                  & Intel(R) core(TM) i7-3610QM CPU  respectivamente.";
        
        info = new JTextArea(15, 38);
        info.setLineWrap(true);
        info.setText(texto);
        info.setEditable(true);
        
        sp1 = new JScrollPane(info);
        sp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
       
      
        sp1.setBounds(30, 390, 330, 250);
        jpLogo.add(sp1);
 
        
        add(jpLogo);
      
        setVisible(true);
        pack();
        setMinimumSize(new Dimension(465,595));
        setClosable(true);
    }
  
}
