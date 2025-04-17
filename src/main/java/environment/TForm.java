package environment;

import constants.DBMS;
import java.awt.Component;
import java.awt.Container;
import javax.swing.*;

 
public class TForm extends JFrame { 
  public JComboBox cbDBMS[] = new JComboBox[2];
  public JTextField tfFile[] = new JTextField[2];
  public JTextField tfUser[] = new JTextField[2];
  public JTextField tfPassword[] = new JTextField[2];
  public JLabel lblOnOff[] = new JLabel[2];
  public JButton btnOnOff[] = new JButton[2];
  
  public JComboBox cbTables[] = new JComboBox[2];
  public DefaultComboBoxModel cbMdlTables[] = new DefaultComboBoxModel[2];
  public JList lstColumns[] = new JList[2];
  public DefaultListModel lstMdlColumns[] = new DefaultListModel[2];
  public JList lstOperation;
  public DefaultListModel lstMdlOperation = new DefaultListModel();
  public JScrollPane scrPnOperation;
  public JComboBox cbEncoding[] = new JComboBox[2];
  public JButton btnClear;
  public JButton btnImport;
  public JButton btnIntuit;
  public JLabel lblWarning;
  public JCheckBox chbCode;
  public JCheckBox chbTransfer;
  
    
  public TForm() { 
      
  super("Конвертор БД"); 
  try{
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
  }catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
    System.out.println(ex.getMessage());
  }
  setSize(530, 730); 
  setLocation(100, 100); 
  setDefaultCloseOperation( EXIT_ON_CLOSE );
  Container c = getContentPane();
  c.setLayout(null);
  String[] encodingComboBoxValues = {null,"Cp866","Cp1251","UTF-8","KOI8-R","ISO-8859-1","CP1251-k"};

  JPanel firstDBPanel = new JPanel();
  firstDBPanel.setBorder(BorderFactory.createTitledBorder("Исходная база"));
  firstDBPanel.setLayout(null);
  firstDBPanel.setSize(250, 200);
  firstDBPanel.setLocation(10, 10);
  
  int y = 0;
  JLabel firstDBDBMSLabel = new JLabel("СУБД");
  setCompLocation(firstDBDBMSLabel, 10, y+20, 100, 20);
  cbDBMS[0] = new JComboBox(DBMS.values()); 
      cbDBMS[0].setSelectedIndex(0);
  setCompLocation(cbDBMS[0], 120, y+20, 120, 20);     y+=25;
  JLabel firstDBFileLocationLabel = new JLabel("Файл");
  setCompLocation(firstDBFileLocationLabel, 10, y+20, 100, 20);
  tfFile[0] = new JTextField("Tds:192.168.0.8:5000/NK");
  setCompLocation(tfFile[0], 120, y+20, 120, 20);     y+=25;
  JLabel firstDBUserLabel = new JLabel("Пользователь");
  setCompLocation(firstDBUserLabel, 10, y+20, 100, 20);
  tfUser[0] = new JTextField("sa");
  setCompLocation(tfUser[0], 120, y+20, 120, 20);   y+=25;
  JLabel firstDBPasswordLabel = new JLabel("Пароль");
  setCompLocation(firstDBPasswordLabel, 10, y+20, 100, 20);
  tfPassword[0] = new JTextField("");
  setCompLocation(tfPassword[0], 120, y+20, 120, 20);y+=25;
  lblOnOff[0] = new JLabel("<html><font color = red>Отключено");
  setCompLocation(lblOnOff[0], 10, y+20, 100, 20);
  btnOnOff[0] = new JButton("Подключить");
  setCompLocation(btnOnOff[0], 120, y+20, 120, 20); y+=25;
  JLabel firstDBEncodeLabel = new JLabel("Кодировка");
  setCompLocation(firstDBEncodeLabel, 10, y+20, 100, 20);
  cbEncoding[0] = new JComboBox(encodingComboBoxValues);
      cbEncoding[0].setSelectedIndex(5);
      setCompLocation(cbEncoding[0],  120, y+21, 120, 20);
  
  
  firstDBPanel.add(firstDBDBMSLabel);
  firstDBPanel.add(cbDBMS[0]);
  firstDBPanel.add(firstDBFileLocationLabel);
  firstDBPanel.add(tfFile[0]);
  firstDBPanel.add(firstDBUserLabel);
  firstDBPanel.add(tfUser[0]);
  firstDBPanel.add(firstDBPasswordLabel);
  firstDBPanel.add(tfPassword[0]);
  firstDBPanel.add(lblOnOff[0]);
  firstDBPanel.add(btnOnOff[0]);
  firstDBPanel.add(firstDBEncodeLabel);
  firstDBPanel.add(cbEncoding[0]);
  
