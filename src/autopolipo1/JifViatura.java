/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autopolipo1;


import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import net.miginfocom.swing.MigLayout;

public class JifViatura extends JInternalFrame{
    
    private JLabel jlID,jlSpace,jlMarca,jlMatricula,jlProprietario,jlAvaria,jlObservacao,jlModelo;
    private JScrollPane jScrollPane1,jspPainelRolagem,jScrollPane2;
    public JButton jbCancelar,jbCancel,jbNovo,jbGravar;
    private String lastMatricula;
    private int currentID;
    public JComboBox jcbMarca,jcbProrietario,jcbModelo;
    private String[] novoArray, outroArray;
     private String marca,modelo;
      private String[] modelos = {""};
    private JCheckBox rbMecanica,rbEletrica,rbRevisao,rbBC;
    private JTextArea txtAvaria,txtObservacao;
    private JTextField txtID;
    private JTextField txtMatricula = new JTextField(15);

    private String[] marcas =  {"" ,"Toyota", "Nissan", "Honda", "Mazda" , "Subaru", "Mercedes", "Audi", "BMW", "Outra"};
    private ArrayList clientes = new ArrayList();
    public JPanel buttonPanel = new JPanel(new MigLayout("fillx,insets 0"));
    private String nomeViatura, adicionada, removida;
    public boolean vEditada = false, viaturaAdicionada=false,viaturaEditada,novo=true,sair =true,sucesso=false;
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private JLabel  lbGravar, lbCancelar,lbNovoCliente, lbGravarNovo, lbRemover, lbID,lbNome, lbBI, lbContacto, lbMorada,lbTipoCliente;
    private JRadioButton jrbTipoPessoal, jrbTipoEmpresarial;
    private JTextField txtNome, txtBI, txtContacto, txtMorada, txtTipoCliente;
    public JPanel painelAdd, painelButton;
    private DesktopMenu dp;
    private JifViatura viaturas;
    private MaskFormatter mascaraMatricula;
   
