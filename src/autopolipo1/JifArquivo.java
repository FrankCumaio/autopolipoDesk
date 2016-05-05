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
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//import static jdk.nashorn.internal.codegen.Compiler.LOG;
import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class JifArquivo extends JInternalFrame {
    

    //private final JButton button;
    JTable table;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    //private JTextField txtPesquisar = new JTextField(20);
    private int nRow,nCol,cod;
    private Object idd;
    private JPanel Painel = new JPanel();
    JButton jbImprimir,jbRemover;
    private boolean action=false, removido = false;
    public String a, arquivoRemovido,arquivoImprimido;
    private JScrollPane sp = new JScrollPane();
    private JLabel tipoUser = new JLabel();
    private ArrayList<String> proprietarios = new ArrayList<String>();
    private AtfProcurar txtPesquisar = new AtfProcurar(proprietarios);
    private Object[][] tableData;
    private JPanel buttonPainel;
     private Connection con;
    private Statement st;
    private ResultSet rs;
    
    public Connection conexao(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
          st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados","Erro",JOptionPane.ERROR_MESSAGE);
        }return con;
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
    
    public void LoadUser(){
        try{
            String query ="SELECT * FROM usuario WHERE `activo`=1";
            rs=st.executeQuery(query);
            while(rs.next()){
           
            tipoUser.setText(rs.getString("Tipo"));
            }
             }catch(Exception es){
            JOptionPane.showMessageDialog(null, "Erro"+es);
        }
           
    }
    
    
    
    
    
    public int Remover(){
         int result = 0;
                if(table.getSelectedRow() == -1){
                    JOptionPane.showMessageDialog(null,"Seleccione o arquivo a ser removido","Atencao!",JOptionPane.WARNING_MESSAGE);
                    removido = false;
                  
                }else{
                    int confirm = JOptionPane.showConfirmDialog(null,"Deseja mesmo remover?","Remover Arquivo",JOptionPane.YES_NO_OPTION);
                    if(confirm ==0){
                        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
          st=con.createStatement();
        }catch(Exception ed){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados","Erro",JOptionPane.ERROR_MESSAGE);
          
        }
                        
              int contacto;
             int id = 0;
              id =(int)table.getValueAt(table.getSelectedRow(), 0);
               
             PreparedStatement st = null;
                        try {
                            st = con.prepareStatement("DELETE FROM autopolipo.arquivo WHERE arquivo.ID = ?");
                            st.setInt(1, id);
                            result = st.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Arquivo removido com sucesso!","Remover Arquivo",JOptionPane.INFORMATION_MESSAGE);
                            removido = true;
                            arquivoRemovido = (""+table.getValueAt(table.getSelectedRow(), 0).toString()+",correspondente a viatura: " + table.getValueAt(table.getSelectedRow(),1).toString()+", modelo: "+table.getValueAt(table.getSelectedRow(),2).toString()+", matricula: "+table.getValueAt(table.getSelectedRow(), 3)+", pertencente ao proprietario '"+table.getValueAt(table.getSelectedRow(),4).toString());
                        } catch (SQLException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                           
                        }
                        
                        LoadData();
                        table = new JTable(tableModel);
                        table.setPreferredScrollableViewportSize(new Dimension(1300,450));
                        Painel.setVisible(false);
                        Painel.remove(sp);
                        sp = new JScrollPane(table);
                        Painel.add(sp);
                        Painel.setVisible(true);
                         
                    }
                }
                return result;
    }
                
    
    public void LoadData(){
        
        txtPesquisar.setText("");
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                Statement stmt = conn.createStatement()) {

            
            
            ResultSet rs = stmt.executeQuery("select * from arquivo");
            ResultSetMetaData metaData = rs.getMetaData();

        
            
            
            
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
    
                columnNames.add("ID");
                columnNames.add("Marca");
                columnNames.add("Modelo");
                columnNames.add("Matricula");
                columnNames.add("Proprietario");
                columnNames.add("Preco da reparacao");
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

            ResultSet rs = stmt.executeQuery("SELECT * FROM `arquivo` ORDER BY `ID` ASC");
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
    
    public JifArquivo () throws HeadlessException, IOException{
        
        AutoCompleteTextField();
        File arquivo = new File((new File("log.txt")).getCanonicalPath());
        this.setLayout(new MigLayout());
        
        JPanel painelPesq = new JPanel(new MigLayout());
        buttonPainel = new JPanel(new MigLayout());
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1300,450));

        
        JLabel jlPesq = new JLabel();
        jlPesq.setIcon(new ImageIcon(getClass().getResource("/Icons/Search.png")));
        painelPesq.add(jlPesq);
        painelPesq.add(txtPesquisar);
        //JButton jbEditar = new JButton("Editar dados");
        //jbEditar.setIcon(new ImageIcon(getClass().getResource("/Icons/Update.png"))); 
        jbImprimir = new JButton("Imprimir");
        jbRemover = new JButton("Remover");
        jbImprimir.setIcon(new ImageIcon(getClass().getResource("/Icons/printer.png"))); 
        jbRemover.setIcon(new ImageIcon(getClass().getResource("/Icons/Remove.png")));
       // buttonPainel.add(jbEditar);
        sp = new JScrollPane(table);
        Painel.add(sp);
       
        LoadData();
       
        buttonPainel.add(jbImprimir);
        buttonPainel.add(jbRemover);
        
     this.setVisible(true);
        
        Painel.setVisible(true);
        buttonPainel.setVisible(true);
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
            public void actionPerformed(ActionEvent ae) {
                    Remover();
                    if(removido == true){
                    try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("O arquivo ''"+arquivoRemovido+"'' foi removido! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
            }

        });
            
     jbImprimir.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
       File pdf = null;
            try {
                pdf = new File(new File("Factura.pdf").getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(JifArquivo.class.getName()).log(Level.SEVERE, null, ex);
            }
                    try {
                        Desktop.getDesktop().open(pdf);
                    } catch (Exception e) {
                    }
                try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("O arquivo ''"+arquivoImprimido+"'' foi imprimido! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                
                
            
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

            ResultSet rs = stmt.executeQuery("select * from arquivo WHERE proprietario = '"+txtPesquisar.getText()+"'");
            ResultSetMetaData metaData = rs.getMetaData();

      
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
         
                columnNames.add("ID");
                columnNames.add("Marca");
                columnNames.add("Modelo");
                columnNames.add("Matricula");
                columnNames.add("Proprietario");
                columnNames.add("Avaria");
                columnNames.add("Preco da Reparacao");
            
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