  ///////////////////////////////////////////////////////////////////////////////////////
  JPanel secondDBPanel = new JPanel();
  secondDBPanel.setBorder(BorderFactory.createTitledBorder("Целевая база"));
  secondDBPanel.setLayout(null);
  secondDBPanel.setSize(250, 200);
  secondDBPanel.setLocation(260, 10);
  
  y = 0;
  JLabel secondDBDBMSLabel = new JLabel("СУБД");
  setCompLocation(secondDBDBMSLabel, 10, y+20, 100, 20);
  cbDBMS[1] = new JComboBox(DBMS.values());
      cbDBMS[1].setSelectedIndex(1);
  setCompLocation(cbDBMS[1], 120, y+20, 120, 20);     y+=25;
  JLabel secondDBFileLocationLabel = new JLabel("Файл");
  setCompLocation(secondDBFileLocationLabel, 10, y+20, 100, 20);
  tfFile[1] = new JTextField("//192.168.0.13:5433/NK");
  setCompLocation(tfFile[1], 120, y+20, 120, 20);     y+=25;
  JLabel secondDBUserLabel = new JLabel("Пользователь");
  setCompLocation(secondDBUserLabel, 10, y+20, 100, 20);
  tfUser[1] = new JTextField("postgres");
  setCompLocation(tfUser[1], 120, y+20, 120, 20);   y+=25;
  JLabel secondDBPasswordLabel = new JLabel("Пароль");
  setCompLocation(secondDBPasswordLabel, 10, y+20, 100, 20);
  tfPassword[1] = new JTextField("smartdoc");
  setCompLocation(tfPassword[1], 120, y+20, 120, 20);y+=25;
  lblOnOff[1] = new JLabel("<html><font color = red>Отключено");
  setCompLocation(lblOnOff[1], 10, y+20, 100, 20);
  btnOnOff[1] = new JButton("Подключить");
  setCompLocation(btnOnOff[1], 120, y+20, 120, 20); y+=25;
  JLabel secondDBEncodeLabel = new JLabel("Кодировка");
  setCompLocation(secondDBEncodeLabel, 10, y+20, 100, 20);
  cbEncoding[1] = new JComboBox(encodingComboBoxValues);
      cbEncoding[1].setSelectedIndex(4);
      setCompLocation(cbEncoding[1],  120, y+21, 120, 20);
  
  secondDBPanel.add(secondDBDBMSLabel);
  secondDBPanel.add(cbDBMS[1]);
  secondDBPanel.add(secondDBFileLocationLabel);
  secondDBPanel.add(tfFile[1]);
  secondDBPanel.add(secondDBUserLabel);
  secondDBPanel.add(tfUser[1]);
  secondDBPanel.add(secondDBPasswordLabel);
  secondDBPanel.add(tfPassword[1]);
  secondDBPanel.add(lblOnOff[1]);
  secondDBPanel.add(btnOnOff[1]);
  secondDBPanel.add(secondDBEncodeLabel);
  secondDBPanel.add(cbEncoding[1]);
  
  /////////////////////////////////////////////////////////////////////////////////
  JPanel workPanel = new JPanel();
  workPanel.setBorder(BorderFactory.createTitledBorder("Панель операции над таблицами"));
  workPanel.setLayout(null);
  workPanel.setSize(500, 460);
  workPanel.setLocation(10, 220);
  
