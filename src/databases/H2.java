package databases;

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

public class H2 implements IDatabase {

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
        return DBMS.H2;
    }
    
    @Override
    public void connectToDb(String fileLocation, String user, String password,DefaultComboBoxModel cbMod){
        cbFreezed = true;
        Statement stmt = null;
        try {
            Class.forName("org.h2.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        if(fileLocation.toLowerCase().endsWith(".h2.db")){
            fileLocation = fileLocation.substring(0, fileLocation.length()-6);
        }
        
        try {
            conn = DriverManager.getConnection("jdbc:h2:"+fileLocation+";IFEXISTS=true;AUTO_SERVER=true",
                                               user, password);
            stmt = conn.createStatement();
            connUrl = "\"jdbc:h2:"+fileLocation + ";IFEXISTS=true;AUTO_SERVER=true\", \"" + user + "\", \"" + password + "\"";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery("select TABLE_NAME from INFORMATION_SCHEMA.TABLES " +
                                          "where TABLE_SCHEMA = 'PUBLIC' order by TABLE_NAME");
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        
        cbMod.removeAllElements();
        try {
            while(resultSet.next()){
                try {
                    cbMod.addElement(resultSet.getString(1));
                } catch (SQLException e1) {}
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        
        cbFreezed = false;
        try {
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(H2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public TableColumnList importColumnsFromTable(String tableName, DefaultListModel listMod){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(H2.class.getName()).log(Level.SEVERE, null, ex);
        }
        TableColumnList tableColumns;
        ResultSet resultSet = null;
        String query = "select cols.COLUMN_NAME, cols.TYPE_NAME, " +
                              "case constraints.CONSTRAINT_TYPE " +
                                  "when 'REFERENTIAL' then 'FOREIGN' " +
                                  "when 'PRIMARY KEY' then 'PRIMARY' "+
                                  "else 'KEYLESS' "+
                              "end as CONSTRAINT_TYPE, " +
                              "fkTable.PKTABLE_NAME, fkTable.PKCOLUMN_NAME "+
                       "from " +
                             "(select COLUMN_NAME, TYPE_NAME " +
                              "from INFORMATION_SCHEMA.COLUMNS "+
                              "where TABLE_NAME = '"+tableName+"' and "+
                              "TABLE_SCHEMA = 'PUBLIC') as cols " +
                       "left join "+
                             "(select COLUMN_LIST, CONSTRAINT_TYPE "+
                              "from INFORMATION_SCHEMA.CONSTRAINTS "+
                              "where TABLE_NAME = '"+tableName+"' and "+
                                    "CONSTRAINT_TYPE in ('REFERENTIAL','PRIMARY KEY')) as constraints " +
                       "left join " +
                             "(select PKTABLE_NAME, PKCOLUMN_NAME, FKCOLUMN_NAME " +
                              "from INFORMATION_SCHEMA.CROSS_REFERENCES " +
                              "where FKTABLE_NAME = '"+tableName+"') as fkTable "+
                       "on   constraints.COLUMN_LIST = fkTable.FKCOLUMN_NAME " +
                       "on   cols.COLUMN_NAME = constraints.COLUMN_LIST";
        
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
                                "<font color = gray>("+resultSet.getString(2)+")", Keys.valueOf(resultSet.getString(3))));
                        tableColumns.add(new TableColumnMod(tableName, resultSet.getString(1), 
                                                            resultSet.getString(2), Keys.valueOf(resultSet.getString(3)),
                                                            resultSet.getString(4), resultSet.getString(5)));
                    } catch (SQLException e1) {e1.printStackTrace();} 
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        try {
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(H2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tableColumns;
    }
}
