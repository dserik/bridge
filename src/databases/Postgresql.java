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

public class Postgresql implements IDatabase{
    
    private String connUrl = "";
    private Connection conn = null;
    private boolean cbFreezed;
    
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
        return DBMS.POSTGRESQL;
    }
    
    @Override
    public void connectToDb(String fileLocation, String user, String password,DefaultComboBoxModel cbMod){
        cbFreezed = true;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql:"+fileLocation,user,password);
            connUrl = 
"            DB_CONNECTION = DriverManager.getConnection(\"jdbc:postgresql:" + fileLocation + "\", \"" + user + "\", \"" + password + "\");\n";
            stmt = (Statement) conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
      
        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery(" SELECT  t.table_name " +
                    "FROM information_schema.TABLES t " +
                    "WHERE t.table_schema::text = 'public'::text AND " +
                    "t.table_catalog::name = current_database() AND " +
                    "t.table_type::text = 'BASE TABLE'::text AND " +
                    " NOT \"substring\"(t.table_name::text, 1, 1) = '_'::text " +
                    "order by t.table_name");
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
            Logger.getLogger(Postgresql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public TableColumnList importColumnsFromTable(String tableName,DefaultListModel listMod) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Postgresql.class.getName()).log(Level.SEVERE, null, ex);
        }
        TableColumnList tableColumns;
        ResultSet resultSet = null;

        String query =  "SELECT  tabcolumns.attname, tabcolumns.atttype, " + 
                                "case keys.contype " +
                                "    when 'p' then 'PRIMARY' " +
                                "    when 'f' then 'FOREIGN' " +
                                "    else 'KEYLESS' " +
                                "end, " +
                                "fkreferences.foreign_table,  fkreferences.foreign_columns " +
                        "FROM  " +
                            "((SELECT  " +
                             "a.attname as attname, format_type(a.atttypid, a.atttypmod) as atttype  " +
                             "FROM  " +
                             "pg_class c, pg_attribute a  " +
                             "WHERE  " +
                             "c.relname = '"+tableName.toLowerCase()+"' AND  " +
                             "a.attnum > 0 AND  " +
                             "a.attrelid = c.oid AND NOT  " +
                             "a.attisdropped) as tabcolumns  " +
                        "LEFT JOIN  " +
                            "(SELECT  " +
                             "contype, attname  " +
                             "FROM  " +
                             "pg_constraint  " +
                             "JOIN pg_attribute ON  " +
                             "attrelid = conrelid AND  " +
                             "attnum = any(conkey)  " +
                             "WHERE  " +
                             "contype in ('p', 'f') AND  " +
                             "conrelid = '"+tableName.toLowerCase()+"'::regclass::oid  " +
                             "ORDER BY 1, attnum) as keys  " +
                        "ON tabcolumns.attname = keys.attname)  " +
                        "LEFT JOIN  " +
                        
                        "(SELECT a.attname as columns, " +
                               "confrelid::regclass as foreign_table, " +
                               "af.attname as foreign_columns " +
                          "FROM pg_attribute AS af, " +
                               "pg_attribute AS a, " +
                               "( SELECT conrelid, " +
                                        "confrelid, " +
                                        "conkey[i] AS conkey, " +
                                        "confkey[i] as confkey " +
                                   "FROM ( SELECT conrelid, " +
                                                 "confrelid,  " +
                                                 "conkey,  " +
                                                 "confkey,  " +
                                                 "generate_series(1, array_upper(conkey, 1)) AS i " +
                                            "FROM pg_constraint " +
                                            "WHERE contype = 'f' " +
                                 ") AS ss " +
                              ") AS ss2 " +
                         "WHERE af.attnum = confkey " +
                           "AND conrelid::regclass = '"+tableName.toLowerCase()+"'::regclass::oid " +
                           "AND af.attrelid = confrelid " +
                           "AND a.attnum = conkey " +
                           "AND a.attrelid = conrelid) as fkreferences  " +
                           "on tabcolumns.attname = fkreferences.columns " +
                           "order by tabcolumns.attname";
        
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
                    try {
                        listMod.addElement(new ColumnListElement("<html>"+resultSet.getString(1)+
                                "<font color = gray>("+resultSet.getString(2)+")",Keys.valueOf(resultSet.getString(3))));
                        tableColumns.add(new TableColumnMod(tableName, resultSet.getString(1), 
                                                            resultSet.getString(2), Keys.valueOf(resultSet.getString(3)),
                                                            resultSet.getString(4), resultSet.getString(5)));
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        try {
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Postgresql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tableColumns;
    }
}
