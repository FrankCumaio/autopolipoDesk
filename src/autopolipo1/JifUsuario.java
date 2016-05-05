
package autopolipo1;

import autopolipo1.ClienteForm;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
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
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//import static jdk.nashorn.internal.codegen.Compiler.LOG;
import net.miginfocom.swing.MigLayout;


public class JifUsuario extends JInternalFrame{
    private JLabel lbTipo,lbGravarEdicao,lbNome,lbUsername,lbSenha,lbConfirmarSenha,lbEditar, lbAdd, lbRemover;
    private JTextField txtNome,txtUserName;
    private JPasswordField txtSenha,txtConfirmarSenha;
    private JButton btGravarEdicao,btSaveNovo,btEditar, btAdd, btRemover,btCancelar;
    private JTable table;
    private boolean editado = false,removido = false,adicionado = false;
    private String usuarioRemovido,tipoUserSelect,usuarioEditado,usuarioAdicionado;
    private JScrollPane sp;
    private JRadioButton jrbAdmin,jrbOutro;
    private ButtonGroup grupo;
    private JPanel painel = new JPanel();
    private JPanel painelTable = new JPanel();
    private JPanel painelButton = new JPanel();
    private Connection con;
    private Statement st;
    private int cont=2;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    
    
    
     public String getDataActual(){
          DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
          Date hoje = null;
          return formato.format(Calendar.getInstance().getTime());
      }
      
      public String getHoraActual(){
          DateFormat formato = new SimpleDateFormat("HH:mm:ss");
          return formato.format(Calendar.getInstance().getTime());
      }
    
    
    
    
    public JifUsuario() throws SQLException, IOException{
        super("Definicoes do usuario");
        File arquivo = new File((new File("log.txt")).getCanonicalPath());
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
         
         
         painel = new JPanel(new MigLayout());
        painel.setBorder(BorderFactory.createEtchedBorder());
       painel.setBounds(20,20,440,190);
        
       jrbAdmin = new JRadioButton("Administrador");
       jrbOutro = new JRadioButton("Utilizador");
       
       grupo = new ButtonGroup();
         grupo.add(jrbAdmin);
         grupo.add(jrbOutro);
       
       lbTipo = new JLabel("Tipo de usuario");  
       lbNome = new JLabel("Nome completo");
       lbSenha = new JLabel("Senha");
       lbUsername = new JLabel("Nome de usuario");
       lbConfirmarSenha = new JLabel("Confirmar senha");
        lbAdd= new JLabel("Registar usuario");
        lbEditar = new JLabel("Editar dados do usuario");
        lbRemover = new JLabel("Remover usuario");
        
        btCancelar = new JButton("Cancelar");
        btAdd = new JButton("Adicionar");
        btEditar = new JButton("Editar");
        btSaveNovo = new JButton("Gravar");
        btGravarEdicao = new JButton("Gravar");
        btRemover = new JButton("Remover");
        txtNome = new JTextField(25);
        txtSenha = new JPasswordField(25);
        txtUserName = new JTextField(25);
        txtConfirmarSenha = new JPasswordField(25);
        txtUserName = new JTextField(25);
        
        ClassLoader loader = getClass().getClassLoader();
        
        btSaveNovo = new JButton(new ImageIcon(loader.getResource("Icons/Save.png")));
      
        btAdd = new JButton(new ImageIcon(loader.getResource("Icons/Add.png")));
        btEditar = new JButton(new ImageIcon(loader.getResource("Icons/Update.png")));
        btRemover = new JButton(new ImageIcon(loader.getResource("Icons/Remove.png")));
        btCancelar = new JButton(new ImageIcon(loader.getResource("Icons/Cancel.png")));
  
        btGravarEdicao = new JButton(new ImageIcon(loader.getResource("Icons/Save.png")));
 
        
                
        
       
        
        
       btRemover.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btAdd.setEnabled(true);
                try {
                    Remover();
                } catch (SQLException ex) {
                    Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    LoadData();
                } catch (SQLException ex) {
                    Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(removido == true){
                    try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("O usuario '"+usuarioRemovido+"' foi removido! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
            }
        })
               ;
       
       
       
       btGravarEdicao.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Update();
                   
                } catch (SQLException ex) {
                    Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    LoadData();
                } catch (SQLException ex) {
                    Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(editado == true){
                    try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("Foram alterados os dados do Usuario '"+usuarioEditado+"'! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
            }
        });
       
