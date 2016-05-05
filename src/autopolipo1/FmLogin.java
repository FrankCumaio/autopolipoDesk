
package autopolipo1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
//import static jdk.nashorn.internal.codegen.Compiler.LOG;
import net.miginfocom.swing.MigLayout;

public class FmLogin{
    
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private final JPasswordField txtSenha;
    private final JTextField txtUsuario;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private  JButton jbOk = new JButton();
    private JButton jbCancelar = new JButton();
    private boolean found =false;
    private String nome,tipoUser,tipo;
    ClassLoader loader = getClass().getClassLoader();
    

   
     
      public void LoadData(){
        
    
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("select * from oficina");
            ResultSetMetaData metaData = rs.getMetaData();

            // Names of columns
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
            //for (int i = 1; i <= columnCount; i++) {
                columnNames.add("ID");
                columnNames.add("Marca");
                columnNames.add("Modelo");
                columnNames.add("Matricula");
                columnNames.add("Proprietario");
                columnNames.add("Avaria");
                columnNames.add("Observacao");
            //}

            // Data of the table
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getObject(i));
                }
                data.add(vector);
            }

            tableModel.setDataVector(data, columnNames);
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Exception in Load Data", e);
        }
    }
     
     
      public String getDataActual(){
          DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
          Date hoje = null;
          return formato.format(Calendar.getInstance().getTime());
      }
      
      public String getHoraActual(){
          DateFormat formato = new SimpleDateFormat("HH:mm:ss");
          return formato.format(Calendar.getInstance().getTime());
      }
      
    
      
      
    
    public FmLogin() throws IOException {
        
        File arquivo = new File((new File("log.txt")).getCanonicalPath());
        
        JFrame frame = new JFrame("Login");
        JPanel Painel = new JPanel(new MigLayout());
        JPanel jpDados = new JPanel(new MigLayout());
        JPanel jpBotoes = new JPanel((new MigLayout()));
        
        
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados","Erro",JOptionPane.ERROR_MESSAGE);
           // LOG = LOG+e+"\n \n";
        }
        
        JLabel jlUsuario = new JLabel("Usuario");
        JLabel jlSenha= new JLabel("Senha");
       JLabel jlIco = new JLabel();
        jlIco.setIcon(new ImageIcon(loader.getResource("Icons/Login.png")));
      
        txtSenha = new JPasswordField(20);
        txtUsuario = new JTextField(20);
 
       
        JLabel jlSpc = new JLabel(" ");
       jbCancelar.setIcon(new ImageIcon(loader.getResource("Icons/Cancel.png")));
       jbCancelar.setText("Cancelar");
       jbCancelar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == jbCancelar)
                    System.exit(0);
            }
        }
       );
       
       
       jbOk.setIcon(new ImageIcon(loader.getResource("Icons/Apply.png"))); 
        jbOk.setText("Entrar");
        
        jbOk.addActionListener(new java.awt.event.ActionListener() {
            public  void actionPerformed(java.awt.event.ActionEvent e) {
                if (txtUsuario.getText().equals("") || txtSenha.getText().equals(""))// se login e senha em branco
                 JOptionPane.showMessageDialog(null,"Campos Usuario e senha são obrigatórios","Atencao",JOptionPane.WARNING_MESSAGE);//mensagem

             else 
             {
                try{
            String query ="SELECT * FROM `usuario` WHERE `username` LIKE '"+txtUsuario.getText()+"' AND `senha` LIKE '"+txtSenha.getText()+"'";
            rs=st.executeQuery(query);
            if(rs.first()){
              String username = rs.getString("username");
              nome=rs.getString("nome");
              tipoUser = rs.getString("Tipo");
              String senha = rs.getString("senha");
              
                try {
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("UPDATE `usuario` SET `activo`=1 WHERE username='"+txtUsuario.getText()+"'");
                              st.executeUpdate();
                  } catch (Exception es) {
                    
        } 
               if(tipoUser.equals("Administrador")){
                  LoadData();
                new DesktopMenu();
                tipo = "Administrador";

               frame.setVisible(false);
               }else{
                    LoadData();
                new DesktopMenuUser();
                tipo = "Utilizador";
                
               frame.setVisible(false);
               }
               
               
               
                   try {
 
                if (!arquivo.exists()) {

                    arquivo.createNewFile();
                    }
                File[] arquivos = arquivo.listFiles();
                FileWriter fw = new FileWriter(arquivo, true);
                BufferedWriter bw = new BufferedWriter(fw);
   
                bw.write("Login: "+tipo+" - "+nome+" Hora: "+getHoraActual()+" Data: "+getDataActual());
                bw.newLine();
                bw.close();
                fw.close();
        
              
            } catch (IOException ex) {
             Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            
            
            else
              {JOptionPane.showMessageDialog(null, "Usuario e/ou Senha incorrectos.","Erro",JOptionPane.ERROR_MESSAGE);
                        txtUsuario.requestFocus();
            }
             }catch(Exception es){
            JOptionPane.showMessageDialog(null, "Erro!!!!"+es);
//            LOG = LOG+es+"\n \n";
             }
                }}
                 });
        
        txtSenha.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if(KeyEvent.getKeyText(e.getKeyCode()).equals("Enter")){
                    jbOk.getAction();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });
        
        
        jpBotoes.add(jbOk,"gapleft 230");
        jpBotoes.add(jbCancelar);
        
           Painel.add(jlIco,"gapleft 30");   
            jpDados.add(jlUsuario);
            jpDados.add(txtUsuario,"wrap");
            jpDados.add(jlSenha);
            jpDados.add(txtSenha,"wrap");
          
        Painel.add(jpDados,BorderLayout.CENTER);
        Painel.add(jpBotoes,BorderLayout.SOUTH);
        frame.setResizable(false);
        
        frame.add(Painel);
       
        //frame.dispose();
        frame.setUndecorated(true);
       // frame.setOpacity((float) 0.8);
        frame.setVisible(true);
         frame.pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit(); 
        Dimension screenSize = toolkit.getScreenSize(); 
        int x = (screenSize.width - frame.getWidth()) / 2; 
        int y = (screenSize.height - frame.getHeight()) / 2; 
        frame.setLocation(x, y); 
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        frame.setIconImage(ImageIO.read(loader.getResource("Icons/Power.png")));
       
       
    } 
    
    
    public String getTipo(){
        return tipoUser;
    }
                  
}



