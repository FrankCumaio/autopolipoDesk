/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package autopolipo1;


import com.mysql.jdbc.exceptions.*;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.DriverManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class JifNovoProprietario extends JInternalFrame {
    
    private JLabel  lbGravar, lbCancelar,lbNovoCliente, lbGravarNovo, lbRemover, lbID,lbNome, lbBI, lbContacto, lbMorada,lbTipoCliente;
    private JRadioButton jrbTipoPessoal, jrbTipoEmpresarial;
    private JTextField txtID,txtNome, txtBI, txtContacto, txtMorada, txtTipoCliente;
    private JPanel painelAdd, painelButton;
    private DesktopMenu dp;
    private JifViatura viaturas;
    private Connection con;
    private Statement st;
    private String tipoClienteSelect;
    private int linha =0;
    private ButtonGroup grupo;
    private int cont=0;
    private final DefaultTableModel tableModel = new DefaultTableModel();
   
      public void Adicionar() throws IOException{
       String nome,morada,bi,tipoCliente="";
     
       
        int contacto,lastId = 0,id = 0;
        ResultSet rs;
        
         try{
            String query ="SELECT * FROM `clientes`  ";
            rs=st.executeQuery(query);
            while(rs.next()){
              
                lastId = rs.getInt("ID");
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null, "Erro na Base de dados!","Erro",JOptionPane.ERROR_MESSAGE);
             }
        
        try{
         //   id=Integer.parseInt(txtID.getText());
            nome=txtNome.getText();
            if(jrbTipoPessoal.isSelected())
            tipoCliente="Pessoal";
            if(jrbTipoEmpresarial.isSelected())
            tipoCliente="Empresarial";
            morada= txtMorada.getText();
            bi=txtBI.getText();
            id = lastId+1;
            contacto = Integer.parseInt(txtContacto.getText());
            PreparedStatement st =(PreparedStatement) con.prepareStatement("INSERT INTO clientes (ID, tipocliente, nome, bi, morada, contacto) VALUES (?, ?, ?, ?, ?, ?)");
            st.setInt(1, id);
            st.setString(2, tipoCliente);
            st.setString(3, nome);
            st.setString(4, bi);
            st.setString(5, morada);
            st.setInt(6, contacto);
            if(("".equals(nome))||("".equals(tipoCliente))||("".equals(morada))||("".equals(bi))||(txtContacto.getText().equals(""))){
                JOptionPane.showMessageDialog(null,"Nenhum campo deve estar vazio","Atencao",JOptionPane.WARNING_MESSAGE);
               
            }
            try{
            st.setInt(1, id);
            st.executeUpdate();
            }catch (MySQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(null,e+"Ja existe um cliente com este contacto(numero de telefone).\n Introduza um novo numero.");
        }
            
            
            JOptionPane.showMessageDialog(null,"Cliente adicionado com sucesso!","Informacao",JOptionPane.INFORMATION_MESSAGE);
            
            JifViatura jifV = new JifViatura();
                    jifV.jcbProrietario.setSelectedItem(nome);
                   jifV.setVisible(true);
                   getParent().add(jifV);
                   jifV.setBounds(370,100,630,315);
                    jifV.setBounds(390, 50, 630, 430);
                 jifV.setTitle("Entrada de viaturas");
            
                   dispose();
            
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Verifique se todos campos foram devidamente preenchidos \n e/ou se os tipos de dado introduzidos sao validos!","Erro",JOptionPane.ERROR_MESSAGE);
            return;
        }       catch (SQLException ex) {
                    Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                }
      
          
   }
      
      
      
  
  
    
      
    public JifNovoProprietario() throws HeadlessException {
 
        
         super("Menu Clientes");

        setContentPane(new JPanel());
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
          st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados","Erro",JOptionPane.ERROR_MESSAGE);
        }
        
        //Painel de Entrada
        
        painelAdd = new JPanel(new MigLayout());
        painelAdd.setBorder(BorderFactory.createTitledBorder("Adicionar Cliente"));
        painelAdd.setBounds(5,0,600,200);
        //painelAdd.setBorder(BorderFactory.createTitledBorder("Adicionar Cliente"));
        painelAdd.setBounds(5,0,600,200);
        
         painelButton = new JPanel( new MigLayout());
        painelButton.setBorder(BorderFactory.createEtchedBorder());
        painelButton.setBounds(5,200,600,70);
        
        lbNome = new JLabel("Nome");
        
        lbBI = new JLabel("Bilhete de Identificacao");
        lbContacto = new JLabel("Contacto (telefone)");
        lbMorada = new JLabel("Morada");
        lbTipoCliente = new JLabel("Tipo de Cliente");
        jrbTipoPessoal = new JRadioButton("Pessoal");
        jrbTipoEmpresarial = new JRadioButton("Empresarial");
        
        jrbTipoPessoal.setMnemonic(KeyEvent.VK_J);
        jrbTipoEmpresarial.setMnemonic(KeyEvent.VK_C);

         ///
             grupo = new ButtonGroup();
         grupo.add(jrbTipoEmpresarial);
         grupo.add(jrbTipoPessoal);
        

        txtBI = new JTextField(15);
        txtContacto = new JTextField(15);
        txtMorada = new JTextField(50);
        txtNome = new JTextField(50);
        txtTipoCliente = new JTextField(15);
        JButton jbGravar = new JButton("Gravar");
       
        JButton jbCancelar = new JButton("Cancelar");
      
        
        ClassLoader loader = getClass().getClassLoader();
        jbGravar = new JButton(new ImageIcon(loader.getResource("Icons/Save.png")));
        jbGravar.setText("Gravar");
        jbCancelar = new JButton(new ImageIcon(loader.getResource("Icons/Cancel.png")));
        jbCancelar.setText("Cancelar");
        
        painelAdd.add(lbNome);
        painelAdd.add(txtNome, "span, growx");
        
        painelAdd.add(lbMorada);
        painelAdd.add(txtMorada, "span, growx");
        
        painelAdd.add(lbTipoCliente);
        painelAdd.add(jrbTipoPessoal);
        painelAdd.add(jrbTipoEmpresarial,"span");
        
        painelAdd.add(lbBI);
        painelAdd.add(txtBI, "span,growx");
 
        painelAdd.add(lbContacto);
        painelAdd.add(txtContacto, "span, growx");
        
        
   
        
        painelButton.add(jbGravar,  " gapleft 200" );
        jbGravar.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent ae) {
                 
                     try {
                         Adicionar();
                     } catch (IOException ex) {
                         Logger.getLogger(JifNovoProprietario.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 
                
                // setVisible(false);
             
             }
         });
        
        
        
       painelButton.add(jbCancelar,"span");
       jbCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
               
    
                int dialogResult = JOptionPane.showConfirmDialog(null, "Deseja cancelar a operação", "Atenção", JOptionPane.YES_NO_OPTION);
               
                if(dialogResult == 0){
                    
                    try {
                        JifViatura jifV = new JifViatura();
                        jifV.setVisible(true);
                        getParent().add(jifV);
                       // jifV.setBounds(370,100,630,315);
                        jifV.setBounds(390, 50, 630, 430);
                        jifV.setTitle("Entrada de viaturas");
                        
                        dispose();
                    } catch (IOException ex) {
                        Logger.getLogger(JifNovoProprietario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
           
            }
        });
       
       
      
     this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     this.setPreferredSize(new Dimension(630,315));
     this.pack();
     this.setVisible(true);
     this.add(painelAdd);
     this.add(painelButton);  
        
        
        
   
        
    
        
        
    }
    


 
   }
   
   
        

    
    
    