       btSaveNovo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 
                    try {
                        Adicionar();
                    } catch (SQLException ex) {
                        Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        LoadData();
                    } catch (SQLException ex) {
                        Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(adicionado == true){
                        try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("O usuario '"+usuarioAdicionado+"' foi adicionado! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            }
        });
        
        btCancelar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==btCancelar){
       
                   // painelButton.setVisible(false);
                    Clean();
                    //painelButton.add(btAdd,"gapleft 20");
                    //painelButton.add(btCancelar,"span");
                    lbConfirmarSenha.setForeground(Color.black);
                    txtConfirmarSenha.setText("");
                    txtNome.setText("");
                    txtSenha.setText("");
                    txtUserName.setText("");
                 btAdd.setEnabled(true);
                 btSaveNovo.setEnabled(false);
                 btGravarEdicao.setEnabled(false);
                 btRemover.setEnabled(false);
                 btCancelar.setEnabled(false);
                    //add(painelButton);
        
                }
            }
        });
        
        btAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==btAdd){
                    Clean();
                    
                    btGravarEdicao.setEnabled(false);
                   btRemover.setEnabled(false);
                  btAdd.setEnabled(false);
                   painelButton.remove(btRemover);
                   painelButton.remove(btGravarEdicao);
                    painelButton.add(btSaveNovo,"gapleft 20");
                    
                    painelButton.add(btRemover);
                    painelButton.add(btCancelar,"span");
                     painelButton.setVisible(true);
                     btSaveNovo.setEnabled(true);
                       btSaveNovo.setEnabled(true);
                       btCancelar.setEnabled(true);
                   painelButton.setBounds(24,185,440,50);
                    add(painelButton);
                    
                    painelTable.setBounds(15,235,445,180);

                    painelTable.setVisible(true);
                    
                    
                    
                   
                }
            }
        });
        
        
        
        try {
            LoadData();
        } catch (SQLException ex) {
            Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
        
                    btSaveNovo.setEnabled(true); 
                    btSaveNovo.setEnabled(true);
                    btGravarEdicao.setEnabled(false);
                   
                   
                    painelButton.remove(btGravarEdicao);
                    painel.add(lbTipo);
                    painel.add(jrbAdmin,"gapleft 55");
                    painel.add(jrbOutro,"span");
                    painel.add(lbNome);
                    painel.add(txtNome,"growx, span");
                    painel.add(lbUsername);
                    painel.add(txtUserName,"growx, span");
                    painel.add(lbSenha);
                    painel.add(txtSenha,"span");
                    painel.add(lbConfirmarSenha);
                    painel.add(txtConfirmarSenha," span, wrap");
                    painel.setVisible(true);
                    painel.setBounds(20,20,440,160);
                    add(painel);
                    painelButton.add(btAdd,"gapleft 20");
                    painelButton.add(btSaveNovo);
                    btSaveNovo.setEnabled(false);
                    painelButton.add(btRemover);
                    btRemover.setEnabled(false);
                    painelButton.add(btCancelar,"span");
                    btCancelar.setEnabled(false);
                     painelButton.setVisible(true);
                   painelButton.setBounds(20,185,440,50);
                   painelButton.setBorder(BorderFactory.createEtchedBorder());
                   painelTable.setBounds(15,235,445,180);
                    add(painelButton);
                    LoadData();
                    table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(420, 110));
        sp = new JScrollPane(table);
        
        painelTable.setBorder(BorderFactory.createTitledBorder("Lista de Usuarios"));
       painelTable.setBounds(15,235,445,180);
       painelTable.add(sp);
        
       this.add(painelTable);  
                       
        
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        
        table.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSeleccionada = table.getSelectedRow();
                
                ResultSet rs;
                
                try{
            String query ="SELECT * FROM usuario WHERE `username`='"+table.getValueAt(table.getSelectedRow(), 0).toString()+"'";
            rs=st.executeQuery(query);
            while(rs.next()){
                
              tipoUserSelect = rs.getString("Tipo");
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null, es);
           
             }
                
                
                painelButton.removeAll();
                btAdd.setEnabled(false);
                painelButton.remove(btSaveNovo);
               painelButton.add(btAdd,"gapleft 20");
                    painelButton.add(btGravarEdicao);
                    btGravarEdicao.setEnabled(true);
                    painelButton.add(btRemover);
                    btRemover.setEnabled(true);
                    painelButton.add(btCancelar,"span");
                    btCancelar.setEnabled(true);
                     painelButton.setVisible(true);
                   painelButton.setBounds(24,185,440,50);
                
                 txtNome.setText(table.getValueAt(linhaSeleccionada, 2).toString());
                    txtSenha.setText(table.getValueAt(linhaSeleccionada, 1).toString());
                    txtConfirmarSenha.setText(table.getValueAt(linhaSeleccionada, 1).toString());
                    txtUserName.setText(table.getValueAt(linhaSeleccionada, 0).toString());
                    
                     if(tipoUserSelect.equals("Administrador")){
                jrbAdmin.doClick();
               }else{
            if(tipoUserSelect.equals("Utilizador"))
                jrbOutro.doClick();
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
    
    
    
    public void LoadData() throws SQLException{
        
        ResultSet rs = null;
        try {
            rs = st.executeQuery("select * from usuario");
        } catch (SQLException ex) {
            Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
            ResultSetMetaData metaData = null;
        try {
            metaData = rs.getMetaData();
        } catch (SQLException ex) {
            Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

            // Names of columns
            Vector<String> columnNames = new Vector<String>();
             int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
                
                 Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getObject(i));
                }
                data.add(vector);
            }

            tableModel.setDataVector(data, columnNames);
    }
        
    
    public void Clean(){
                   txtNome.setText("");
                   txtConfirmarSenha.setText("");
                   txtSenha.setText("");
                   txtUserName.setText("");
                   // painelButton.remove(btAdd);
                   // painelButton.remove(btCancelar);
    }
    
    public void Adicionar() throws SQLException{
       String lastName = null,lastSenha = null,username = null,tipoUser = null,senha,nome,ConfirmarSenha="";
     
       
        int lastId = 0,id = 0;
        ResultSet rs;
        
            try{
            String query ="SELECT * FROM `usuario`  ";
            rs=st.executeQuery(query);
            while(rs.next()){
              lastName = rs.getString("nome");
              lastSenha = rs.getString("senha");
              lastId = rs.getInt("ID");
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null, es);
             }
        
            if(jrbAdmin.isSelected())
                tipoUser = "Administrador";
            
            if(jrbOutro.isSelected())
                tipoUser = "Utilizador";
            
            username=txtUserName.getText();
            senha= txtSenha.getText();
            nome = txtNome.getText();
            ConfirmarSenha = txtConfirmarSenha.getText();
            id = lastId+1;
            if(!(txtSenha.getText().trim().equals(txtConfirmarSenha.getText().trim()))){
                lbConfirmarSenha.setForeground(Color.red);
                JOptionPane.showMessageDialog(null,"A senha introduzida nao e valida","Erro",JOptionPane.ERROR_MESSAGE);
                adicionado = false;
                return;
            }else{
               if(txtSenha.getText().trim().equals(lastSenha) && txtNome.getText().trim().equals(lastName)) {
                    JOptionPane.showMessageDialog(null,"Os dados introduzidos nao sao validos","Erro",JOptionPane.ERROR_MESSAGE);
                    adicionado = false;
                    return;
               } 
            }
            PreparedStatement st = null;
        try {
            st = (PreparedStatement) con.prepareStatement("INSERT INTO autopolipo.usuario (username, senha, nome, ID, Tipo) VALUES (?,?,?,?,?)");
        } catch (SQLException ex) {
            Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
           
            st.setString(1,username);
            st.setString(2,senha);
            st.setString(3,nome);
            st.setInt(4, id);
            st.setString(5, tipoUser);
            if(("".equals(nome))||("".equals(username))||("".equals(senha))||("".equals(nome))){
                JOptionPane.showMessageDialog(null,"Nenhum campo deve estar vazio","Erro",JOptionPane.ERROR_MESSAGE);
                adicionado = false;
               return;
            }
            st.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuario adicionado com sucesso!","Informacao",JOptionPane.INFORMATION_MESSAGE);
            lbConfirmarSenha.setForeground(Color.black);
            adicionado = true;
            usuarioAdicionado = nome;
             return;
    }
     
    public int Remover() throws SQLException{
        
        int result = 0;
         int rowSelected = table.getSelectedRow();
                if(rowSelected == -1){
                    JOptionPane.showMessageDialog(null,"Seleccione o usuario a ser removido","Atencao",JOptionPane.WARNING_MESSAGE);
                    removido = false;
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
                        
          int id = 0;              
         id =(int)table.getValueAt(table.getSelectedRow(), 3);
              
             PreparedStatement st = null;
                        try {
                            st = con.prepareStatement("DELETE FROM autopolipo.usuario WHERE usuario.ID = ?");
                            st.setInt(1, id);
                            result = st.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Usuario removido com sucesso!","Informacao",JOptionPane.INFORMATION_MESSAGE);
                            removido = true;
                            usuarioRemovido = table.getValueAt(table.getSelectedRow(), 2).toString();
                        } catch (SQLException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                      
                    }else{
                    removido = false;
                            }
                }
                        LoadData();
                       /* table = new JTable(tableModel);
                        painelTable.setVisible(false);
                        painelTable.remove(sp);
                        
                        table.setPreferredScrollableViewportSize(new Dimension(420, 110));
                        sp = new JScrollPane(table);
                         sp.setSize(590, 158);
                        painelTable.add(sp);
                        painelTable.setVisible(true);
                        
                        this.add(painelTable);*/
        return result;
        
    }
    
  
    
    
    public void Update() throws SQLException{
        int result = 0;
        int id = 0; 
         int rowSelected = table.getSelectedRow();
                if(rowSelected == -1){
                    JOptionPane.showMessageDialog(null,"Seleccione o usuario a editar","Atencao",JOptionPane.WARNING_MESSAGE);
                    editado = false;
                }else{
                    int confirm = JOptionPane.showConfirmDialog(null,"Deseja continuar?","Atencao",JOptionPane.YES_NO_OPTION);
                    if(confirm ==0){
                        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
          st=con.createStatement();
        }catch(Exception ed){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados","Erro",JOptionPane.ERROR_MESSAGE);
        }
                        
                        
                       
         id =(int)table.getValueAt(table.getSelectedRow(), 3);
              
             PreparedStatement st = null;
                        try {
                            st = con.prepareStatement("DELETE FROM autopolipo.usuario WHERE usuario.ID = ?");
                            st.setInt(1, id);
                            result = st.executeUpdate();
                            //JOptionPane.showMessageDialog(null, "Usuario removido com sucesso!");
                        } catch (SQLException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
              
               String lastName = null,lastSenha = null,username,tipoUser = null,senha,nome,ConfirmarSenha="";
     
       
        int lastId = 0;
        ResultSet rs;
        
            try{
            String query ="SELECT * FROM `usuario`  ";
            rs=st.executeQuery(query);
            while(rs.next()){
              lastName = rs.getString("nome");
              lastSenha = rs.getString("senha");
              lastId = rs.getInt("ID");
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null,"Ocorreu um erro ao carregar os dados da base de dados","Erro",JOptionPane.ERROR_MESSAGE);
            editado = false;
             }
        
            
            username=txtUserName.getText();
            senha= txtSenha.getText();
            nome = txtNome.getText();
            ConfirmarSenha = txtConfirmarSenha.getText();
     
             if(jrbAdmin.isSelected())
                tipoUser = "Administrador";
            
            if(jrbOutro.isSelected())
                tipoUser = "Utilizador";
            
            if(!(txtSenha.getText().trim().equals(txtConfirmarSenha.getText().trim()))){
                lbConfirmarSenha.setForeground(Color.red);
                JOptionPane.showMessageDialog(null,"A senha introduzida nao e valida","Erro",JOptionPane.ERROR_MESSAGE);
                editado = false;
                return;
            }else{
               if(txtSenha.getText().trim().equals(lastSenha) && txtNome.getText().trim().equals(lastName)) {
                    JOptionPane.showMessageDialog(null,"Os dados introduzidos nao sao validos","Erro",JOptionPane.ERROR_MESSAGE);
                    editado = false;
                    return;
               } 
            }
            PreparedStatement st = null;
        try {
            st = (PreparedStatement) con.prepareStatement("INSERT INTO autopolipo.usuario (username, senha, nome, ID, Tipo) VALUES (?,?,?,?,?)");
        } catch (SQLException ex) {
            Logger.getLogger(JifUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
           
            st.setString(1,username);
            st.setString(2,senha);
            st.setString(3,nome);
            st.setInt(4, id);
            st.setString(5, tipoUser);
            if(("".equals(nome))|| (tipoUser.equals("")) ||("".equals(username))||("".equals(senha))||("".equals(nome))){
                JOptionPane.showMessageDialog(null,"Nenhum campo deve estar vazio","Atencao",JOptionPane.WARNING_MESSAGE);
                        editado= false;
               return;
            }
            st.executeUpdate();
            JOptionPane.showMessageDialog(null, "Dados gravados com sucesso!","Informacao",JOptionPane.INFORMATION_MESSAGE);
            editado = true;
            usuarioEditado = nome;
            lbConfirmarSenha.setForeground(Color.black);
             LoadData();
                        
                    
                        
                        //this.add(painelTable);
        return; 
                
    }     
    
    
    
    
    }
    

