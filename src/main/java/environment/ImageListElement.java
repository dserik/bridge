package environment;

import bridge.Bridge;
import utils.Z;

import javax.swing.*;
 
public class ImageListElement {

    public static final Icon fineIcon = new ImageIcon("images/fine.png");
    public static final Icon warningIcon = new ImageIcon("images/warning.png");
    public static final Icon errorIcon = new ImageIcon("images/error.png");
    public static final Icon notFoundIcon = new ImageIcon("images/not_found.png");

    public Icon icon;
    public String text;
    public String warning;

  public ImageListElement(String tab1ColType, String tab2ColType,  String text) { 
      
      this.text = text; 
      switch(Z.compTypes(tab1ColType, tab2ColType)){
          case FINE:
              this.warning = "<html><font color = green>Без предупреждений.";
              this.icon = fineIcon; 
              return;
          case POSSIBLE:
              this.warning = "<html><font color = #D2691E>Предупреждение! Разные типы, но перевод возможен.";
              this.icon = warningIcon;
              return;
          case IMPOSSIBLE:
              this.warning = "<html><font color = red>Внимание! Типы полей не несопоставимы, может возникнуть ошибка!";
              this.icon = errorIcon;
              return;
          case NOT_FOUND:
              this.warning = "<html><font color = red>Внимание! Типы неопределены!";
              this.icon = notFoundIcon;
              Bridge.log += "Типы не найдены: " + tab1ColType + ", " + tab2ColType + "\n";
      }
  } 
  
  public String getWarning() { 
      return this.warning; 
  } 

} 