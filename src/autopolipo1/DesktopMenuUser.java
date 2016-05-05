/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autopolipo1;

import autopolipo1.ClienteForm;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.BevelBorder;


public class DesktopMenuUser extends JFrame {
    
    
    
    
    boolean visivel;
    int cont =0;
    ClienteForm jifCliente;
    JifViatura jifViatura;
     JifOficina jiOf;
    JifUsuario user;
  //  FmLogin lg = new FmLogin();
    JifArquivoUser jifArquivo = new JifArquivoUser();
      JifPecas acesssorios = new JifPecas();
    Info inf = new Info();
    Connection con;
    Statement st;
    String activo = "1";
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private ResultSet rs;
    private String nome;
    private JLabel jlUser = new JLabel();
    private JLabel tipoUser = new JLabel();
    private JDesktopPane desktopMenu = new JDesktopPane(); 
  
    
   
     public String getDataActual(){
          DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
          java.sql.Date hoje = null;
          return formato.format(Calendar.getInstance().getTime());
      }
      
      public String getHoraActual(){
          DateFormat formato = new SimpleDateFormat("HH:mm:ss");
          return formato.format(Calendar.getInstance().getTime());
      }
      
      
      
    
    

    public DesktopMenuUser() throws IOException, SQLException{
        this.user = new JifUsuario();
      
        ClassLoader loader = getClass().getClassLoader();
        File arquivo = new File((new File("log.txt")).getCanonicalPath());
        
        JFrame frame = new JFrame("Oficina Auto Polipo v1.0");
        JPanel Painel = new JPanel(new BorderLayout());
        
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(this.getWidth(), 25));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(this.getWidth(), 25));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
              
        ClockLabel clock = new ClockLabel();
       // Date data = new Date();
        JLabel jlData = new JLabel();
        JLabel jldataActual = new JLabel();
        jldataActual.setText("     Data: "+getDataActual()+"\n \n");
        LoadData();
        nome=jlUser.getText();
        jlUser.setText("Utilizador - "+jlUser.getText()+"  Hora: ");
        jlData=clock;
        jlUser.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(jlUser);
        statusPanel.add(jlData);
        statusPanel.add(jldataActual);
        
        
      desktopMenu = new JDesktopPane(){
         Image im =(new ImageIcon(loader.getResource("Icons/Blue.jpg"))).getImage();
         
         @Override
         public void paintComponent(Graphics g){
             int dimX = desktopMenu.getWidth()/2-970;
             int dimY = desktopMenu.getHeight()/2-350;
             g.drawImage(im,dimX,dimY,this);
         }
       };
        
       JScrollPane jScrollPane1 = new JScrollPane();
       
       
       JMenuBar jMenuBar = new JMenuBar();
       JMenu jmFicheiro = new JMenu("Sistema"),
             jmViaturas = new JMenu("Viaturas"),
             jmCliente = new JMenu("Clientes"),
             jmSobre = new JMenu("Sobre");
        
        jmSobre.setIcon(new ImageIcon(loader.getResource("Icons/Info.png")));
       JMenuItem jmSair = new JMenuItem("Sair"),
                 jmEntradas = new JMenuItem("Entradas"),
                jmArquivo = new JMenuItem("Arquivo"),
                 jmOficina = new JMenuItem("Oficina"),
                jmiClientes = new JMenuItem("Menu Clientes"),
                     jmInfo = new JMenuItem("Informacoes"),
                     jmAcessorios = new JMenuItem("Acessorios"),
                     jmLogout = new JMenuItem("Terminar Sessao");
                  
        
       jmiClientes.setIcon(new ImageIcon(loader.getResource("Icons/menu.png")));
        jMenuBar.add(jmFicheiro);
        jMenuBar.add(jmViaturas);
        jMenuBar.add(jmCliente);
        jMenuBar.add(jmSobre);
        
        jmFicheiro.add(jmLogout);
        jmLogout.setIcon(new ImageIcon(loader.getResource("Icons/Logout.png")));
        jmLogout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                try {
                    new FmLogin();
                } catch (IOException ex) {
                    Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose();
                
                
                
                try {
                      
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("UPDATE `usuario` SET `activo`='0' WHERE nome='"+nome+"'");
                           activo="";   
            st.executeUpdate();
          
                  } catch (Exception es) {
        }
                
                
                
                 try {
 
if (!arquivo.exists()) {

arquivo.createNewFile();
}
File[] arquivos = arquivo.listFiles();
FileWriter fw = new FileWriter(arquivo, true);
BufferedWriter bw = new BufferedWriter(fw);
   
                bw.write("Logout: "+jlUser.getText()+""+getHoraActual()+ " Data: "+getDataActual());
                bw.newLine();
                bw.write("-------------------------------------------------------------------------------------------------------------------------------------------------");
                bw.newLine();
                bw.close();
                fw.close();
            
            } catch (IOException ex) {
              ex.printStackTrace();
            }
            }
        });
        
        
          
        jmViaturas.add(jmAcessorios);
        jmAcessorios.setIcon(new ImageIcon(loader.getResource("Icons/Options.png")));
        acesssorios = null;
        jmAcessorios.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 if(e.getSource()==jmAcessorios){
          
            if (acesssorios == null) {
                try {
                    acesssorios = new JifPecas();
                } catch (IOException ex) {
                    Logger.getLogger(DesktopMenuUser.class.getName()).log(Level.SEVERE, null, ex);
                }
           acesssorios.setVisible(true);
           acesssorios.setBounds(400,50,490,490);
            acesssorios.setClosable(true);
        
            desktopMenu.add(acesssorios);
            
        }
        desktopMenu.moveToFront(acesssorios);
    
    if (acesssorios.isClosed()) {
                try {
                    acesssorios = new JifPecas();
                } catch (IOException ex) {
                    Logger.getLogger(DesktopMenuUser.class.getName()).log(Level.SEVERE, null, ex);
                }
        acesssorios.setVisible(true);
        acesssorios.setBounds(400,50,490,490);
        desktopMenu.add(acesssorios);
        acesssorios.setClosable(true);
      //  acesssorios.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        desktopMenu.moveToFront(acesssorios);
    }     
                }
            
            }
        });
        
        
        
        
         jmSobre.add(jmInfo);
         jmInfo.setIcon(new ImageIcon(loader.getResource("Icons/About.png")));
         inf = null;
         jmInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==jmInfo){
          
            if (inf == null) {
            inf = new Info();
           inf.setVisible(true);
           inf.setBounds(470,20,465,595);
            inf.setClosable(true);
        
            desktopMenu.add(inf);
            
        }
        desktopMenu.moveToFront(inf);
    
    if (inf.isClosed()) {
        inf = new Info();
        inf.setVisible(true);
        inf.setBounds(470,20,465,595);
        desktopMenu.add(inf);
        inf.setClosable(true);
    
        desktopMenu.moveToFront(inf);
    }     
                }
            }
        });
         
       
        
    
        
        
        
        jmViaturas.add(jmArquivo);
        jmArquivo.setIcon(new ImageIcon(loader.getResource("Icons/archive.png")));
        jifArquivo = null;
        
        jmArquivo.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent ae) {

                 if (jifArquivo == null) {
                     try {
                         jifArquivo = new JifArquivoUser();
                     } catch (HeadlessException ex) {
                         Logger.getLogger(DesktopMenuUser.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (IOException ex) {
                         Logger.getLogger(DesktopMenuUser.class.getName()).log(Level.SEVERE, null, ex);
                     }
            jifArquivo.setVisible(true);
 
     jifArquivo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     jifArquivo.pack();
    
     jifArquivo.setClosable(true);
     jifArquivo.setIconifiable(true);
      jifArquivo.setVisible(true);
                     try {
                         System.out.println(""+new JifArquivo().getComponentCount());
                     } catch (HeadlessException ex) {
                         Logger.getLogger(DesktopMenuUser.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (IOException ex) {
                         Logger.getLogger(DesktopMenuUser.class.getName()).log(Level.SEVERE, null, ex);
                     }
        desktopMenu.add(jifArquivo);
        try {     
        jifArquivo.setSelected(true);         
        jifArquivo.setMaximizable(false);        
        jifArquivo.setMaximum(true);     
    } catch (java.beans.PropertyVetoException e) {}
        }
        desktopMenu.moveToFront(jifArquivo);
    
    if (jifArquivo.isClosed()) {
                     try {
                         jifArquivo = new JifArquivoUser();
                     } catch (HeadlessException ex) {
                         Logger.getLogger(DesktopMenuUser.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (IOException ex) {
                         Logger.getLogger(DesktopMenuUser.class.getName()).log(Level.SEVERE, null, ex);
                     }
        jifArquivo.setVisible(true);
     
     jifArquivo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     jifArquivo.pack();
     jifArquivo.setClosable(true);
     jifArquivo.setIconifiable(true);
      jifArquivo.setVisible(true);
      desktopMenu.add(jifArquivo);
        try {     
        jifArquivo.setSelected(true);         
        jifArquivo.setMaximizable(false);        
        jifArquivo.setMaximum(true);     
    } catch (java.beans.PropertyVetoException e) {}
        
        desktopMenu.moveToFront(jifArquivo);
    }
                 
             }
         });
        
        
        
        
        jmFicheiro.add(jmSair);
        jmSair.setIcon(new ImageIcon(loader.getResource("Icons/saida.png")));
        jmFicheiro.setIcon(new ImageIcon(loader.getResource("Icons/file.png")));
        jmSair.addActionListener(new ActionListener() {
            @Override
            
            public void actionPerformed(ActionEvent ae){
                int confirm = JOptionPane.showConfirmDialog(null,"Deseja mesmo sair?","Sair do Sistema",JOptionPane.YES_NO_OPTION);
                if(confirm ==0){
                      try {
                      
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("UPDATE `usuario` SET `activo`='0' WHERE nome='"+nome+"'");
                           activo="";   
            st.executeUpdate();
          
                  } catch (Exception es) {
        }
                      
                      try {
 
if (!arquivo.exists()) {

arquivo.createNewFile();
}
File[] arquivos = arquivo.listFiles();
FileWriter fw = new FileWriter(arquivo, true);
BufferedWriter bw = new BufferedWriter(fw);
   
                bw.write("Logout: Usuario - "+jlUser.getText()+""+getHoraActual()+ " Data: "+getDataActual());
                bw.newLine();
                bw.write("-------------------------------------------------------------------------------------------------------------------------------------------------");
                bw.newLine();
                bw.close();
                fw.close();
            
            } catch (IOException ex) {
              ex.printStackTrace();
}
                      
                System.exit(0);
                }
            }
        });
        jmViaturas.add(jmEntradas);
        jmViaturas.setIcon(new ImageIcon(loader.getResource("Icons/Car.png")));
       
        jmEntradas.setIcon(new ImageIcon(loader.getResource("Icons/Carro.png")));
        jifViatura = null;
        jmEntradas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){       
                 if (ae.getSource() == jmEntradas) {
            if (jifViatura == null) {
                try {
                    jifViatura = new JifViatura();
                } catch (IOException ex) {
                    Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            jifViatura.setBounds(390, 50, 630, 430);
            jifViatura.setTitle("Entrada de viaturas");
            jifViatura.setVisible(true);
            desktopMenu.add(jifViatura);
        }
        desktopMenu.moveToFront(jifViatura);
    
    if (jifViatura.isClosed()) {
                try {
                    jifViatura = new JifViatura();
                } catch (IOException ex) {
                    Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
        jifViatura.setBounds(390, 50, 630, 430);
        jifViatura.setTitle("Entrada de viaturas");
        jifViatura.setVisible(true);

        desktopMenu.add(jifViatura);
        desktopMenu.moveToFront(jifViatura);
    }
   }
        }
        });
        
        
        
        jmViaturas.add(jmOficina);
        jmOficina.setIcon(new ImageIcon(loader.getResource("Icons/Viatura.png")));
        jiOf = null;
        jmOficina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (e.getSource() == jmOficina) {
            if (jiOf == null) {
                try {
                    jiOf = new JifOficina();
                } catch (HeadlessException ex) {
                    Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            jiOf.setVisible(true);
 
     jiOf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     jiOf.pack();
     jiOf.setClosable(true);
     jiOf.setIconifiable(true);
      jiOf.setVisible(true);
        desktopMenu.add(jiOf);
         try {     
        jiOf.setSelected(true);         
        jiOf.setMaximizable(false);        
        jiOf.setMaximum(true);     
    } catch (java.beans.PropertyVetoException es) {}
            }
        desktopMenu.moveToFront(jiOf);
    
    if (jiOf.isClosed()) {
                try {
                    jiOf = new JifOficina();
                } catch (HeadlessException ex) {
                    Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
        jiOf.setVisible(true);
     
     jiOf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     jiOf.pack();
     jiOf.setClosable(true);
     jiOf.setIconifiable(true);
      jiOf.setVisible(true);
        desktopMenu.add(jiOf);
        try {     
        jiOf.setSelected(true);         
        jiOf.setMaximizable(false);        
        jiOf.setMaximum(true);     
    } catch (java.beans.PropertyVetoException es) {}
        desktopMenu.moveToFront(jiOf);
    }
   }
}
        });

        
        
        jmCliente.add(jmiClientes);
        jmCliente.setIcon(new ImageIcon(loader.getResource("Icons/Notes.png")));
        jifCliente = null;
        jmiClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
    if (ae.getSource() == jmiClientes) {
    if (jifCliente == null) {
        try {
            jifCliente = new ClienteForm();
            jifCliente.setBounds(380, 20, 630, 620);
            desktopMenu.add(jifCliente);
        } catch (HeadlessException ex) {
            Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        }
        desktopMenu.moveToFront(jifCliente);
    }
    if (jifCliente.isClosed()) {
        try {
            jifCliente = new ClienteForm();
        } catch (HeadlessException ex) {
            Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DesktopMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        jifCliente.setBounds(380, 20, 630, 620);
        desktopMenu.add(jifCliente);
        
        desktopMenu.moveToFront(jifCliente);
    }
 }
  
        });
        Painel.add(jMenuBar,BorderLayout.NORTH);
        Painel.add(desktopMenu,BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.add(Painel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800,600));
        frame.setIconImage(ImageIO.read(loader.getResource("Icons/iconFrame.png")));
    
    }
    
  
     
    public void LoadData(){
             try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados");
        }
           try{
            String query ="SELECT * FROM usuario WHERE `activo`=1";
            rs=st.executeQuery(query);
            while(rs.next()){
            jlUser.setText(rs.getString("nome"));
            tipoUser.setText(rs.getString("Tipo"));
            }
            con.close();
            dispose();
        }catch(Exception es){
            JOptionPane.showMessageDialog(null, "Erro"+es);
        }
        }
    
    
    
    
}
