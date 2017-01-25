package environment; 
import constants.Keys; 
import javax.swing.*; 
 
public class ColumnListElement { 

    public Icon icon; 
    public String columnName;
    public boolean enabled;
    
    public ColumnListElement(String colName, Keys key) { 
        this.columnName = colName; 
        this.enabled = true;
        
        switch(key){
            case KEYLESS: this.icon = constraintIcon[0]; break;
            case PRIMARY: this.icon = constraintIcon[1]; break;
            case FOREIGN: this.icon = constraintIcon[2];
        }
    } 
  
    static Icon[] constraintIcon = {
        new ImageIcon("images/none.png"),
        new ImageIcon("images/pkey.png"), 
        new ImageIcon("images/fkey.png") 
    }; 
} 