
package autopolipo1;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;


public class JifPecas extends JInternalFrame{
    private JLabel lbMtn,lbPreco,lbTipo,lbMarca,lbModelo,lbQuant,lbEditar, lbAdd, lbRemover;
    private JTextField txtPreco,txtQuant;
    private JComboBox jcbModelo,jcbMarca,jcbTipo;
    private JButton btGravar, btCancelar, btRemover;
    private JTable table;
    private JScrollPane sp;
    private String[] novoArray, outroArray;
     private String marca,modelo;
    private String[] modelos = {""};
    private boolean editado = false,removido = false,adicionado = false;
    private String acessorioRemovido,acessorioEditado,acessorioAdicionado,novoTipo;
    private JPanel painel = new JPanel();
    private JPanel painelTable = new JPanel();
    private JPanel painelButton = new JPanel();
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private int nRow,nCol,cod;
    private Object idd;
    private boolean actualizar=false,gravar=false,selected=true;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private String[] marcas =  {"","Universal","Outra","Toyota", "Nissan", "Honda", "Mazda" , "Subaru", "Mercedes", "Audi", "BMW"};
    private String[] acessorios =  {"","Outro","Lampadas", "Calços", "Balatas", "Oleo de Travao DOT3/4" , "Oleo de Motor(Gasolina)", "Oleo de Motor(Gasolina)", "Velas", "Filtros de Ar", "Filtros de combustivel"};
  
    
    public void LoadModels(){
        if(jcbMarca.getSelectedItem().equals("Universal"))
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
             jcbModelo.setModel(new DefaultComboBoxModel(new String[] {"","Avensis","Allion","Altezza","Corolla Runx","Corolla Spacio","Camry","Chaser","Duet","Harrier","HIACE van","Hilux","Land Cruiser","Mark II","Prado","RAV4","Starlet","Vitz","Wish",}));
         else{
               if(jcbMarca.getSelectedItem().equals("Nissan"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"","Atlas","Bluebird","Civilian","Datsun","Fairlady Z","Fuga","Gloria","Homy","Hardbody","Murano","March","Navara","Pulsar","Safari","Sunny","Skyline","Terrano","Tilda","X-Trail",}));
         else{
               if(jcbMarca.getSelectedItem().equals("Honda"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"","Accord","Acty","Civic","CR-V","Fit","Fit Aria","HR-v","Insight","Integra","Logo","Mobilio","Odyssey","Partner","Prelude","S2000","Step Wagon","Stream","Street"}));
         else{
               if(jcbMarca.getSelectedItem().equals("Mazda"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"","Atenza","Alexa","Bongo","Capella","Cx-5","Cx-7","Demio","Familia","Levante","Mazda 3","Mazda 5","MPV","Mx-6","Premacy","Rx-7","Rx-8","Titan","Tribute","Verisa",}));
         else{
               if(jcbMarca.getSelectedItem().equals("Mercedes"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"","A-Class","B-Class","C-Class","CLK-Class","CLS-Class","E-Class","G-Class","GL-Class","GLK-Class","M-Class","ML-Class","SL-Class","SLK","SLR","Vaneo","V-Class","Viano"}));
         else{
               if(jcbMarca.getSelectedItem().equals("Subaru"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"","Domingo","Forester","Impreza","Impreza ANESIS","Impreza WRX","Legacy","Legacy B4","Outback","R2","Sambar","Stella"}));
         else{
               if(jcbMarca.getSelectedItem().equals("Audi"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"","A3","A4","A5","A6","A8","Allroad Quattro","Q5","Q7","Quattro","RS4","RS6","S3","S4","S5","S8","TT"}));
         else{
               if(jcbMarca.getSelectedItem().equals("BMW"))
               jcbModelo.setModel(new DefaultComboBoxModel(new String[]{"","1 Series","3 Series","5 Series","6 Series","7 Series","Mini","Mini Cooper","Mini Clubman","X1","X3","X5","X6","Z3","Z4","Z8"}));
              }}}}}}}}}}
     
    
    public void LoadCombos(){
        jcbTipo.setEnabled(false);
        jcbMarca.setEnabled(false);
        jcbModelo.setEnabled(false);
      /*
        if(!(table.getValueAt(nRow,0).toString().equals("Outro")|| table.getValueAt(nRow,0).toString().equals("Lampadas") || table.getValueAt(nRow,0).toString().equals("Calços") || table.getValueAt(nRow,0).toString().equals("Balatas") || table.getValueAt(nRow,0).toString().equals("Oleo de Travao DOT3/4") || table.getValueAt(nRow,0).toString().equals("Oleo de Motor(Gasolina)") || table.getValueAt(nRow,0).toString().equals("Oleo de Motor(Gasolina)") || table.getValueAt(nRow,0).toString().equals("Velas") || table.getValueAt(nRow,0).toString().equals("Filtros de Ar")|| table.getValueAt(nRow,0).toString().equals("Filtros de combustivel") )){
          jcbTipo.setSelectedItem("Outro");
          jcbMarca.setSelectedItem(table.getValueAt(nRow,1));
          jcbModelo.setSelectedItem(table.getValueAt(nRow,2));
          txtQuant.setText(table.getValueAt(nRow,3).toString());
          txtPreco.setText(table.getValueAt(nRow,4).toString());
    }else{
        */
       jcbTipo.setSelectedItem(table.getValueAt(nRow,0));
        jcbMarca.setSelectedItem(table.getValueAt(nRow,1));
        jcbModelo.setSelectedItem(table.getValueAt(nRow,2));
        txtQuant.setText(table.getValueAt(nRow,3).toString());
        txtPreco.setText(table.getValueAt(nRow,4).toString());
        
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
    
    
    
    
    public void Ouve(){
      table.addMouseListener(new MouseListener() {  
             
            public void mouseClicked(MouseEvent me) {
                
                nRow = table.getSelectedRow();
                nCol = table.getSelectedColumn();
                idd= table.getValueAt(nRow,0);
                btGravar.setText("Actualizar");
                btGravar.setIcon(new ImageIcon(getClass().getClassLoader().getResource("Icons/Update.png")));
                btCancelar.setEnabled(true);
                btRemover.setEnabled(true);
                btGravar.setEnabled(true);
                gravar = false;
                acessorioEditado = table.getValueAt(nRow, 0).toString();
                LoadCombos();
                
            }

            @Override
            public void mousePressed(MouseEvent me) {
                selected=true;
                nRow = table.getSelectedRow();
                nCol = table.getSelectedColumn();
                idd= table.getValueAt(nRow,0);
                btGravar.setText("Actualizar");           
                btGravar.setIcon(new ImageIcon(getClass().getClassLoader().getResource("Icons/Update.png")));
                btCancelar.setEnabled(true);
                btRemover.setEnabled(true);
                btGravar.setEnabled(true);
                gravar = false;
                LoadCombos();
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
    }
 
     public void LoadData() throws SQLException{
        
        ResultSet rs = null;
        try {
            rs = st.executeQuery("select * from acessorios");
        } catch (SQLException ex) {
            Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
        }
            ResultSetMetaData metaData = null;
        try {
            metaData = rs.getMetaData();
        } catch (SQLException ex) {
            Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
        }

 
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
               
                columnNames.add("Tipo");
                columnNames.add("Marca a usar");
                columnNames.add("Modelo");
                columnNames.add("Quant.");
                columnNames.add("P.Unitario");
                 columnNames.add("ID");
                 
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

    public void Adicionar() throws SQLException{
       String marca,quant = null,preco = null,modelo,tipo = null;
       int id, lastId = 0;
                
       
        try{
            String query ="SELECT * FROM `acessorios` ";
            rs=st.executeQuery(query);
            while(rs.next()){
              
              lastId = rs.getInt("ID");
            }
         }catch(Exception es){
            JOptionPane.showMessageDialog(null, es);
             }
       
       try{
       
            
            if (jcbMarca.getSelectedItem().equals("Universal")){
                jcbModelo.setEnabled(false);
                modelo = "universal";
                marca = "universal";
                
                if(jcbTipo.getSelectedItem().equals("Outro")){
                tipo = novoTipo;
                }else{
                tipo = jcbTipo.getSelectedItem().toString();
                }
               quant = txtQuant.getText();
               preco = txtPreco.getText();
               
            }
            else{
             marca=jcbMarca.getSelectedItem().toString();
            modelo= jcbModelo.getSelectedItem().toString();
            
             if(jcbTipo.getSelectedItem().equals("Outro")){
                tipo = novoTipo;
                }else{
                tipo = jcbTipo.getSelectedItem().toString();
                }
            quant = txtQuant.getText();
            preco = txtPreco.getText();
           
            }
            id = lastId +1;
            
            
            if((!marca.equals("Universal") && ((modelo==null) || (tipo.equals("")) || (tipo.equals("")) || (quant.equals("")) || (preco.equals(""))))){
                 JOptionPane.showMessageDialog(null,"Nenhum campo deve estar vazio","Atencao",JOptionPane.WARNING_MESSAGE);
                adicionado = false;
                 return;
            }
        
            PreparedStatement st =(PreparedStatement) con.prepareStatement("INSERT INTO acessorios (tipo, marca, modelo, quant, preco, ID) VALUES (?,?,?,?,?,?)");
            
            st.setString(1,tipo);
            st.setString(2,marca);
            st.setString(3,modelo);
            st.setInt(4, Integer.parseInt(quant));
            st.setInt(5, Integer.parseInt(preco));
            st.setInt(6, id);
            st.executeUpdate();
            JOptionPane.showMessageDialog(null,"Acessorio registado","Registro",JOptionPane.INFORMATION_MESSAGE);
            adicionado = true;
            acessorioAdicionado = tipo;
            Limpar();
        } catch (SQLException ex) {
            Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
        
       }catch (NumberFormatException ek){
                JOptionPane.showMessageDialog(null,"Verifique os tipos dos dados introduzidos","Erro",JOptionPane.ERROR_MESSAGE);
                return;
            }
         LoadData();
         
     
    }
     
    public void Limpar(){
                    btGravar.setText("Gravar");
                    btGravar.setIcon(new ImageIcon(getClass().getClassLoader().getResource("Icons/Save.png")));
                    btGravar.setEnabled(false);
                    
                    jcbTipo.setSelectedItem("");
                    jcbMarca.setEnabled(true);
                    jcbMarca.setSelectedItem("");
                    jcbModelo.setSelectedItem("");
                    txtPreco.setText("");
                    txtQuant.setText("");
                     btCancelar.setEnabled(false);
                     btRemover.setEnabled(false);
                table.setEnabled(true);
                jcbTipo.requestFocus();
                jcbTipo.setEnabled(true);
                jcbMarca.setEnabled(true);
                jcbModelo.setEnabled(true);
                   // painelButton.remove(btAdd);
                   // painelButton.remove(btCancelar);
    }
    
    public void conexao(){
            try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
            st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao conectar a base de dados");
        }
    }
    
    
    
     public int Remover() throws SQLException{
        
        int result = 0;
         int rowSelected = table.getSelectedRow();
                if(rowSelected == -1){
                    JOptionPane.showMessageDialog(null,"Seleccione o acessorio a ser removido","Atencao",JOptionPane.WARNING_MESSAGE);
                    removido = false;
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
           
          String lastNome = null;
          int id = 0, lastQuant = 0;            
         id =(int)table.getValueAt(table.getSelectedRow(), 5);
              
             PreparedStatement st = null;
                        try {
                            st = con.prepareStatement("DELETE FROM autopolipo.acessorios WHERE acessorios.ID = ?");
                            st.setInt(1, id);
                            result = st.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Acessorio removido com sucesso!","Informacao",JOptionPane.INFORMATION_MESSAGE);
                            removido = true;
                            acessorioRemovido = table.getValueAt(table.getSelectedRow(), 0).toString();
                           
                        } catch (SQLException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        
                        /////////////////////////////////////////
                       /*  st.setInt(1, id);
                        rs=st.executeQuery("select * from acessorios WHERE acessorios.ID = ?");
                        
                        while(rs.next()){
                        lastQuant = rs.getInt("quant");
                        lastNome = rs.getString("tipo");
                        }
                        
                        rs=st.executeQuery("UPDATE `acessorios` SET `quant`='"+(lastQuant-1)+"' WHERE ID='"+id+"'");
                        if((lastQuant-1)<11)
                            JOptionPane.showMessageDialog(null,"Sobraram apenas 10 items do acessorio "+lastNome+"!","Atencao!",JOptionPane.WARNING_MESSAGE);
                      */
                               ///////////////////////////////////////////////
                        
                        
                    }else{
                        removido = false;
                            }
                }
                        LoadData();
                        table.setModel(tableModel);
                      
        return result;
        
    }
    
    
    
    
    
    public JifPecas() throws IOException{
        
        super("Peças");
        File arquivo = new File((new File("log.txt")).getCanonicalPath());
        setContentPane(new JPanel());
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ClassLoader loader = getClass().getClassLoader();
        conexao();
       painel = new JPanel(new MigLayout());
       painel.setBorder(BorderFactory.createTitledBorder("Registo de Acessorios"));
       
       lbTipo = new JLabel("Tipo de acessorio");
       lbModelo = new JLabel("Modelo");
       lbMarca = new JLabel("Marca a aplicar");
       lbQuant = new JLabel("Quantidade");
       lbAdd= new JLabel("Registar usuario");
       lbPreco = new JLabel("Preço unitario");
       lbMtn = new JLabel("Meticais");
        lbRemover = new JLabel("Remover usuario");

        jcbTipo = new JComboBox();
        jcbTipo.setModel(new DefaultComboBoxModel(acessorios));
        jcbTipo.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                gravar=true;
                btCancelar.setEnabled(true);
                 btGravar.setEnabled(true);
                 btRemover.setEnabled(true);
            }

            @Override
            public void mousePressed(MouseEvent me) {
               gravar=true;
               btCancelar.setEnabled(true);
                 btGravar.setEnabled(true);
                 btRemover.setEnabled(true);
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

            @Override
            public void mouseDragged(MouseEvent me) {
                
            }

            @Override
            public void mouseMoved(MouseEvent me) {
               
            }
        });
   
        jcbModelo = new JComboBox();
        jcbModelo.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
          jcbModelo.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                 try{
            String query ="SELECT * FROM `acessorios` WHERE `tipo`='"+jcbTipo.getSelectedItem()+"' AND `marca`='"+jcbMarca.getSelectedItem()+"' AND `modelo`='"+jcbModelo.getSelectedItem()+"'";
            rs=st.executeQuery(query);
            if (rs.first()==true){
            txtPreco.setText(rs.getString("preco"));
            txtQuant.setText(rs.getString("quant"));
            gravar=false;
            btGravar.setText("Actualizar");
            btGravar.setIcon(new ImageIcon(getClass().getClassLoader().getResource("Icons/Update.png")));
            }
            else
            {
                gravar=true;
            btGravar.setText("Gravar");
            btGravar.setIcon(new ImageIcon(getClass().getClassLoader().getResource("Icons/Save.png")));
            txtPreco.setText("");
            txtQuant.setText("");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro"+e);
        }  
                }
            });
            }

            @Override
            public void mousePressed(MouseEvent me) {
         
            
                
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
        jcbMarca = new JComboBox();
        jcbMarca.setModel(new DefaultComboBoxModel(marcas));
        jcbMarca.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
           LoadModels();
            }
        });
        
        
        jcbTipo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(jcbTipo.getSelectedItem().equals("Outro")){
                    novoTipo = JOptionPane.showInputDialog("Introduza o nome do acessorio: ","tipo de acessorio");
                    jcbTipo.setSelectedItem("Outro");
                }
            }
        });
        
        
        txtPreco = new JTextField(20);
        txtQuant = new JTextField(25);  
        
        btGravar = new JButton(new ImageIcon(loader.getResource("Icons/Save.png")));
        btGravar.setText("Gravar");
        btGravar.setEnabled(false);
          // gravar = true;   
        
        btCancelar = new JButton(new ImageIcon(loader.getResource("Icons/Cancel.png")));
      btCancelar.setText("Cancelar");
      btCancelar.setEnabled(false);
       
      btRemover = new JButton(new ImageIcon(loader.getResource("Icons/Remove.png")));
      btRemover.setText("Remover");
      btRemover.setEnabled(false);

                    painel.setBounds(20,10,440,210);
                    painel.add(lbTipo);
                    painel.add(jcbTipo,"growx, span");
                    painel.add(lbMarca);
                    painel.add(jcbMarca,"growx, span");
                    painel.add(lbModelo);
                    painel.add(jcbModelo,"growx, span");
                    painel.add(lbQuant);
                    painel.add(txtQuant," span, wrap");
                    painel.add(lbPreco);
                    painel.add(txtPreco);
                    painel.add(lbMtn," span, wrap");
                    painelButton.add(btGravar,"gapleft 15");
                    painelButton.add(btCancelar);
                    painelButton.add(btRemover);
                    painel.add(painelButton);
                    painelButton.setBounds(20,220,440,50);
                    painelButton.setBorder(BorderFactory.createEtchedBorder());
                    painel.setVisible(true);
                    
