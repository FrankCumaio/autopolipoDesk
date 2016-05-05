

import java.sql.*;
import javax.swing.JOptionPane;


public class DBR {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    
    public DBR(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste","root","");
            st=con.createStatement();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro: "+e);
        }
    }
    
    public void GetData(){
        try{
            String query ="Select * from usuario";
            rs=st.executeQuery(query);
            while(rs.next()){
                String nome = rs.getString("nome");
                String senha = rs.getString("senha");
                JOptionPane.showMessageDialog(null,"Nomes "+nome+"\n"+"Senha "+senha);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
}
