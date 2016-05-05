

import java.sql.*;
import javax.swing.JOptionPane;


public class DBConnect {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    
    public static void DBConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro: "+e);
        }
    }
    
    public void SetData(){
        DBConnect();
        String nome;int senha;
        try{
            nome=JOptionPane.showInputDialog("NOme");
            senha= Integer.parseInt(JOptionPane.showInputDialog("Senha"));
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste","root","");
            PreparedStatement st =(PreparedStatement) con.prepareStatement("INSERT INTO usuario (nome, senha) VALUES (?,?)");
            st.setString(1, nome);
            st.setInt(2, senha);
            st.executeUpdate();
            con.close();
            
            /*con.prepareStatement();
            PreparedStatement st = new PreparedStatement()
            st.setString();
            
            String query ="INSERT INTO `teste`.`usuario` (`nome`, `senha`) VALUES ('rozay', '1324');";
            rs=st.executeQuery(query);
            while(rs.next()){
                String nome = rs.getString("nome");
                String senha = rs.getString("senha");
                JOptionPane.showMessageDialog(null,"Nomes "+nome+"\n"+"Senha "+senha);*/
            //}
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
}
