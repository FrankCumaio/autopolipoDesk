/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autopolipo1;

import com.mysql.jdbc.exceptions.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
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
import java.sql.DriverManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static jdk.nashorn.internal.codegen.Compiler.LOG;

public class ClienteForm extends JInternalFrame {
    File arquivo = new File("C:\\Users\\Mr. Belton\\Desktop\\INSTRUCOES.txt");
    private JLabel  lbTitle,lbGravar, lbCancelar,lbNovoCliente, lbGravarNovo, lbRemover, lbID,lbNome, lbBI, lbContacto, lbMorada,lbTipoCliente;
    private JRadioButton jrbTipoPessoal, jrbTipoEmpresarial;
    private JTextField txtID,txtNome, txtBI, txtContacto, txtMorada, txtTipoCliente;
    private JPanel painelAdd,painelTable, painelButton;
    private JButton btNovoCliente,btNovo, btGravar, btRemove, btCancelar;
    public JTable table;
    private JScrollPane sp, sp1;
    JPanel painelPesq = new JPanel(new MigLayout());
    private ArrayList<String> proprietarios = new ArrayList<String>();
    private AtfProcurar txtPesquisar = new AtfProcurar(proprietarios);
    private Connection con;
    private Statement st;
    private String tipoClienteSelect,LOGtxt="Erro na Classe ClienteForm: ";;
    private int linha =0;
    private ButtonGroup grupo;
    private int cont=0;
    private boolean editado=false,adicionado=false,removido=false;
    private final DefaultTableModel tableModel = new DefaultTableModel();
   
   
    private int IDcliente;
    GUI data = new GUI();
    
    
    
    public String getDataActual(){
          DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
          Date hoje = null;
          return formato.format(Calendar.getInstance().getTime());
      }
      