  int x = 0;
  JLabel workPanelTabels1Label = new JLabel("Таблицы");
  setCompLocation(workPanelTabels1Label, x+10, 20, 100, 20);
  cbMdlTables[0] = new DefaultComboBoxModel();
  cbTables[0] = new JComboBox(cbMdlTables[0]);
  setCompLocation(cbTables[0], x+10, 40, 230, 20);
  JLabel workPanelColumns1Label = new JLabel("Поля");
  setCompLocation(workPanelColumns1Label, x+10, 70, 100, 20);
  lstMdlColumns[0] = new DefaultListModel();
  lstColumns[0] = new JList(lstMdlColumns[0]); 
  setCompLocation(lstColumns[0], 0, 0, 230, 150);
      JScrollPane workPanelColumns1ListScrollPane = new JScrollPane(lstColumns[0]);
      workPanelColumns1ListScrollPane.setBorder(BorderFactory.createEtchedBorder());
      setCompLocation(workPanelColumns1ListScrollPane,x+10, 90, 230, 150);
  
  x+=250;
  JLabel workPanelTabels2Label = new JLabel("Таблицы");
  setCompLocation(workPanelTabels2Label, x+10, 20, 100, 20);
  cbMdlTables[1] = new DefaultComboBoxModel();
  cbTables[1] = new JComboBox(cbMdlTables[1]);
  setCompLocation(cbTables[1], x+10, 40, 230, 20);
  JLabel workPanelColumns2Label = new JLabel("Поля");
  setCompLocation(workPanelColumns2Label, x+10, 70, 100, 20);
  lstMdlColumns[1] = new DefaultListModel();
  lstColumns[1] = new JList(lstMdlColumns[1]); 
  setCompLocation(lstColumns[1], 0, 0, 230, 150);
      JScrollPane workPanelColumns2ListScrollPane = new JScrollPane(lstColumns[1]);
      workPanelColumns2ListScrollPane.setBorder(BorderFactory.createEtchedBorder());
      setCompLocation(workPanelColumns2ListScrollPane,x+10, 90, 230, 150);
  
  JLabel workPanelOperationLabel = new JLabel("Текущие операции");
  setCompLocation(workPanelOperationLabel, 10, 250, 150, 20);
  lstOperation = new JList(lstMdlOperation);
  setCompLocation(lstOperation, 0, 0, 470, 100);
      scrPnOperation = new JScrollPane(lstOperation);
      scrPnOperation.setBorder(BorderFactory.createEtchedBorder());
      setCompLocation(scrPnOperation,10, 270, 480, 100);
  
  lblWarning = new JLabel("");
      setCompLocation(lblWarning, 30, 370, 440, 20);
  chbCode = new JCheckBox("Сформировать код");
      setCompLocation(chbCode, 10, 395, 200, 20);
  chbTransfer = new JCheckBox("Выполнить запрос");
      setCompLocation(chbTransfer, 10, 420, 200, 20);
  btnClear = new JButton("Очистить");
  setCompLocation(btnClear, 305, 400, 93, 20);
  btnImport = new JButton("Импорт");
  setCompLocation(btnImport, 400, 400, 91, 20);
  btnIntuit = new JButton("Интуитивный перенос БД");
  setCompLocation(btnIntuit, 305, 425, 186, 20);
  
  workPanel.add(workPanelTabels1Label);
  workPanel.add(cbTables[0]);
  workPanel.add(workPanelColumns1Label);
  workPanel.add(workPanelColumns1ListScrollPane);
  workPanel.add(workPanelTabels2Label);
  workPanel.add(cbTables[1]);
  workPanel.add(workPanelColumns2Label);
  workPanel.add(workPanelColumns2ListScrollPane);
  workPanel.add(workPanelOperationLabel);
  workPanel.add(scrPnOperation);
  workPanel.add(lblWarning);
  workPanel.add(chbCode);
  workPanel.add(chbTransfer);
  workPanel.add(btnClear);
  workPanel.add(btnImport);
  workPanel.add(btnIntuit);
  
  
  ///////////////////////////////////////////////////////////////////////
  
  c.add(firstDBPanel);
  c.add(secondDBPanel);
  c.add(workPanel);
  setVisible(true); 
 } 
  public static void setCompLocation(Component comp, int x, int y, int width, int height){
      comp.setLocation(x, y);
      comp.setSize(width, height);
  }
}