    private String tipoClienteSelect;
    private int linha =0;
    private ButtonGroup grupo;
    private int cont=0;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    
    public void conexao(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados");
        }
    }
    
    public void ClearAlteracao(){
        try {
           Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("UPDATE `oficina` SET `alteracao`='' WHERE ID='"+txtID.getText()+"'");
                              st.executeUpdate(); 
        } catch (Exception e) {
        }
         
    }
    
    public void EditarData(){
        conexao();
        LoadCombo();
        txtID.setEnabled(false);
        novo=false;
        try{
            String query ="SELECT * FROM oficina WHERE `alteracao`='editar'";
            rs=st.executeQuery(query);
            while(rs.next()){
            txtID.setText(rs.getString("ID"));
            String b=rs.getString("proprietario");
            jcbProrietario.setSelectedItem(b);
            // JOptionPane.showMessageDialog(null, ""+b);
            String a="";
            for (int i=0;i<marcas.length;i++){
                 if (rs.getString("marca").equals(marcas[i]))
                    a=marcas[i];
            }  
            jcbMarca.setSelectedItem(a);
            LoadModels();
            jcbModelo.setSelectedItem(rs.getString("modelo"));
            txtMatricula.setText(rs.getString("matricula"));
            String avaria=rs.getString("avaria");
            
            if (rs.getString("avaria").equals("Electrica"))
                rbEletrica.doClick();
            
             if (rs.getString("avaria").equals("Mecanica"))
                 rbMecanica.doClick();
            
             if (rs.getString("avaria").equals("Revisao"))
                 rbRevisao.doClick();
            
             if (rs.getString("avaria").equals("Bate-chapa/Pintura"))
                 rbBC.doClick();
            
             if (rs.getString("avaria").equals("Servico completo")){
                 rbRevisao.doClick();
                 rbBC.doClick();
                 rbEletrica.doClick();
                 rbMecanica.doClick();
                
             }
             
                 if (rs.getString("avaria").equals("ElectroMecanica")){
                     rbEletrica.doClick();
                      rbMecanica.doClick();

                 }
            
                     if(rs.getString("avaria").equals("Mecanica/(Bate-chapa/Pintura)")){
                         rbMecanica.doClick();
                         rbBC.doClick();
                     }
            
                         if(rs.getString("avaria").equals("Electrica/(Bate-chapa/Pintura)")){
                             rbEletrica.doClick();
                             rbBC.doClick();
                         }
            
                             if(rs.getString("avaria").equals("ElectroMecanica/(Bate-chapa/Pintura)")){
                                 rbEletrica.doClick();
                                 rbMecanica.doClick();
                                 rbBC.doClick();
                             }
                             
                             if(rs.getString("avaria").equals("Mecanica/revisao")){
                                 rbMecanica.doClick();
                                 rbRevisao.doClick();
                             }
                             
                                         
            txtObservacao.setText(rs.getString("observacao"));
            }
            con.close();
            //dispose();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }
      
    }
    public void UpdateData(){
        conexao();
     try {          String avaria="";
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                
                    if(rbMecanica.isSelected())
                avaria = "Mecanica";
            
                  if(rbEletrica.isSelected())
                    avaria = "Electrica"; 
            
                   if(rbRevisao.isSelected())
                        avaria = "Revisao";
            
                      
               if(rbBC.isSelected())
                    avaria = "Bate-chapa/Pintura";
            
            
                   
                  if(rbMecanica.isSelected() && rbEletrica.isSelected()){
                avaria = "ElectroMecanica";
                  }
                 
                  if((rbMecanica.isSelected()) && (rbBC.isSelected()))
                      avaria = "Mecanica/(Bate-chapa/Pintura)";
            
                  if((rbEletrica.isSelected()) && (rbBC.isSelected()))
                      avaria = "Electrica/(Bate-chapa/Pintura)";
             
                  if(rbEletrica.isSelected() && rbMecanica.isSelected() && rbBC.isSelected())
                              avaria ="ElectroMecanica/(Bate-chapa/Pintura)";
              
                          
                           if((rbMecanica.isSelected() && rbRevisao.isSelected()) || (rbEletrica.isSelected() && rbRevisao.isSelected()) || (rbRevisao.isSelected() && rbBC.isSelected()) || (rbEletrica.isSelected() && rbMecanica.isSelected() && rbBC.isSelected() &&rbRevisao.isSelected()) || (rbEletrica.isSelected() && rbMecanica.isSelected() && rbRevisao.isSelected()) || (rbMecanica.isSelected() && rbBC.isSelected() && rbRevisao.isSelected()) || (rbEletrica.isSelected() && rbRevisao.isSelected() && rbBC.isSelected()))
                            avaria = "Servico completo";
            
                          
                  
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("UPDATE `oficina` SET `marca` = '"+jcbMarca.getSelectedItem()+"',`modelo` = '"+jcbModelo.getSelectedItem()+"', `matricula` = '"+txtMatricula.getText()+"', `proprietario` = '"+jcbProrietario.getSelectedItem()+"', `avaria` = '"+avaria+"', `observacao` = '"+txtObservacao.getText()+"', `alteracao`=''  WHERE `ID` = '"+txtID.getText()+"'");
                              st.executeUpdate();
                              JOptionPane.showMessageDialog(null,"Dados actualizados com sucesso","Informacao",JOptionPane.INFORMATION_MESSAGE);
                             
                              
                              JifOficina JifOficina = new JifOficina();
                              JifOficina.setVisible(true);
                              getParent().add(JifOficina);
                              try {     
        JifOficina.setSelected(true);     
        //diz que a janela interna é maximizável     
        JifOficina.setMaximizable(false);     
        //set o tamanho máximo dela, que depende da janela pai     
        JifOficina.setMaximum(true);     
    } catch (java.beans.PropertyVetoException e) {}
        JifOficina.setTitle("Viaturas em reparacao");
                                 
        } catch (Exception e) {
        }     
    }
        
    public void SetData(){
        conexao();
              String nome,matricula,modelo,marca,avaria="", observacao = null;
        int contacto,id;
        
         
         
        
        try{
            
           
            
            
            
            id=Integer.parseInt(txtID.getText());
            currentID = id;
            nome= (String) jcbProrietario.getSelectedItem();
            marca=jcbMarca.getSelectedItem().toString();
            modelo= jcbModelo.getSelectedItem().toString();
            matricula=txtMatricula.getText();
            observacao = txtObservacao.getText();
            
              if(rbMecanica.isSelected())
                avaria = "Mecanica";
            
                  if(rbEletrica.isSelected())
                    avaria = "Electrica"; 
            
                   if(rbRevisao.isSelected())
                        avaria = "Revisao";
            
                      
               if(rbBC.isSelected())
                    avaria = "Bate-chapa/Pintura";
            
            
                   
                  if(rbMecanica.isSelected() && rbEletrica.isSelected()){
                avaria = "ElectroMecanica";
                  }
                 
                  if((rbMecanica.isSelected()) && (rbBC.isSelected()))
                      avaria = "Mecanica/(Bate-chapa/Pintura)";
            
                  if((rbEletrica.isSelected()) && (rbBC.isSelected()))
                      avaria = "Electrica/(Bate-chapa/Pintura)";
             
                  if(rbEletrica.isSelected() && rbMecanica.isSelected() && rbBC.isSelected())
                              avaria ="ElectroMecanica/(Bate-chapa/Pintura)";
              
                          
                           if((rbMecanica.isSelected() && rbRevisao.isSelected()) || (rbEletrica.isSelected() && rbRevisao.isSelected()) || (rbRevisao.isSelected() && rbBC.isSelected()) || (rbEletrica.isSelected() && rbMecanica.isSelected() && rbBC.isSelected() &&rbRevisao.isSelected()) || (rbEletrica.isSelected() && rbMecanica.isSelected() && rbRevisao.isSelected()) || (rbMecanica.isSelected() && rbBC.isSelected() && rbRevisao.isSelected()) || (rbEletrica.isSelected() && rbRevisao.isSelected() && rbBC.isSelected()))
                            avaria = "Servico completo";
               
              
               
            PreparedStatement st =(PreparedStatement) con.prepareStatement("INSERT INTO oficina (ID, marca, modelo, matricula, proprietario, avaria, observacao) VALUES (?, ?, ?, ?, ?, ?, ?)");
            st.setInt(1, id);
            st.setString(2, marca);
            st.setString(3, modelo);
            st.setString(4, matricula);
            st.setString(5, nome);
            st.setString(6, avaria);
            st.setString(7, observacao);
          
            
            if(txtMatricula.getText().equals(lastMatricula)){
                JOptionPane.showMessageDialog(null,"A matricula introduzida pertence a uma viatura ja registrada!","Erro!",JOptionPane.ERROR_MESSAGE);
                return;
            }
            
           
             if((jcbProrietario.getSelectedItem()==null || jcbProrietario.getSelectedItem().equals(" ")) || (jcbMarca.getSelectedItem()==null) || (jcbModelo.getSelectedItem()==null) || (txtMatricula.getText()==null)){
                   JOptionPane.showMessageDialog(null, "Preencha todos os campos","Atencao",JOptionPane.WARNING_MESSAGE);
                   viaturaAdicionada = false;
                   vEditada = false;
                   return;
               }
            
            if(txtMatricula.getText().equals("")|| (rbMecanica.isSelected() == false && rbRevisao.isSelected() == false && rbEletrica.isSelected() == false && rbBC.isSelected() == false) || jcbModelo.getSelectedItem().equals("") || jcbMarca.getSelectedItem().equals("") || jcbProrietario.getSelectedItem().equals("")){
                     JOptionPane.showMessageDialog(null,"Nenhum campo deve estar vazio","Atencao",JOptionPane.WARNING_MESSAGE);
                     return;
            }
              
             
            
            if(txtMatricula.getText().equals("")|| (rbMecanica.isSelected() == false && rbRevisao.isSelected() == false && rbEletrica.isSelected() == false && rbBC.isSelected() == false) || jcbModelo.getSelectedItem().equals("") || jcbMarca.getSelectedItem().equals("") || jcbProrietario.getSelectedItem().equals("")){
                     JOptionPane.showMessageDialog(null,"Nenhum campo deve estar vazio","Atencao",JOptionPane.WARNING_MESSAGE);
                     return;
            }
                
            boolean existe = false;
            try{
            String query ="SELECT * FROM `oficina`  ";
            rs=st.executeQuery(query);
            while(rs.next()){
              
                lastMatricula = rs.getString("matricula");
                if(txtMatricula.getText().equals(rs.getString("matricula")))
                existe = true;
               
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null, "Erro na Base de dados!","Erro",JOptionPane.ERROR_MESSAGE);
             }
            
            if(existe == true){
                JOptionPane.showMessageDialog(null,"A matricula introduzida pertence a uma viatura ja registrada!","Erro!",JOptionPane.ERROR_MESSAGE);
                return;
            }
              st.executeUpdate();
            JOptionPane.showMessageDialog(null,"Viatura registrada com sucesso!","Informacao",JOptionPane.INFORMATION_MESSAGE);
            viaturaAdicionada=true; 
            vEditada = true;
            nomeViatura = ("de marca: "+marca+", modelo: "+modelo+", matricula: "+matricula+", percentecente ao proprietario: '"+nome+"'");
            sair=true;
             novo=true;
            con.close();
            //dispose();
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Verifique os tipos de dado introduzido!","Erro",JOptionPane.ERROR_MESSAGE);
            return;
        } catch (SQLException ex) {
            Logger.getLogger(JifViatura.class.getName()).log(Level.SEVERE, null, ex);
        }
        Limpar();
    }
       

    public boolean Editada(){
        return vEditada;
    }
    
   public void LoadCombo(){
         conexao();
        try{
            
            String query ="SELECT * FROM `clientes`";
            rs=st.executeQuery(query);
            if(rs.first()){
                clientes.add(" ");
                clientes.add("Novo");}
           while(rs.next()){
            clientes.add(rs.getString("nome"));
            }
           jcbProrietario.setModel(new DefaultComboBoxModel(clientes.toArray()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        } 
     }
   

     public void LoadModels(){
        if(jcbMarca.getSelectedItem().equals(""))
             jcbModelo.setModel(new DefaultComboBoxModel(new String[] {""}));
         else{
         
            if(jcbMarca.getSelectedItem().equals("Outra")){
            marca = JOptionPane.showInputDialog("Introduza a marca da viatura: ","marca");
            modelo = JOptionPane.showInputDialog("Introduza o modelo da viatura "+marca+": ","modelo");
            
            novoArray = new String[marcas.length+1];
            outroArray = new String[marcas.length+1]; 
            
            System.arraycopy(marcas,0,novoArray,0,marcas.length);
            for(int i=marcas.length; i<novoArray.length; i++){
             novoArray[i] = marca;
            }
             marcas = new String[novoArray.length];
            System.arraycopy(novoArray,0,marcas,0,novoArray.length);
            
            System.arraycopy(modelos,0,outroArray,0,modelos.length);
            for(int i=modelos.length; i<outroArray.length; i++){
             outroArray[i] = modelo;
            }
             modelos = new String[outroArray.length];
            System.arraycopy(outroArray,0,modelos,0,outroArray.length);
            
            jcbMarca.setModel(new DefaultComboBoxModel(marcas));
            jcbModelo.setModel(new DefaultComboBoxModel(modelos));
            jcbMarca.setSelectedItem(marca);
            jcbModelo.setSelectedItem(modelo);
            
            }else{
            
            
         if(jcbMarca.getSelectedItem().equals("Toyota"))
             jcbModelo.setModel(new DefaultComboBoxModel(new String[] {"Avensis","Allion","Altezza","Corolla Runx","Corolla Spacio","Camry","Chaser","Duet","Harrier","HIACE van","Hilux","Land Cruiser","Mark II","Prado","RAV4","Starlet","Vitz","Wish",}));
         else{
               if(jcbMarca.getSelectedItem().equals("Nissan"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"Atlas","Bluebird","Civilian","Datsun","Fairlady Z","Fuga","Gloria","Homy","Hardbody","Murano","March","Navara","Pulsar","Safari","Sunny","Skyline","Terrano","Tilda","X-Trail",}));
         else{
               if(jcbMarca.getSelectedItem().equals("Honda"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"Accord","Acty","Civic","CR-V","Fit","Fit Aria","HR-v","Insight","Integra","Logo","Mobilio","Odyssey","Partner","Prelude","S2000","Step Wagon","Stream","Street"}));
         else{
               if(jcbMarca.getSelectedItem().equals("Mazda"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"Atenza","Alexa","Bongo","Capella","Cx-5","Cx-7","Demio","Familia","Levante","Mazda 3","Mazda 5","MPV","Mx-6","Premacy","Rx-7","Rx-8","Titan","Tribute","Verisa",}));
         else{
               if(jcbMarca.getSelectedItem().equals("Mercedes"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"A-Class","B-Class","C-Class","CLK-Class","CLS-Class","E-Class","G-Class","GL-Class","GLK-Class","M-Class","ML-Class","SL-Class","SLK","SLR","Vaneo","V-Class","Viano"}));
         else{
               if(jcbMarca.getSelectedItem().equals("Subaru"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"Domingo","Forester","Impreza","Impreza ANESIS","Impreza WRX","Legacy","Legacy B4","Outback","R2","Sambar","Stella"}));
         else{
               if(jcbMarca.getSelectedItem().equals("Audi"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"A3","A4","A5","A6","A8","Allroad Quattro","Q5","Q7","Quattro","RS4","RS6","S3","S4","S5","S8","TT"}));
         else{
               if(jcbMarca.getSelectedItem().equals("BMW"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"1 Series","3 Series","5 Series","6 Series","7 Series","Mini","Mini Cooper","Mini Clubman","X1","X3","X5","X6","Z3","Z4","Z8"}));
         }}}}}}}}}
     }
     
     
    public void Limpar(){
            txtID.setEnabled(false);
            jcbMarca.setSelectedItem("");
            jcbModelo.setSelectedItem("");;
            jcbProrietario.setSelectedItem(" ");;
            
            rbBC.setSelected(false);
            rbEletrica.setSelected(false);
            rbRevisao.setSelected(false);
            rbMecanica.setSelected(false);
            
           
            jbNovo.setText("Novo");
            txtID.setText((currentID+1)+"");
            //jcbModelo.setText("");
            txtMatricula.setText("");
            rbBC.removeAll();
            rbEletrica.revalidate();
            rbRevisao.removeAll();
            rbMecanica.removeAll();
            txtAvaria.setText("");
            txtObservacao.setText("");
           // jcbModelo.removeAllItems();
           // jcbProrietario.removeAllItems();
           // jcbMarca.removeAllItems();
            
     }
     
    public void Activar(){
           txtID.setEnabled(false);
           txtID.setEditable(false);
         
            jcbMarca.setEnabled(true);
            jcbMarca.setModel(new DefaultComboBoxModel(marcas));
            LoadCombo();
            jcbModelo.setEnabled(true);
            jcbProrietario.setEnabled(true);
            //jcbProrietario.setEditable(true);
            txtMatricula.setEnabled(true);
            rbBC.setEnabled(true);
            rbEletrica.setEnabled(true);
            rbRevisao.setEnabled(true);
            rbMecanica.setEnabled(true);
            txtAvaria.setEnabled(true);
            txtObservacao.setEnabled(true);
     
    }
    
    public JifViatura() throws IOException{
       conexao();
 
        File arquivo = new File((new File("log.txt")).getCanonicalPath());
     this.setIconifiable(true);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
     JPanel painel = new JPanel(new MigLayout());
     JPanel radioPainel = new JPanel(new MigLayout());
   
     
        
        jlID = new JLabel("ID");
        txtID = new JTextField(5);
        
        //mascaraMatricula.setPlaceholderCharacter('_');
        
       txtMatricula.setToolTipText("Introduza a matricula");
        jlMarca = new JLabel("Marca");
        jlMatricula = new JLabel("Matricula");
        jlProprietario = new JLabel("Proprietario");
        jlAvaria = new JLabel("Avaria/servico");
         //private String[] avaria ={"Mecanica","Eletrica","Revisao","Bate-chapaPintura"};
        rbMecanica=new JCheckBox("Mecanica");
        rbEletrica =new JCheckBox("Eletrica");
        rbRevisao =new JCheckBox("Revisao");
        rbBC=new JCheckBox("Bate-chapa/Pintura");
        jcbMarca = new JComboBox();
        jcbMarca.setMinimumSize(new Dimension(450,10));
        jcbProrietario=new JComboBox();
        jcbProrietario.setMinimumSize(new Dimension(450,10));
        jScrollPane1 = new JScrollPane();
        txtAvaria = new JTextArea(20,10);
        jbNovo = new JButton("Novo");
        jbGravar = new JButton("Gravar");
        jbCancelar = new JButton("Cancelar");
        jbCancel = new JButton("Cancelar");
        jlObservacao = new JLabel("Observacao");
        jlSpace = new JLabel("     ");
        jScrollPane2 = new JScrollPane();
        jspPainelRolagem = new JScrollPane();
        txtObservacao = new JTextArea(25,10);
        //cboProrietario = new JComboBox(15);
        jlModelo = new JLabel("Modelo");
        jcbModelo = new JComboBox();
        jcbModelo.setMinimumSize(new Dimension(450,10));

        txtAvaria.setColumns(40);
        txtAvaria.setRows(4);
        txtObservacao.setColumns(40);
        txtObservacao.setRows(4);
        txtAvaria.setLineWrap(true);
        txtObservacao.setLineWrap(true);
        jScrollPane1.setViewportView(txtAvaria);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane2.setViewportView(txtObservacao);
        jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        
        //setClosable(true);
        setIconifiable(true);
        
        
        painel.add(jlID,"gapleft 10");
        painel.add(txtID,"gapleft 10,wrap");
        painel.add(jlProprietario,"gapleft 10");
        painel.add(jcbProrietario,"gapleft 10,wrap");
        painel.add(jlMarca,"gapleft 10");
        painel.add(jcbMarca,"gapleft 10,wrap");
        painel.add(jlModelo,"gapleft 10");
        painel.add(jcbModelo,"gapleft 10,wrap");
        painel.add(jlMatricula,"gapleft 10");
        painel.add(txtMatricula,"gapleft 10,wrap");
       
        painel.add(jlAvaria,"gapleft 10");
        painel.add(radioPainel,"wrap");
        
        painel.add(jlObservacao,"gapleft 10");
        painel.add(jScrollPane2,"wrap");
        
       buttonPanel = new JPanel(new MigLayout("fillx,insets 0"));
     buttonPanel.add(jbNovo, "split,right,width 120!");
     
     //buttonPanel.add(jbCancelar, "width 120!");
     radioPainel.add(rbEletrica);
     radioPainel.add(rbMecanica,"wrap");
     radioPainel.add(rbRevisao);
     radioPainel.add(rbBC,"wrap");
     
     painel.add(buttonPanel,"dock South");
     
     jcbMarca.setModel(new DefaultComboBoxModel(marcas));
        
     
    
    
        
     
        //////////////////////////////////////////////////////////////
        
if (novo==true){
    Limpar();
    jbCancel.setText("Cancelar");
   
    jbNovo.setIcon(new ImageIcon(getClass().getResource("/Icons/Create.png")));
     
    
     sair=false;
                  Activar();
                  conexao();
                       try{
            String query ="SELECT * FROM oficina";
            int id =1;String a;
            rs=st.executeQuery(query);
            if(rs.last()){
                id =id+Integer.parseInt(rs.getString("ID"));
                a=Integer.toString(id);
            txtID.setText(a);
            con.close();}
            else{
            a=Integer.toString(id);
            txtID.setText(a);}
            //dispose();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }
                  buttonPanel.remove(jbNovo);
                  buttonPanel.remove(jbCancel);
                  buttonPanel.add(jbGravar,"split,right,width 120!");
                  buttonPanel.add(jbCancel, "width 120!");
                 // jbCancel.setText("Sair"); 
                 jcbMarca.addActionListener(new ActionListener() {
                     
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                       LoadModels();
                    }
                });  
                 
                jcbProrietario.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        
                   if(jcbProrietario.getSelectedItem().equals("Novo"))
                   {
                     
                   JifNovoProprietario jifNCliente = new JifNovoProprietario();
                   jifNCliente.setVisible(true);
                   getParent().add(jifNCliente);
                   jifNCliente.setBounds(390,100,630,315);
                   jifNCliente.setTitle("Novo Cliente");
                    dispose(); 
                   //jcbProrietario.setModel(new DefaultComboBoxModel());
                   //LoadCombo();
                  
                   //dispose();
                   }
                    }
                });
                
                novo = false;
            
          
        
        jbGravar.setIcon(new ImageIcon(getClass().getResource("/Icons/Save.png")));
             jbGravar.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            
                  SetData();
               
                  
                  
                  if(viaturaAdicionada==true){
                  try{
                        if (!arquivo.exists()) {
                            arquivo.createNewFile();}
                                        
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("A viatura '"+nomeViatura+"' foi registrada ! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                   
                  
                  
           sair =true;
        }}
    });
                  
 
        jbCancel.setIcon(new ImageIcon(getClass().getResource("/Icons/Cancel.png")));
    
        jbCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
               int confirm = JOptionPane.showConfirmDialog(null, "Deseja mesmo cancelar?","Atencao",JOptionPane.YES_NO_OPTION);
               if(confirm == 0){
                  Limpar();
                    dispose();
                    txtID.setText(currentID+"");  
               }
                  
           
            }
        });
      
    }
        
     this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     this.setPreferredSize(new Dimension(600,420));
     this.pack();
     this.setVisible(true);
     this.add(painel);
        
        
        
        
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
      
    
    
    
     public void Adicionar(){
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
                return;
            }
            try{
            st.setInt(1, id);
            st.executeUpdate();
            }catch (MySQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(null,e+"Ja existe um cliente com este contacto(numero de telefone).\n Introduza um novo numero.");
        }
            
            
            JOptionPane.showMessageDialog(null,"Cliente adicionado com sucesso!","Informacao",JOptionPane.INFORMATION_MESSAGE);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Verifique se todos campos foram devidamente preenchidos \n e/ou se os tipos de dado introduzidos sao validos!","Erro",JOptionPane.ERROR_MESSAGE);
        }       catch (SQLException ex) {
                    Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                }
      
          
   }
  
    
    public void JifNovoProprietario() throws HeadlessException {

        setTitle("Menu Clientes");
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
                 if(txtNome.getText().equals("") && txtMorada.getText().equals("") && txtBI.getText().equals("") && txtContacto.getText().equals(""))
                JOptionPane.showMessageDialog(null,"Nenhum campo deve estar vazio","Atencao",JOptionPane.WARNING_MESSAGE);
                 else{
                  Adicionar();
                 
                JifViatura nova = null;
                     try {
                         nova = new JifViatura();
                     } catch (IOException ex) {
                         Logger.getLogger(JifViatura.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 Activar();
                  conexao();
      nova.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     nova.setPreferredSize(new Dimension(630,315));
     nova.pack();
     nova.setVisible(true);
    dispose();
              
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
   
   
    
    
    
    
    
  