      public String getHoraActual(){
          DateFormat formato = new SimpleDateFormat("HH:mm:ss");
          return formato.format(Calendar.getInstance().getTime());
      }
    
    
     public void AutoCompleteTextField() {
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("select * from clientes");
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next())
            {
                proprietarios.add(rs.getString("nome"));
            }
            
        } catch (Exception e) {
        }
        
        
        txtPesquisar.setText("");
        txtPesquisar.setToolTipText("Pesquisa pelo nome do cliente");
        txtPesquisar.setMinimumSize(new Dimension(200,10));
        }
    
      
      
    
    
    public ClienteForm() throws HeadlessException, IOException {
 
        
         super("Menu Clientes");
         File arquivo = new File((new File("log.txt")).getCanonicalPath());
         AutoCompleteTextField();
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
            LOGtxt = LOGtxt+"Erro na Classe ClienteForm: "+e+"\n \n";
        }
        
        JLabel jlPesq = new JLabel();
        jlPesq.setIcon(new ImageIcon(getClass().getResource("/Icons/Search.png")));
        lbTitle = new JLabel("Lista de Clientes");
       // lbTitle.setText();
        //lbTitle.setFont(new Font(, 2, 14));
        painelPesq.add(lbTitle,"gapleft 70");
        painelPesq.add(jlPesq, "gapleft 132");
        txtPesquisar.setMinimumSize(new Dimension(230,10));
        painelPesq.add(txtPesquisar);
     
       
        painelPesq.setVisible(true);
        
        
        //Painel de Entrada
        
        painelAdd = new JPanel(new MigLayout());
        painelAdd.setBorder(BorderFactory.createTitledBorder("Adicionar Cliente"));
        painelAdd.setBounds(5,0,600,200);
        
        lbNome = new JLabel("Nome");
        
        lbBI = new JLabel("Bilhete de Identificacao");
        lbContacto = new JLabel("Contacto (telefone)");
        lbMorada = new JLabel("Morada");
        lbTipoCliente = new JLabel("Tipo de Cliente");
        jrbTipoPessoal = new JRadioButton("Pessoal");
        jrbTipoEmpresarial = new JRadioButton("Empresarial");
        
        jrbTipoPessoal.setMnemonic(KeyEvent.VK_J);
        jrbTipoEmpresarial.setMnemonic(KeyEvent.VK_C);
        jrbTipoPessoal.setEnabled(false);
        jrbTipoEmpresarial.setEnabled(false);
       
        
         ///
             grupo = new ButtonGroup();
         grupo.add(jrbTipoEmpresarial);
         grupo.add(jrbTipoPessoal);
        
        lbNovoCliente = new JLabel("Novo Cliente");
        lbGravarNovo = new JLabel("Gravar");
        lbGravar = new JLabel("Gravar");
        
        
        lbRemover = new JLabel("Remover");
        lbCancelar = new JLabel("Cancelar");
        
        
        txtBI = new JTextField(15);
        txtContacto = new JTextField(15);
        txtMorada = new JTextField(50);
        txtNome = new JTextField(50);
        txtTipoCliente = new JTextField(15);
        
        
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
        
        
        
           //Painel de Botoes
        painelButton = new JPanel( new MigLayout());
        painelButton.setBorder(BorderFactory.createEtchedBorder());
        painelButton.setBounds(5,210,600,70);
        
        ClassLoader loader = getClass().getClassLoader();
        btNovoCliente = new JButton("Novo Cliente");
        btNovo = new JButton("Novo");
        btGravar = new JButton("Gravar");
        btCancelar = new JButton("Cancelar");
        
        btNovoCliente = new JButton(new ImageIcon(loader.getResource("Icons/Add.png")));
        btNovo = new JButton(new ImageIcon(loader.getResource("Icons/Save.png")));
        btGravar = new JButton(new ImageIcon(loader.getResource("Icons/Save.png")));
        btCancelar = new JButton(new ImageIcon(loader.getResource("Icons/Cancel.png")));
      
        btRemove = new JButton(new ImageIcon(loader.getResource("Icons/Remove.png")));
       
       txtNome.setEnabled(false);
           txtBI.setEnabled(false);
           txtContacto.setEnabled(false);
           txtMorada.setEnabled(false);
           txtTipoCliente.setEnabled(false);
       
           
       btNovo.setEnabled(false);
       btGravar.setEnabled(false);
       btCancelar.setEnabled(false);
       btRemove.setEnabled(false);
       
       painelButton.add(btNovoCliente,  " gapleft 160" );
       painelButton.add(btNovo);
       painelButton.add(btCancelar);

  
       painelButton.add(btRemove,"span");

       
       painelButton.add(lbNovoCliente,  " gapleft 150");
       painelButton.add(lbGravarNovo," gapleft 15");
       painelButton.add(lbCancelar,  " gapleft 5");
    

       painelButton.add(lbRemover,  " gapleft 5");


       
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

            ResultSet rs = stmt.executeQuery("select * from clientes WHERE nome = '"+txtPesquisar.getText()+"'");
            ResultSetMetaData metaData = rs.getMetaData();

      
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
         
                columnNames.add("ID");
                columnNames.add("Tipo de Cliente");
                columnNames.add("Nome");
                columnNames.add("Bilhete de Identificacao");
                columnNames.add("Morada");
                columnNames.add("Contacto");
            
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
        
    
       
       
       btNovoCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
       painelButton.removeAll();
      
       btRemove.setEnabled(false);
       btNovoCliente.setEnabled(false);
      
       
      
       painelButton.add(btNovoCliente,  " gapleft 160" );
       painelButton.add(btNovo);
       painelButton.add(btCancelar);
       btCancelar.setEnabled(true);
        btNovo.setEnabled(true);
       
       painelButton.add(btRemove,"span");

       
       painelButton.add(lbNovoCliente,  " gapleft 150");
       painelButton.add(lbGravarNovo," gapleft 15");
       painelButton.add(lbCancelar,  " gapleft 5");
      // painelButton.add(lbGravar,  " gapleft 10");

       painelButton.add(lbRemover,  " gapleft 5");
                
                
                btCancelar.setEnabled(true);
                ActivarCampos(true);
                Limpar();
                btNovo.setEnabled(true);
                btGravar.setEnabled(false);
                painelButton.remove(btGravar);
               
            }
        });
       
       btGravar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == btGravar){
                  
                    FileWriter fw = null;
                   
                        try {
                            Update();
                             Limpar();
                        } catch (SQLException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if(editado == true){
                            try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("Cliente Editado! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    } catch (IOException ex) {
                        Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            fw.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    }
                    
                    
                }
            }
        });
       btRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              Remove();
              
             
              LoadData();
           
                        painelTable.setVisible(false);
                        painelTable.remove(sp);
                        table = new JTable(tableModel);
                        table.setPreferredScrollableViewportSize(new Dimension(558, 170));
                        sp = new JScrollPane(table);
                         sp.setSize(590, 158);
                        
                        
                        painelTable.add(sp);
                        painelTable.setVisible(true);
                        
                        add(painelTable);
                        
                        btNovoCliente.setEnabled(true);
                  btCancelar.setEnabled(false);
                  btGravar.setEnabled(false);
                  btNovo.setEnabled(false);
                  btRemove.setEnabled(false);
                        
                        if(removido == true){
                            try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("Cliente Removido! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                   
                        
                      
                        }  
                         Limpar();
                        ActivarCampos(true);
                        
        }
                
       });
       
       
       btNovo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                
                    btCancelar.setEnabled(false);
                    btNovo.setEnabled(true);
                    btGravar.setEnabled(false);
                    btRemove.setEnabled(false);
                    painelButton.removeAll();
                    
                    painelButton.add(btNovoCliente,  " gapleft 160" );
                    painelButton.add(btNovo);
                    painelButton.add(btCancelar);
                    //painelButton.add(btGravar);
                    
                    painelButton.add(btRemove,"span");
                    
                    
                    painelButton.add(lbNovoCliente,  " gapleft 150");
                    painelButton.add(lbGravarNovo," gapleft 15");
                    painelButton.add(lbCancelar,  " gapleft 5");
                    // painelButton.add(lbGravar,  " gapleft 10");

                    painelButton.add(lbRemover,  " gapleft 5");
                    Adicionar();
                    
                    btNovoCliente.setEnabled(true);
                    btCancelar.setEnabled(false);
                    
                   if(adicionado==true){
                       try{
                    if (!arquivo.exists()) {
                        
                        arquivo.createNewFile();
                    }
                    File[] arquivos = arquivo.listFiles();
                    FileWriter fw = new FileWriter(arquivo, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("Cliente Adicionado! Hora: "+getHoraActual()+" Data: "+getDataActual());
                    bw.newLine();
                    bw.close();
                    fw.close();
                   
                   }catch (IOException ex) {
                    Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                   }

            }
        });
        
       
        btCancelar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if(ae.getSource() == btCancelar){
                  Limpar();
                  ActivarCampos(true);
                  btNovoCliente.setEnabled(true);
                  btCancelar.setEnabled(false);
                  btGravar.setEnabled(false);
                  btNovo.setEnabled(false);
                  btRemove.setEnabled(false);
                }
            }
        });
        
        

         

        
       //Painel da tabela
       painelTable = new JPanel();
       painelTable.setBorder(BorderFactory.createTitledBorder(""));
       painelTable.setBounds(5,290,600,290);
       
       
       LoadData();
       table = new JTable(tableModel);
       table.setPreferredScrollableViewportSize(new Dimension(558, 170));
       sp = new JScrollPane(table);
      

       sp.setSize(590, 158);
       
      
       painelTable.add(painelPesq);
       painelTable.add(sp);
      
     
       
       data.setBounds(5,230,590,250);
       
        this.add(painelAdd);
        this.add(painelButton);
        this.add(painelTable);
        this.setMinimumSize(new Dimension(620,740));
        this.setVisible(true);
        this.setResizable(false);
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setClosable(true);
        this.setIconifiable(true);
        
        
        
         table.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getSource() == table){
                  ActivarCampos(true);
                 btRemove.setEnabled(true);
               
              ResultSet rs;
        
            try{
            String query ="SELECT * FROM clientes WHERE `nome`='"+table.getValueAt(table.getSelectedRow(), 2).toString()+"'";
            rs=st.executeQuery(query);
            while(rs.next()){
                
              tipoClienteSelect = rs.getString("tipocliente");
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null, es);
            LOGtxt = LOGtxt+"Erro na Classe ClienteForm: "+es+"\n \n";
             }
             
               
                
                 
                
                btNovo.setEnabled(false);
              
               txtNome.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
              txtBI.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
              txtMorada.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
              txtContacto.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
              
               if(tipoClienteSelect.equals("Pessoal")){
                jrbTipoPessoal.doClick();
               }else{
            if(tipoClienteSelect.equals("Empresarial"))
                jrbTipoEmpresarial.doClick();
               }
    
       painelButton.removeAll();
       
       painelButton.add(btNovoCliente,  " gapleft 160" );
       painelButton.add(btGravar);
       painelButton.add(btCancelar);
       painelButton.add(btRemove,"span");
        btGravar.setEnabled(true);
        btCancelar.setEnabled(true);
       
       painelButton.add(lbNovoCliente,  " gapleft 150");
       painelButton.add(lbGravar," gapleft 15");
       painelButton.add(lbCancelar,  " gapleft 5");
      // painelButton.add(lbGravar,  " gapleft 10");
     
       painelButton.add(lbRemover,  " gapleft 5");
              
            
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
    
        
        
    }
    
    
    
    
   
    
    
    
    public void Limpar(){
        ActivarCampos(true);
               txtNome.setText("");
                txtBI.setText("");
                 txtContacto.setText("");
                  txtMorada.setText("");
                  
            table.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getSource() == table){
                 
                    ActivarCampos(true);
                    btRemove.setEnabled(true);
              ResultSet rs;
        
            try{
            String query ="SELECT * FROM clientes WHERE `nome`='"+table.getValueAt(table.getSelectedRow(), 2).toString()+"'";
            rs=st.executeQuery(query);
            while(rs.next()){
                
              tipoClienteSelect = rs.getString("tipocliente");
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null, es);
            LOGtxt = LOGtxt+"Erro na Classe ClienteForm: "+es+"\n \n";
             }
            
                    
                
                if(tipoClienteSelect.equals("Pessoal")){
                jrbTipoPessoal.doClick();
                }else{
            if(tipoClienteSelect.equals("Empresarial"))
                jrbTipoEmpresarial.doClick();
                }
                btNovo.setEnabled(false);
                btGravar.setEnabled(true);
                 btCancelar.setEnabled(true);
               txtNome.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
              txtBI.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
              txtMorada.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
              txtContacto.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
              
              
    
       painelButton.removeAll();
       
       painelButton.add(btNovoCliente,  " gapleft 160" );
       painelButton.add(btGravar);
       painelButton.add(btCancelar);
       //painelButton.add(btGravar);
    
       painelButton.add(btRemove,"span");

       
       painelButton.add(lbNovoCliente,  " gapleft 150");
       painelButton.add(lbGravar," gapleft 15");
       painelButton.add(lbCancelar,  " gapleft 5");
      // painelButton.add(lbGravar,  " gapleft 10");
     
       painelButton.add(lbRemover,  " gapleft 5");
              
            
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
              
    }


    
    public void Update() throws SQLException{
        int result = 0;
        String lastTipocliente = null,lastContacto = null,lastMorada = null,lastBi = null,lastNome = null,nome,tipoCliente = null,morada,bi;
               int contacto;
        int id = 0; 
         int rowSelected = table.getSelectedRow();
                if(rowSelected == -1){
                    JOptionPane.showMessageDialog(null,"Seleccione o usuario a editar","Erro",JOptionPane.ERROR_MESSAGE);
                    ActivarCampos(false);
                    editado=false;
                    return;
                }else{
                    int confirm = JOptionPane.showConfirmDialog(null,"Deseja continuar?","Editar Usuario",JOptionPane.YES_NO_OPTION);
                    if(confirm ==0){
                        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
          st=con.createStatement();
        }catch(Exception ed){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados","Erro",JOptionPane.ERROR_MESSAGE);
            LOGtxt = LOGtxt+"Erro na Classe ClienteForm: "+ed+"\n \n";
        }
                        
                        
                       
         id =(int)table.getValueAt(table.getSelectedRow(), 0);
              
             PreparedStatement st = null;
                        try {
                            st = con.prepareStatement("DELETE FROM autopolipo.clientes WHERE clientes.ID = ?");
                            st.setInt(1, id);
                            result = st.executeUpdate();
                            //JOptionPane.showMessageDialog(null, "Usuario removido com sucesso!");
                        } catch (SQLException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                            LOGtxt = LOGtxt+"Erro na Classe ClienteForm: "+ex+"\n \n";
                        }
                    }
                    
                }
              
               
     
       
        int lastId = 0;
        ResultSet rs;
        
            try{
            String query ="SELECT * FROM `clientes`  ";
            rs=st.executeQuery(query);
            while(rs.next()){
                lastNome = rs.getString("nome");
              lastId = rs.getInt("ID");
              lastBi = rs.getString("bi");
              lastMorada = rs.getString("morada");
              lastContacto = rs.getString("contacto");
              lastTipocliente = rs.getString("tipocliente");
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null, es);
            LOGtxt = LOGtxt+es+"\n \n";
             }
        
            
            bi = txtBI.getText();
            morada = txtMorada.getText();
            if(jrbTipoPessoal.isSelected()==true)
                tipoCliente = "Pessoal";
            if(jrbTipoEmpresarial.isSelected()== true)
                tipoCliente = "Empresarial";
            contacto = Integer.parseInt(txtContacto.getText());
            nome = txtNome.getText();
     
            
               if((txtBI.getText().trim().equals(lastBi)) && (txtNome.getText().trim().equals(lastNome)) && (txtMorada.getText().trim().equals(lastMorada)) && (txtID.getText().trim().equals(lastId)) && (txtContacto.getText().trim().equals(lastContacto)) && (txtTipoCliente.getText().trim().equals(lastTipocliente)) ) {
                    JOptionPane.showMessageDialog(null,"Os dados introduzidos nao sao validos","Erro",JOptionPane.ERROR_MESSAGE);
                      LOGtxt = LOGtxt+"Erro na Classe ClienteForm: Os dados introduzidos nao sao validos \n \n";
                      editado=false;
                    return;
               } 
            
            PreparedStatement st = null;
        try {
         st =(PreparedStatement) con.prepareStatement("INSERT INTO clientes (ID, tipocliente, nome, bi, morada, contacto) VALUES (?, ?, ?, ?, ?, ?)");
        } catch (SQLException ex) {
            Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
            LOGtxt = LOGtxt+"Erro na Classe ClienteForm: "+ex+"\n \n";
        }
           
            st.setInt(1, id);
            st.setString(2, tipoCliente);
            st.setString(3, nome);
            st.setString(4, bi);
            st.setString(5, morada);
            st.setInt(6, contacto);
        
        
            if(("".equals(nome))||("".equals(morada))||("".equals(contacto))||("".equals(bi)) || ((jrbTipoPessoal.isSelected() == false) && (jrbTipoEmpresarial.isSelected() == false))){
                JOptionPane.showMessageDialog(null,"Nenhum campo deve estar vazio","Erro",JOptionPane.ERROR_MESSAGE);
                LOGtxt = LOGtxt+"Erro na Classe ClienteForm: Nenhum campo deve estar vazio \n \n";
                editado=false;
               return;
            }
            st.executeUpdate();
            JOptionPane.showMessageDialog(null, "Dados gravados com sucesso!","Sucesso",JOptionPane.INFORMATION_MESSAGE);
            LOGtxt = LOGtxt+"Dados gravados com sucesso! \n \n";
            editado=true;
                        LoadData();
                         painelTable.setVisible(false);
                        painelTable.remove(sp);
                        table = new JTable(tableModel);
                        table.setPreferredScrollableViewportSize(new Dimension(558, 170));
                        sp = new JScrollPane(table);
                         sp.setSize(590, 158);
                        
                        
                        painelTable.add(sp);
                        painelTable.setVisible(true);
                        
                        add(painelTable);
                        
                        
                        
                        
  return;
}
      

   public void LoadData() {
   
//       LOG.info("START loadData method");


        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("select * from clientes");
            ResultSetMetaData metaData = rs.getMetaData();

        
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
                columnNames.add("ID");
                columnNames.add("Tipo de Cliente");
                columnNames.add("Nome");
                columnNames.add("Bilhete de Identificacao");
                columnNames.add("Morada");
                columnNames.add("Contacto");
                

        
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
//            LOG.log(Level.SEVERE, "Ocorreu um erro ao carregar os dados!","Erro",JOptionPane.ERROR_MESSAGE);
            LOGtxt = LOGtxt+"Erro na Classe ClienteForm: "+e+"\n \n";
        }
      
       
       
       
      
        
      