//painelButton.add();
                   
                    add(painel);
                    add(painelButton);

                    //painelTable.setBounds(30,280,500,400);

                    painelTable.setVisible(true);    
        
        try {
            LoadData();
        } catch (SQLException ex) {
            Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
        }
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(420, 110));
        sp = new JScrollPane(table);
       painelTable.add(sp);
       painelTable.setBorder(BorderFactory.createTitledBorder("Acessorios Disponiveis"));
       painelTable.setBounds(15,270,445,180);
       add(painelTable);
       
       
       
        
        btGravar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (gravar == false){
                    try {
                            PreparedStatement st =(PreparedStatement) con.prepareStatement("UPDATE `acessorios` SET `quant` = '"+txtQuant.getText()+"',`preco` = '"+txtPreco.getText()+"'  WHERE `ID` = '"+table.getValueAt(table.getSelectedRow(), 5)+"'");
                              st.executeUpdate();
                              JOptionPane.showMessageDialog(null,"Dados actualizados com sucesso");
                              LoadData();
                              
                              try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("Foram alterados dados do acessorio '"+acessorioEditado+"'! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                              
                    } catch (Exception e) {
                    }
                                    
                    try {
                        LoadData();
                    } catch (SQLException ex) {
                        Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                   gravar=false;
                   selected=true;
                   Limpar();
                    Ouve();
               }
                
                
               else
                {
                table.setEnabled(false);
                try {
                    Adicionar();
                } catch (SQLException ex) {
                    Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
                if(adicionado == true){
                        try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("O acessorio '"+acessorioAdicionado+"' foi adicionado! Hora: "+getHoraActual()+" Data: "+getDataActual());
                        bw.newLine();
                        bw.close();
                        fw.close();
                       
                    
                        } catch (IOException ex) {
                            Logger.getLogger(ClienteForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            }
            }
        });
        
        
        btRemover.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Remover();
                } catch (SQLException ex) {
                    Logger.getLogger(JifPecas.class.getName()).log(Level.SEVERE, null, ex);
                }
                Limpar();
                
                if(removido == true){
                    try{
                        if (!arquivo.exists()) {
                           
                            arquivo.createNewFile();
}
                        File[] arquivos = arquivo.listFiles();
                        FileWriter fw = new FileWriter(arquivo, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("O acessorio '"+acessorioRemovido+"' foi apagado! Hora: "+getHoraActual()+" Data: "+getDataActual());
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
            public void actionPerformed(ActionEvent ae) {
                
                gravar=false;
                btCancelar.setEnabled(false);
                btGravar.setEnabled(false);
                Limpar();
               
   
            }
        });
        Ouve();
        
       

         painelButton.setVisible(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        //this.setMinimumSize(new Dimension(620,600));
        this.setPreferredSize(new Dimension(490,490));
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
  
    }
  
    }
    

