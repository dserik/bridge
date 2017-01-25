package environment;

import constants.Keys;

public class TableColumnMod {
    public String tabName;
    public String columnName;
    public String columnType;
    
    public Keys tpKey;
    public FkTableMod fkTableMod = null;
    
    public  TableColumnMod(String tabName, String columnName, String columnType, 
                           Keys key, String fkTabName, String fkIdColumnName){
        this.tabName = tabName;
        this.columnName = columnName;
        this.columnType = columnType;
        this.tpKey = key;
        this.fkTableMod = new FkTableMod(fkTabName, fkIdColumnName);
    }
}