//        LOG.info("END loadData method");
        this.setVisible(true);
        
        
        
        
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
                 adicionado=false;
                return;
            }
            try{
            st.setInt(1, id);
            st.executeUpdate();
            }catch (MySQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(null,e+"Ja existe um cliente com este contacto(numero de telefone).\n Introduza um novo numero.");
            adicionado=false;
        }
            
            
            JOptionPane.showMessageDialog(null,"Cliente adicionado com sucesso!","Adicionar Cliente",JOptionPane.INFORMATION_MESSAGE);
            Limpar();
            adicionado=true;
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Verifique se todos campos foram devidamente preenchidos \n e/ou se os tipos de dado introduzidos sao validos!","Erro",JOptionPane.ERROR_MESSAGE);
            adicionado=false;
                   
        }       catch (SQLException ex) {
                    Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                }
      
             LoadData();
           
                        painelTable.setVisible(false);
                        painelTable.remove(sp);
                        table = new JTable(tableModel);
                        table.setPreferredScrollableViewportSize(new Dimension(558, 170));
                        sp = new JScrollPane(table);
                         sp.setSize(590, 158);
                        
                        
                        painelTable.add(sp);
                        painelTable.setVisible(true);
                        
                        add(painelTable);
   }
   
   public void ActivarCampos(boolean b){
           txtNome.setEnabled(b);
           txtBI.setEnabled(b);
           txtContacto.setEnabled(b);
           txtMorada.setEnabled(b);
           jrbTipoPessoal.setEnabled(b);
        jrbTipoEmpresarial.setEnabled(b);

   }
   
   
   public int Remove(){
       int result = 0;
         int rowSelected = table.getSelectedRow();
                if(rowSelected == -1){
                    JOptionPane.showMessageDialog(null,"Seleccione o cliente a ser removido","Atencao",JOptionPane.WARNING_MESSAGE);
                    removido=false;
                }else{
                    int confirm = JOptionPane.showConfirmDialog(null,"Deseja mesmo remover?","Remover Cliente",JOptionPane.YES_NO_OPTION);
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
                            st = con.prepareStatement("DELETE FROM autopolipo.clientes WHERE clientes.ID = ?");
                            st.setInt(1, id);
                            result = st.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!","Remover Cliente",JOptionPane.INFORMATION_MESSAGE);
                           
                            removido=true;
                        } catch (SQLException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        return result;
                        
                    }
                    
                    
                }
                        LoadData();
                        table = new JTable(tableModel);
                        painelTable.setVisible(false);
                        painelTable.remove(sp);
                        
                        table.setPreferredScrollableViewportSize(new Dimension(558, 170));
                        sp = new JScrollPane(table);
                         sp.setSize(590, 158);
                        painelTable.add(sp);
                        painelTable.setVisible(true);
                        
                        this.add(painelTable);
        return result;
                
            }
   
   
   
     
   
   }
   
   
        

    
    
    

