package databases;

import com.sybase.jdbc3.jdbc.SybDriver;
import constants.DBMS;
import constants.Keys;
import environment.ColumnListElement;
import environment.TableColumnList;
import environment.TableColumnMod;
import interfaces.IDatabase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

public class Sybase implements IDatabase{

    private Connection conn = null;
    private boolean cbFreezed;
    private String connUrl = "";
    
    @Override
    public String getConnUrl(){
        return this.connUrl;
    }
    
    @Override
    public boolean isFrozen() {
        return this.cbFreezed;
    }
    
    @Override
    public Connection getConn() {
        return this.conn;
    }
    
    @Override
    public DBMS getDbms() {
        return DBMS.SYBASE;
    }
    
    @Override
    public void connectToDb(String fileLocation, String user, String password,DefaultComboBoxModel cbMod){
        Statement stmt = null;
        cbFreezed = true;
        SybDriver sybDriver;
        @SuppressWarnings("rawtypes")
        Class c;
        
        try {
            c = Class.forName("com.sybase.jdbc3.jdbc.SybDriver");
            sybDriver = (SybDriver)c.newInstance();              
            DriverManager.registerDriver(sybDriver);
            conn = DriverManager.getConnection("jdbc:sybase:"+fileLocation, user, password);
            connUrl = 
"            @SuppressWarnings(\"rawtypes\")                                            \n" + 
"            Class c = Class.forName(\"com.sybase.jdbc3.jdbc.SybDriver\");              \n" +
"            SybDriver sybDriver = (SybDriver)c.newInstance();                          \n" +
"            DriverManager.registerDriver(sybDriver);                                   \n" + 
"            DB_CONNECTION = DriverManager.getConnection(\"jdbc:sybase:" + fileLocation + "\", \"" + user + "\", \"" + password + "\");\n";
            stmt = (Statement) conn.createStatement();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        

        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery("select * from sysobjects where type = 'U' ");
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        
        cbMod.removeAllElements();
        try {
            while(resultSet.next()){
                try {
                    cbMod.addElement(resultSet.getString(1));
                } catch (SQLException e1) {
                    Logger.getLogger(Sybase.class.getName()).log(Level.SEVERE, null, e1);
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        cbFreezed = false;
        try {
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Sybase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public TableColumnList importColumnsFromTable(String tableName,DefaultListModel listMod){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Sybase.class.getName()).log(Level.SEVERE, null, ex);
        }
        TableColumnList tableColumns;
        ResultSet resultSet = null;
        String query = "select " +
                           "fld.name,t.name "+
                       "from " +
                           "systypes t, syscolumns fld, sysobjects tab "+
                       "where " +
                           "tab.id=fld.id and " +
                           "tab.name = '"+tableName+"' and " +
                           "t.type=fld.type and not " +
                           "t.name = 'sysname' and not " +
                           "t.name = 'varchar' and not " + 
                           "t.name = 'char'";
        try {
            resultSet = stmt.executeQuery(query);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        
        tableColumns = new TableColumnList();
        if(tableName.length()!=0&&resultSet!=null){
            listMod.clear();
            try {
                while(resultSet.next()){
                    try{
                        listMod.addElement(new ColumnListElement("<html>"+resultSet.getString(1)+
                                "<font color = gray>("+resultSet.getString(2)+")", Keys.KEYLESS));
                        tableColumns.add(new TableColumnMod(tableName, resultSet.getString(1), 
                                                            resultSet.getString(2), Keys.KEYLESS,
                                                            null, null));
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } 
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return tableColumns;
    }
}
