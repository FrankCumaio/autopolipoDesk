/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autopolipo1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//import static jdk.nashorn.internal.codegen.Compiler.LOG;
import net.miginfocom.swing.MigLayout;

public class JifOficina extends JInternalFrame {

    private final JTable table;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private boolean editada = false;
    private int nRow,nCol,cod;
    public boolean action = false, alteracao = false;
    private Object idd;
    public String a;
    private ArrayList<String> proprietarios = new ArrayList<String>();
    private AtfProcurar txtPesquisar = new AtfProcurar(proprietarios);
    private Object[][] tableData;
    private Connection con;
    private Statement st;
    private ResultSet rs;
    
    
     public void conexao(){
         
    try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados");
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
    
    
    
    
    public void LoadData(){
        
        txtPesquisar.setText("");
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM `oficina` ORDER BY `ID` ASC");
            ResultSetMetaData metaData = rs.getMetaData();

         
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
        
                columnNames.add("ID");
                columnNames.add("Marca");
                columnNames.add("Modelo");
                columnNames.add("Matricula");
                columnNames.add("Proprietario");
                columnNames.add("Avaria");
                columnNames.add("Observacao");
            
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
    
    public void AutoCompleteTextField() {
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("select * from oficina");
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next())
            {
                proprietarios.add(rs.getString("proprietario"));
            }
            
        } catch (Exception e) {
        }
        
        
        txtPesquisar.setText("");
        txtPesquisar.setToolTipText("Pesquisa pelo nome do proprietario");
        txtPesquisar.setMinimumSize(new Dimension(200,10));
        }
 
    /*
    public String Alteracao(){
        String alteracao = null;

         try{
            String query ="SELECT * FROM oficina WHERE `ID`='"+table.getValueAt(table.getSelectedRow(), 0)+"'";
            rs=st.executeQuery(query);
            while(rs.next()){
                
              alteracao = rs.getString("alteracao");
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null,"oooo"+ es);
             }
         return alteracao;
     } */
     
    
    
