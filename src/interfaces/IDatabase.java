package interfaces;

import constants.DBMS;
import environment.TableColumnList;
import java.sql.Connection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

public interface IDatabase {
    boolean isFrozen();
    Connection getConn();
    DBMS getDbms();
    String getConnUrl();
    void connectToDb(String fileLocation, String user, String password, DefaultComboBoxModel cbMod);
    /**
     * глюки с полями дата время в SyBase(дублируются)
     * @param tableName
     * @param listMod
     * @return 
     */
    TableColumnList importColumnsFromTable(String tableName, DefaultListModel listMod);
}
