package environment;

public class FkTableMod{
    public String fkTabName;
    public String fkIdColumnName;
    
    public FkTableMod(String fkTabName, String fkIdColumnName){
        this.fkTabName = fkTabName;
        this.fkIdColumnName = fkIdColumnName;
    }
}