    public JifOficina() throws HeadlessException, IOException{
        
         File arquivo = new File((new File("log.txt")).getCanonicalPath());
        AutoCompleteTextField();
        
        this.setLayout(new MigLayout());
        JPanel Painel = new JPanel();
        JPanel painelPesq = new JPanel(new MigLayout());
        JPanel buttonPainel = new JPanel(new MigLayout());
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1300,450));

        
        JLabel jlPesq = new JLabel();
        jlPesq.setIcon(new ImageIcon(getClass().getResource("/Icons/Search.png")));
        painelPesq.add(jlPesq);
        painelPesq.add(txtPesquisar);
        JButton jbEditar = new JButton("Editar dados");
        JButton jbRemover = new JButton("Remover");
        jbRemover.setIcon(new ImageIcon(getClass().getResource("/Icons/Remove.png")));
        jbEditar.setIcon(new ImageIcon(getClass().getResource("/Icons/Update.png"))); 
        JButton jbConfirmar = new JButton("Reparacao Concluida");
        jbConfirmar.setIcon(new ImageIcon(getClass().getResource("/Icons/OK.png"))); 
        buttonPainel.add(jbConfirmar);
        buttonPainel.add(jbEditar);
        buttonPainel.add(jbRemover);
        Painel.add(new JScrollPane(table));
       
        LoadData();

         this.setVisible(true);
         this.setTitle("Oficina");
        Painel.setVisible(true);
        this.add(painelPesq,"wrap");
        this.add(Painel,"wrap");
        this.add(buttonPainel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
     this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     this.pack();
     setClosable(true);
     setIconifiable(true);
     
     jbRemover.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 conexao();
                 if(action == true){
                 int confirm = JOptionPane.showConfirmDialog(null,"Deseja mesmo remover?","Remover Viatura",JOptionPane.YES_NO_OPTION);
                 if(confirm == 0){ 
                 try {
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("DELETE FROM `oficina` WHERE `ID` = '"+idd+"'");
                              st.executeUpdate();
                             LoadData();
                              
        } catch (Exception es) {
            JOptionPane.showMessageDialog(null,es);
        }
             }
             }else{ 
                     JOptionPane.showMessageDialog(null,"Seleccione a viatura a remover","Atencao",JOptionPane.WARNING_MESSAGE);
                 }
             }
         });
     
     jbConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(action==false)
                    JOptionPane.showMessageDialog(null,"Selecione uma viatura para a confirmacao da reparacao","Atencao",JOptionPane.WARNING_MESSAGE);
                else{
              try {
                  action=false;
                 Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("UPDATE `oficina` SET `alteracao`='confirmar' WHERE ID='"+table.getValueAt(table.getSelectedRow(), 0)+"'");
                              st.executeUpdate();  
         JifConfirmarRep jifViatura = new JifConfirmarRep();
         
         jifViatura.setBounds(370, 20, 630, 580);
         jifViatura.setMinimumSize(new Dimension(630,580));
        jifViatura.setVisible(true);
       getParent().add(jifViatura);
       jifViatura.setTitle("Confirmar reparacao");
                    
        dispose();
    
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Exception in Load Data", e);
        }}
            }
        });
            
     
     
     
     
            
            editada = new JifViatura().Editada();
          jbEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
             
                if(action==false)
                    JOptionPane.showMessageDialog(null,"Selecione a viatura a editar","Atencao",JOptionPane.WARNING_MESSAGE);
                else{
              try {
                  action=false;
                  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            PreparedStatement st =(PreparedStatement) conn.prepareStatement("UPDATE `oficina` SET `alteracao`='editar' WHERE ID='"+table.getValueAt(table.getSelectedRow(), 0)+"'");
                              st.executeUpdate();   
                  JifViatura jifViatura = new JifViatura();
        jifViatura.setVisible(true);
        jifViatura.setBounds(370, 50, 630, 430);
        getParent().add(jifViatura);
        jifViatura.setTitle("Editar");
                
        try{
            
            String editar ="SELECT * FROM oficina WHERE `alteracao`='editar'";
            rs=st.executeQuery(editar);
            if (rs.first()){
                jifViatura.EditarData();
                jifViatura.jbNovo.setIcon(new ImageIcon(getClass().getResource("/Icons/Update.png")));
                jifViatura.jbNovo.setText("Actualizar");
                
                jifViatura.jbCancelar.setIcon(new ImageIcon(getClass().getResource("/Icons/Cancel.png")));
                jifViatura.buttonPanel.remove(jifViatura.jbGravar);
                 jifViatura.buttonPanel.remove(jifViatura.jbCancel);
                  jifViatura.buttonPanel.add(jifViatura.jbNovo,"split,right,width 120!");
                  jifViatura.buttonPanel.add(jifViatura.jbCancelar, "width 120!");
                
                
                jifViatura.jbNovo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                jifViatura.UpdateData();
                jifViatura.ClearAlteracao();
                jifViatura.dispose();
            }
        });
                
                
                jifViatura.jbCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Deseja cancelar a operação", "Atenção", dialogButton);
                if(dialogResult == 0) {
                          jifViatura.ClearAlteracao();
                              JifOficina jifOficina = null;
                    try {
                        jifOficina = new JifOficina();
                    } catch (HeadlessException ex) {
                        Logger.getLogger(JifOficina.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(JifOficina.class.getName()).log(Level.SEVERE, null, ex);
                    }
                                jifOficina.setVisible(true);
                                jifViatura.getParent().add(jifOficina);
                                try {     
        jifOficina.setSelected(true);     
        //diz que a janela interna é maximizável     
        jifOficina.setMaximizable(false);     
        //set o tamanho máximo dela, que depende da janela pai     
        jifOficina.setMaximum(true);     
    } catch (java.beans.PropertyVetoException e) {}
                                jifOficina.setTitle("Viaturas em reparacao");
                              jifViatura.dispose();
       
                    }
            }
        });
                
             
            }
        } catch(Exception b){}  
        
        
          //  if(editada == true){
            try{
                        if (!arquivo.exists()) {
                            arquivo.createNewFile();}
                                        
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("Viatura Editada! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
          //  }
            
        
        dispose();
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Exception in Load Data", e);
        }}
            }
        });
     
     
       table.addMouseListener(new MouseListener() {  
             
            public void mouseClicked(MouseEvent me) {
                nRow = table.getSelectedRow();
                nCol = table.getSelectedColumn();
                idd= table.getValueAt(nRow,0);
                action=true;
            }

            @Override
            public void mousePressed(MouseEvent me) {
                nRow = table.getSelectedRow();
                nCol = table.getSelectedColumn();
                idd= table.getValueAt(nRow,0);
                 action=true;
            }

            @Override
            public void mouseReleased(MouseEvent me) {
      
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                
                 
            }

            @Override
            public void mouseExited(MouseEvent me) {
               
            }
        });
       txtPesquisar.addMouseListener(new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent me) {
            txtPesquisar.setEnabled(true);
        }

        @Override
        public void mousePressed(MouseEvent me) {
           txtPesquisar.setEnabled(true);
        }

        @Override
        public void mouseReleased(MouseEvent me) {
           
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            
        }

        @Override
        public void mouseExited(MouseEvent me) {
        
        }
    });
       
       txtPesquisar.addKeyListener(new KeyListener() {

            @Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
                    String pressed = "Enter";
                    if (KeyEvent.getKeyText(e.getKeyCode()).equals(pressed)){
                    
                            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("select * from oficina WHERE proprietario = '"+txtPesquisar.getText()+"'");
            ResultSetMetaData metaData = rs.getMetaData();

      
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
         
                columnNames.add("ID");
                columnNames.add("Marca");
                columnNames.add("Modelo");
                columnNames.add("Matricula");
                columnNames.add("Proprietario");
                columnNames.add("Avaria");
                columnNames.add("Observacao");
            
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getObject(i));
                }
                data.add(vector);
            }

            tableModel.setDataVector(data, columnNames);
        } catch (Exception es) {
//            LOG.log(Level.SEVERE, "Exception in Load Data", es);
        }
		}else{
                      if (KeyEvent.getKeyText(e.getKeyCode()).equals("Backspace"))
                          LoadData();
                    }
                        }

		@Override
		public void keyReleased(KeyEvent e) {
					}
        });
        }
}

