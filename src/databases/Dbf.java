package databases;

import constants.DBMS;
import environment.TableColumnList;
import interfaces.IDatabase;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

public class Dbf implements IDatabase{
    
    private Connection conn = null;
    private Statement stmt = null;
    private boolean cbFreezed;
    private String connUrl = "";

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
        return DBMS.DBF;
    }

    @Override
    public String getConnUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connectToDb(String fileLocation, String user, String password, DefaultComboBoxModel cbMod) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TableColumnList importColumnsFromTable(String tableName, DefaultListModel listMod) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
