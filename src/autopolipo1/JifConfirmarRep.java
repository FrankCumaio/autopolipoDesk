/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autopolipo1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class JifConfirmarRep extends JInternalFrame{
    
    private JLabel jlID,jlSpace,jlMarca,jlMatricula,jlProprietario,jlAcessorio,jlQuant,jlCar,jlTotal,jlAvaria,jlObservacao,jlModelo;
    private JComboBox jcbAcessorios;
    private JScrollPane jScrollPane1,jspPainelRolagem,jScrollPane2;
    private JButton jbCancelar,jbGuardar,jbAddCar,jbRemoCar;
    private JTextArea txtAvaria,txtObservacao;
    private JTextField txtID,txtModelo,txtMatricula,txtProprietario, txtMarca,txtTotal,txtQuant,txtContacto;
    private JTable table;
    private JPanel painelTable;
    private ResultSetMetaData metaData;
    private int rows,quantidade;
    private String tipo = "";
    private int valorTotal=0;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private String cont;
     private ArrayList acessorios = new ArrayList();
    ClassLoader loader = getClass().getClassLoader();
    
   public Connection conexao(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
          st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados","Erro",JOptionPane.ERROR_MESSAGE);
        }return con;
    }
    
   {
    try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados");
        }
    }
   
 
    
    public void setContacto(){
        conexao();
        
    }
    public void LoadData(){
             conexao();
             setContacto();
           try{
            String query ="SELECT * FROM oficina WHERE `alteracao`='confirmar'";
            rs=st.executeQuery(query);
            while(rs.next()){
            txtID.setText(rs.getString("ID"));
            txtProprietario.setText(rs.getString("proprietario"));
            txtMarca.setText(rs.getString("marca"));
            txtModelo.setText(rs.getString("modelo"));
            txtMatricula.setText(rs.getString("matricula"));}
            
            txtID.setEditable(false);
            txtProprietario.setEditable(false);;
            txtMarca.setEditable(false);
            txtModelo.setEditable(false);
            txtMatricula.setEditable(false);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }
           try{
            String query ="SELECT * FROM `clientes` WHERE `nome` LIKE '"+txtProprietario.getText()+"'";
            rs=st.executeQuery(query);
            if(rs.first()){
            cont = (rs.getString("contacto"));
           
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }
           
           try {
            String query ="SELECT * FROM `acessorios` WHERE `modelo`='"+txtModelo.getText()+"'";
            rs=st.executeQuery(query);
            while(rs.next()){
                acessorios.add(rs.getString("tipo"));
           }
             jcbAcessorios.setModel(new DefaultComboBoxModel(acessorios.toArray()));
        } catch (Exception e) {
        }
           
            try {
            String query ="SELECT * FROM `acessorios` WHERE `modelo`='universal'";
            rs=st.executeQuery(query);
            while(rs.next()){
                acessorios.add(rs.getString("tipo"));
           }
             jcbAcessorios.setModel(new DefaultComboBoxModel(acessorios.toArray()));
        } catch (Exception e) {
        }
           
                    
        }
    
    public void loadtable() throws SQLException{
         ResultSet rs = null;
        try {
            rs = st.executeQuery("select * from carrinho");
            metaData = rs.getMetaData();
        } catch (SQLException ex) {
            Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
        }
            ResultSetMetaData metaData = null;
        try {
            metaData = rs.getMetaData();
        } catch (SQLException ex) {
            Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
        }

         // Names of columns
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
                columnNames.add("Descricao");
                columnNames.add("Preco Unit.");
                columnNames.add("Quant.");
                columnNames.add("Valor a pagar");
               
                
                 Vector<Vector<Object>> data = new Vector<Vector<Object>>();
               while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getObject(i));
                }
                data.add(vector);
                
            }
                /*Vector<Object> vector = new Vector<Object>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add("");
                }
                data.add(vector);*/
                tableModel.setDataVector(data, columnNames);
               
    
    }
    
     public JRResultSetDataSource Relatorio(String SQL){
        ResultSet rs = null;
        JRResultSetDataSource rsResultado = null;
        try {
            PreparedStatement stm =conexao().prepareStatement(SQL);
            rs = stm.executeQuery();
            rsResultado = new JRResultSetDataSource(rs);
        } catch (Exception e) {
        }return rsResultado;
    }
     
     
     public void delAux(){
          try {
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("DELETE FROM `aux`");
                              st.executeUpdate();
                              dispose();
                              
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
        }
     }
     
     public void MaoObra(){
         
          conexao();
            String tipo="Mao de obra";
            int quant=1;
            int preco= 2000;
            int pagar=2000;
            try{
            PreparedStatement st =(PreparedStatement) con.prepareStatement("INSERT INTO carrinho (acessorio, preco, quant, pagar) VALUES (?, ?, ?, ?)");
            st.setString(1, tipo);
            st.setInt(2, preco);
            st.setInt(3, quant);
            st.setInt(4, pagar);
            st.executeUpdate();
              
                   
        }catch(Exception es){
            JOptionPane.showMessageDialog(null, "Erro hey"+es);
        }
     }
    
    public void setData(){
        conexao();
        
        
                       String nome,matricula,modelo,marca,acessorios="", observacao ;
        int preco,id=1;
       try{ 
                     try{
            String query ="SELECT * FROM arquivo";
            
            rs=st.executeQuery(query);
            if(rs.last()){
                id =id+Integer.parseInt(rs.getString("ID"));;}
            else{
            
            }
            //dispose();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }
          
            nome=txtProprietario.getText();
            marca=txtMarca.getText();
            modelo= txtModelo.getText();
            matricula=txtMatricula.getText();
            for (int i=0;i<table.getRowCount();i++){
               acessorios = acessorios+","+table.getValueAt(i, 0); 
            }
            preco=Integer.parseInt(txtTotal.getText());
            observacao = txtObservacao.getText();
            PreparedStatement st =(PreparedStatement) con.prepareStatement("INSERT INTO arquivo (ID, marca, modelo, matricula, proprietario,preco, observacao) VALUES (?, ?, ?, ?, ?, ?, ?)");
            st.setInt(1, id);
            st.setString(2, marca);
            st.setString(3, modelo);
            st.setString(4, matricula);
            st.setString(5, nome);
            st.setInt(6, preco);
            st.setString(7, observacao);
            st.executeUpdate();
             Factura fs = new Factura();
                fs.createDocument(closable);
                delAux();
             //int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Reparacao da viatura confirmada. Por favor Contacte o Proprietario pelo "+cont+"\n Deseja imprimir a factura? ", "Atenção",JOptionPane.YES_NO_OPTION);
                if(dialogResult == 0) {
             File pdf = new File(new File("Factura.pdf").getCanonicalPath());
                    try {
                        Desktop.getDesktop().open(pdf);
                    } catch (Exception e) {
                    }
        delData();
        delCar();
        dispose();
                     
                    
                } else {
                    delData();
                    delCar();
                    dispose();
                    }

           // JOptionPane.showMessageDialog(null,");
            //
      //  con.close(); 
                   
        }catch(Exception es){
            JOptionPane.showMessageDialog(null, "Erro hey"+es);
        }
                              
                        
                                 
    }
    
     public String getProprietario(){
      return txtProprietario.getText();
  }  
  
  public String getMatricula(){
      return txtMatricula.getText();
  }
  
  public int getTotal(){
      return Integer.parseInt(txtTotal.getText());
  }
    
    public void delData(){
        conexao();
        try {
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("DELETE FROM `oficina` WHERE `ID` = '"+txtID.getText()+"'");
                              st.executeUpdate();
                              JifOficina JifOficina = new JifOficina();
        JifOficina.setVisible(true);
        getParent().add(JifOficina);
        try {     
        JifOficina.setSelected(true);         
        JifOficina.setMaximizable(false);        
        JifOficina.setMaximum(true);     
    } catch (java.beans.PropertyVetoException e) {}
        JifOficina.setTitle("Viaturas em reparacao");
                              dispose();
                              
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
        }
        
    }
    
    public void delCar(){
        try {
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("DELETE FROM `carrinho`");
                              st.executeUpdate();
                              dispose();
                              
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    
     public int Remover() throws SQLException{
        
        int result = 0;
         int rowSelected = table.getSelectedRow();
                if(rowSelected == -1){
                    JOptionPane.showMessageDialog(null,"Seleccione o acessorio a ser removido","Atencao",JOptionPane.WARNING_MESSAGE);
                
                    return result;
                }else{
                    int confirm = JOptionPane.showConfirmDialog(null,"Deseja mesmo remover?","Atencao",JOptionPane.YES_NO_OPTION);
                    if(confirm ==0){
                        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
          st=con.createStatement();
        }catch(Exception ed){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados","Erro",JOptionPane.ERROR_MESSAGE);
        }
           
             rs = st.executeQuery("SELECT * FROM `carrinho` WHERE `acessorio` LIKE '"+table.getValueAt(rowSelected, 0)+"'");
             
            if(rs.first()){
                txtQuant.setText(""+(Integer.parseInt(txtQuant.getText()) - rs.getInt("pagar")));
            }           
                        
          String lastNome = null;
           
          int getQuant,gtId;
         tipo = (String) table.getValueAt(table.getSelectedRow(), 0);
         quantidade =  (int) table.getValueAt(table.getSelectedRow(), 2);    
             PreparedStatement st = null;
                        try {
                            st = con.prepareStatement("DELETE FROM autopolipo.carrinho WHERE carrinho.acessorio LIKE ?");
                            st.setString(1, tipo);
                            result = st.executeUpdate();
                          //  JOptionPane.showMessageDialog(null, "Acessorio removido com sucesso!","Informacao",JOptionPane.INFORMATION_MESSAGE);
                           
                           
                           
                        } catch (SQLException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        
                
                     
                        loadtable();
                        table.setModel(tableModel);
                       table.setPreferredScrollableViewportSize(new Dimension(420, 110));
                       
                     
       
        
    }
                }
                 return result;
     }
    
    
    
    
    public JifConfirmarRep() throws SQLException{
       //LoadData();
        
        delCar();
        MaoObra();
    
     JPanel painel = new JPanel(new MigLayout());
     painelTable = new JPanel(new MigLayout());
        
     jlID = new JLabel("ID");
        txtID = new JTextField(5);
        txtMatricula = new JTextField();
        txtTotal = new JTextField(5);
        txtQuant = new JTextField(5);
        jlMarca = new JLabel("Marca");
        txtMarca=new JTextField(20);
        jlMatricula = new JLabel("Matricula");
        jlProprietario = new JLabel("Proprietario");
        jlAvaria = new JLabel("Trabalho realizado");
        jlAcessorio = new JLabel("Acessorio");
        jlQuant = new JLabel("Quant.");
        jlCar=new JLabel("Carrinho");
        jlTotal = new JLabel("Total");
        jScrollPane1 = new JScrollPane();
        txtAvaria = new JTextArea(25,10);
        jbGuardar = new JButton("Confirmar");
        jbCancelar = new JButton("Cancelar");
        jlObservacao = new JLabel("Observacao");
        jbAddCar = new JButton("");
        jbRemoCar = new JButton();
         jbAddCar.setIcon(new ImageIcon(loader.getResource("Icons/cart.png")));
         jbRemoCar.setIcon(new ImageIcon(loader.getResource("Icons/cartRemove.png")));
        jlSpace = new JLabel("     ");
        jScrollPane2 = new JScrollPane();
        jspPainelRolagem = new JScrollPane();
        txtObservacao = new JTextArea(25,10);
        txtProprietario = new JTextField();
        jlModelo = new JLabel("Modelo");
        txtModelo = new JTextField(20);
      
        
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
         
       table = new JTable(tableModel);
       table.setPreferredScrollableViewportSize(new Dimension(420, 110));
        JScrollPane sp = new JScrollPane(table);
        jcbAcessorios = new JComboBox();
        txtTotal.setEditable(false);
        txtTotal.setText("2000");
       
        
        jbCancelar.setIcon(new ImageIcon(loader.getResource("Icons/Cancel.png")));
        
        jbCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Deseja cancelar a operação", "Atenção", dialogButton);
                if(dialogResult == 0) {
                    
                           try {
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("UPDATE `oficina` SET `alteracao`='' WHERE ID='"+txtID.getText()+"'");
                              st.executeUpdate();
        JifOficina JifOficina = new JifOficina();
        JifOficina.setVisible(true);
        getParent().add(JifOficina);
        try {     
        JifOficina.setSelected(true);         
        JifOficina.setMaximizable(false);        
        JifOficina.setMaximum(true);     
    } catch (java.beans.PropertyVetoException e) {}
        JifOficina.setTitle("Viaturas em reparacao");
        delCar();
                                 dispose();
        } catch (Exception e) {
        } 
                    } else {
                    System.out.println("No Option");
                    }
                
           
            }
        });
      setClosable(true);
    setIconifiable(true);
        
        
        painel.add(jlID,"gapleft 10");
        painel.add(txtID,"wrap");
        painel.add(jlMarca,"gapleft 10");
        painel.add(txtMarca);
        painel.add(jlModelo,"gap unrelated");
        painel.add(txtModelo,"gapleft 10,wrap");
        painel.add(jlMatricula,"gapleft 10");
        painel.add(txtMatricula,"span,grow,wrap");
        painel.add(jlProprietario,"gapleft 10");
        painel.add(txtProprietario,"span,grow,wrap");
        /*painel.add(jlAvaria,"gapleft 10");
        painel.add(jScrollPane1,"span,grow,wrap");*/
        
        painel.add(jlObservacao,"gapleft 10");
        painel.add(jScrollPane2,"span,grow,wrap");
       // add(painelTable,"gapleft 10,wrap");
        JPanel buttonPanel = new JPanel(new MigLayout("fillx,insets 0"));

     
       buttonPanel.add(jlSpace,"wrap");   
     buttonPanel.add(jbGuardar, "split,right,width 120!");
     buttonPanel.add(jbCancelar, "width 120!");
      
       painelTable.add(jlAcessorio);
       painelTable.add(jcbAcessorios,"gapleft 25");
       painelTable.add(jlQuant);
       painelTable.add(txtQuant);
       painelTable.add(jbAddCar,"gap unrelated");
       painelTable.add(jbRemoCar,"gap unrelated, wrap");
       painelTable.add(jlCar);
       painelTable.add(sp,"span,grow,wrap,gapleft 25");
       painelTable.add(jlTotal);
       painelTable.add(txtTotal,"gapleft 25, wrap");
       
      // painelTable.add(buttonPanel,"gap unrelated");
       painelTable.setBorder(BorderFactory.createTitledBorder("Acessorios Usados"));
       painelTable.setBounds(15,270,445,180);
     painel.add(buttonPanel,"dock South");
     painel.add(painelTable,"gapleft 10, dock South");
     
      LoadData();
      loadtable();
      //MaoObra();
      
      jbGuardar.setIcon(new ImageIcon(loader.getResource("Icons/OK.png")));
        jbGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                
                
               // delData();
                setData();
                
            }
        });
        
        jbAddCar.addActionListener(new ActionListener(){

          @Override
          public void actionPerformed(ActionEvent ae) {
              
                  conexao();
                  ResultSet rs = null, rs1 = null;
                  Object valueAt;
                  String tipo,TP;
                   if (table.getRowCount()==1){
       String acessorioc =(String) table.getValueAt(0, 0);
       int precoc=(int) table.getValueAt(0, 1);
       int quantc=(int) table.getValueAt(0, 2);
       int pagarc=(int) table.getValueAt(0, 3);
 try{
                          PreparedStatement st =(PreparedStatement) con.prepareStatement("INSERT INTO aux (acessorio,preco,quant,pagar) VALUES (?,?,?,?)");
                          
                          st.setString(1,acessorioc);
                          st.setInt(2,precoc);
                          st.setInt(3,quantc);
                          st.setInt(4, pagarc);
                          st.executeUpdate();
                      } catch (SQLException ex) {
                          Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
                      }}

                  int getQuant = 0,gtQunt,quant = 0,upQuant,upPagar,nPagar,tPagar = 0,nQuant = 0,columnCount=0,preco=0,pagar=0,total;
                  tipo=jcbAcessorios.getSelectedItem().toString();
                  
                  valorTotal = Integer.parseInt(txtTotal.getText());
                  
                  
                  
                  try {
                      rs = st.executeQuery("SELECT * FROM `carrinho` WHERE `acessorio` = '"+tipo+"'");
                      
                      if (rs.first()) {
                          try{
                              upQuant=rs.getInt("quant");
                              upPagar=rs.getInt("preco");
                              nPagar=upPagar*Integer.parseInt(txtQuant.getText());
                              tPagar = (valorTotal+nPagar)-2000;
                              valueAt = table.getValueAt(table.getRowCount()-1,3);
                              total= (valorTotal+Integer.parseInt(valueAt.toString()));
                              TP=""+total;
                              txtTotal.setText(TP);
                              valorTotal = Integer.parseInt(txtTotal.getText());
                              quant=upQuant+Integer.parseInt(txtQuant.getText());
                            
                          }catch(NumberFormatException e){ 
                              JOptionPane.showMessageDialog(null,"Verifique o tipo de dado introduzido!","Erro",JOptionPane.ERROR_MESSAGE);
                              return;
                          }
                          
                         
                          
                      rs = st.executeQuery("SELECT * FROM `acessorios` WHERE `tipo` = '"+tipo+"'");    
                    while(rs.next()){
                      getQuant = rs.getInt("quant");
                      gtQunt = getQuant;
                      if(getQuant<=0){
                           JOptionPane.showMessageDialog(null,"Nao esta disponivel nenhum item deste acessorio!","Atencao",JOptionPane.WARNING_MESSAGE);
                           return;
                      }else{
                      
                              if(Integer.parseInt(txtQuant.getText())>gtQunt){
                              JOptionPane.showMessageDialog(null,"A quantidade desejada nao esta disponivel!","Atencao",JOptionPane.WARNING_MESSAGE);
                              return;
                          }else{
                              if((getQuant-quant) < 10){
                                  JOptionPane.showMessageDialog(null,"A quantidade de "+tipo+" no stock é inferior a 10","Atencao",JOptionPane.WARNING_MESSAGE);
                              }
                              }
                  }
                      PreparedStatement st =(PreparedStatement) con.prepareStatement("UPDATE `acessorios` SET `quant`='"+(getQuant-quant)+"' WHERE tipo LIKE '"+tipo+"'");
                              st.executeUpdate();
                    }             
                        
                          
                          
                          
                          try {
                              Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                              PreparedStatement st =(PreparedStatement) conn.prepareStatement("UPDATE `carrinho` SET `quant`='"+quant+"',`pagar`='"+tPagar+"' WHERE acessorio='"+tipo+"'");
                              st.executeUpdate();
                              loadtable();
                          } catch (Exception e) {
                          }
                          
                          
                      }else{
                          
                          try {
                              rs = st.executeQuery("SELECT * FROM `acessorios` WHERE `tipo` = '"+tipo+"'");
                              
                              while(rs.next()){
                                  preco = preco+rs.getInt("preco");
                              }
                              
                              if (rs.first()) {
                                  try{
                                      preco=rs.getInt("preco");
                                      quant=Integer.parseInt(txtQuant.getText());
                                      pagar=valorTotal+(quant*preco)-2000;
                                      
                                      if(table.getRowCount()>0){
                                          valueAt = table.getValueAt(table.getRowCount()-1,3);
                                          total=(pagar+Integer.parseInt(valueAt.toString())+valorTotal);}
                                      else
                                          total=pagar+valorTotal;
                                      JOptionPane.showMessageDialog(null,table.getRowCount()-1);
                                      
                                      TP=""+total;
                                      txtTotal.setText(TP);
                                      valorTotal = Integer.parseInt(txtTotal.getText());
                                  }catch(NumberFormatException e){
                                      JOptionPane.showMessageDialog(null,"Verifique o tipo de dado introduzido!","Erro",JOptionPane.ERROR_MESSAGE);
                                      return;
                                  }
                                  
                                  rs = st.executeQuery("SELECT * FROM `acessorios` WHERE `tipo` = '"+tipo+"'");
                  while(rs.next()){
                      getQuant = rs.getInt("quant");
                      gtQunt = getQuant;
                      if(getQuant<=0){
                           JOptionPane.showMessageDialog(null,"Nao esta disponivel nenhum item deste acessorio!","Atencao",JOptionPane.WARNING_MESSAGE);
                           return;
                      }else{
                              if(Integer.parseInt(txtQuant.getText())>gtQunt){
                              JOptionPane.showMessageDialog(null,"A quantidade desejada nao esta disponivel!","Atencao",JOptionPane.WARNING_MESSAGE);
                              return;
                              
                          }
                              else{
                              if((getQuant-quant) < 10){
                                  JOptionPane.showMessageDialog(null,"A quantidade de "+tipo+" no stock é inferior a 10","Atencao",JOptionPane.WARNING_MESSAGE);
                              }
                              }
                  }
                      PreparedStatement st =(PreparedStatement) con.prepareStatement("UPDATE `acessorios` SET `quant`='"+(getQuant-quant)+"' WHERE tipo LIKE '"+tipo+"'");
                                st.executeUpdate();
                  }
                                  
                                  
                                  try{
                                      PreparedStatement st =(PreparedStatement) con.prepareStatement("INSERT INTO carrinho (acessorio, preco, quant, pagar) VALUES (?,?,?,?)");
                                      
                                      st.setString(1,tipo);
                                      st.setInt(2,preco);
                                      st.setInt(3,quant);
                                      st.setInt(4, pagar);
                                      
                                      st.executeUpdate();
                                      loadtable();
                                  } catch (SQLException ex) {
                                      Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
                                  }
                              }
                          } catch (Exception e) {
                          }
                          
                      }
                  } catch (Exception e) {
                  }
                  if(table.getRowCount()>0){
        String acessorioc =(String) table.getValueAt(table.getRowCount()-1, 0);
        int precoc=(int) table.getValueAt(table.getRowCount()-1, 1);
        int quantc=(int) table.getValueAt(table.getRowCount()-1, 2);
        int pagarc=(int) table.getValueAt(table.getRowCount()-1, 3);
          try{
                          PreparedStatement st =(PreparedStatement) con.prepareStatement("INSERT INTO aux (acessorio,preco,quant,pagar) VALUES (?,?,?,?)");
                          
                          st.setString(1,acessorioc);
                          st.setInt(2,precoc);
                          st.setInt(3,quantc);
                          st.setInt(4,pagarc);
                                                  
                          st.executeUpdate();
                          loadtable();
                      } catch (SQLException ex) {
                          Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
                      }}

                  txtQuant.setText("");
            
                  }   
         
      });
        
        jbRemoCar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int getQuant,gtId;
              
                    try {
                        Remover();
                    } catch (SQLException ex) {
                        Logger.getLogger(JifConfirmarRep.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    try{
                    rs = st.executeQuery("SELECT * FROM `acessorios` WHERE `tipo` LIKE '"+tipo+"'");
                    while(rs.next()){
                        getQuant = rs.getInt("quant");
                        gtId = rs.getInt("ID");
                 PreparedStatement st =(PreparedStatement) con.prepareStatement("UPDATE `acessorios` SET `quant` = ? WHERE `tipo` LIKE '"+tipo+"'");  
                        st.setInt(1,quantidade+getQuant); 
                        st.executeUpdate();
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(JifConfirmarRep.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        
     this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     //this.setPreferredSize(new Dimension(820,420));
    // this.pack();
     this.setVisible(true);
     this.add(painel);
     this.setMinimumSize(new Dimension(630,580));
     this.setResizable(true);
    // this.add(painelTable);
        
       
        
        
    }
}
