
package autopolipo1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//import static jdk.nashorn.internal.codegen.Compiler.LOG;

public class GUI extends JPanel {

   

    private final JButton button;
    private final JTable table;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    ResultSet rs = null;
    
    public GUI() throws HeadlessException {

        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        table = new JTable(tableModel);
        button = new JButton("Load Data");
        
       // this.add(button);
        table.setPreferredScrollableViewportSize(new Dimension(558, 170));
        this.add(new JScrollPane(table));
        //this.pack();


        this.setSize(600, 250);
        
        
        
        
        
//        LOG.info("START loadData method");

        button.setEnabled(false);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/autopolipo","root","");
                Statement stmt = conn.createStatement()) {

            rs = stmt.executeQuery("select * from clientes");
            ResultSetMetaData metaData = rs.getMetaData();

           
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
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Exception in Load Data", e);
        }
        button.setEnabled(true);

//        LOG.info("END loadData method");
        this.setVisible(true);
    }
        
        
    }

   