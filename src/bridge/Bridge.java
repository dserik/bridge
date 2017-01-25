package bridge;

import constants.DBMS;
import constants.DbTypes;
import constants.Keys;
import databases.H2;
import databases.Postgresql;
import databases.Sybase;
import environment.ColumnListCellRenderer;
import environment.ImageListCellRenderer;
import environment.ImageListElement;
import environment.TForm;
import environment.TableColumnList;
import environment.TableColumnMod;
import interfaces.IDatabase;
import utils.JCode;
import utils.Utils;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bridge {
  
  private static TForm eForm; 
  public static IDatabase db[] = new IDatabase[2];
  private static ArrayList<TableColumnMod[]> tabColumnsToExport ;
  private static TableColumnList tableColumns[] = new TableColumnList[2];
  public static String log = "";
  
  
  private static void init(){
      eForm = new TForm(); 
      DbTypes.initVars();
      eForm.lstOperation.setCellRenderer(new ImageListCellRenderer());
      eForm.lstColumns[0].setCellRenderer(new ColumnListCellRenderer());
      eForm.lstColumns[1].setCellRenderer(new ColumnListCellRenderer());
  }
  
  private static void resetAllVariables(){
      resetVariables();
      eForm.lstColumns[0].setSelectedIndex(-1); 
      eForm.lstColumns[1].setSelectedIndex(-1);
      eForm.lstMdlColumns[0].clear();
      eForm.lstMdlColumns[1].clear();  
  }
      
  private static void resetVariables(){
      eForm.lstOperation.setSelectedIndex(-1); 
      tabColumnsToExport = null;
      eForm.lstMdlOperation.clear(); 
  }
  
  private static void dBAreaComboBoxChanged(DBMS dbms, int i){
      switch(dbms){
      case SYBASE:
          eForm.tfFile[i].setText("Tds:192.168.0.8:5000/NK");
          eForm.tfUser[i].setText("sa");
          eForm.tfPassword[i].setText("");
          return;
      case POSTGRESQL:
          eForm.tfFile[i].setText("//192.168.0.13:5433/NK");
          eForm.tfUser[i].setText("postgres");
          eForm.tfPassword[i].setText("smartdoc");
          return;
      case H2:
          eForm.tfFile[i].setText("Avanti");
          eForm.tfUser[i].setText("");
          eForm.tfPassword[i].setText("");
      }
  }
  
  private static void dbConnect(DBMS dbms, int i){
      switch(dbms){
          case SYBASE: 
              db[i] = new Sybase(); 
              break;
          case POSTGRESQL:  
              db[i] = new Postgresql();
              break;
          case H2: 
              db[i] = new H2();
              break;
      }
      
      db[i].connectToDb(eForm.tfFile[i].getText(), eForm.tfUser[i].getText(), 
                        eForm.tfPassword[i].getText(), eForm.cbMdlTables[i]);
      eForm.lblOnOff[i].setText("<html><font color = green>Подключено");
  }
  
  private static void getColumns(int i){
      tableColumns[i] = db[i].importColumnsFromTable(eForm.cbTables[i].getSelectedItem().toString(),
                                                     eForm.lstMdlColumns[i]);
      resetVariables();
      tabColumnsToExport = new ArrayList<>();
  }
  
  private static void getFKeyData(int i){
      if (tableColumns[i].get(eForm.lstColumns[i].getSelectedIndex()).tpKey==Keys.FOREIGN){
          eForm.lblWarning.setText("Родительская колонка -> "+
                   tableColumns[i].get(eForm.lstColumns[i].getSelectedIndex()).fkTableMod.fkTabName+"."+
                   tableColumns[i].get(eForm.lstColumns[i].getSelectedIndex()).fkTableMod.fkIdColumnName);
      }else{
          eForm.lblWarning.setText("");
      }
  }
  
  private static void transferColumn(){
      TableColumnMod[] tbCols = new TableColumnMod[2];
      tbCols[0] = tableColumns[0].get(eForm.lstColumns[0].getSelectedIndex());
      tbCols[1] = tableColumns[1].get(eForm.lstColumns[1].getSelectedIndex());
      tabColumnsToExport.add(tbCols); 
      
      eForm.lstMdlOperation.addElement(
           new ImageListElement(
               tableColumns[0].get(eForm.lstColumns[0].getSelectedIndex()).columnType,
               tableColumns[1].get(eForm.lstColumns[1].getSelectedIndex()).columnType,
               tbCols[0].tabName+"."+tbCols[0].columnName+" --> "+tbCols[1].tabName+"."+tbCols[1].columnName
           )
      );
      eForm.lstOperation.setSelectedIndex(eForm.lstMdlOperation.getSize()-1);
      eForm.scrPnOperation.getHorizontalScrollBar().setValue(eForm.scrPnOperation.getHorizontalScrollBar().getSize().width);
  }
  
  public static void exportTable(boolean createCode, boolean execute){
      if(createCode){
          JCode.add(tabColumnsToExport);
      }
      
      if(execute){
          ResultSet rs = null;
          Statement stmt0 = null, stmt1 = null;
          try {
              stmt0 = db[0].getConn().createStatement();
              stmt1 = db[1].getConn().createStatement();
          } catch (SQLException ex) {
              Logger.getLogger(Bridge.class.getName()).log(Level.SEVERE, null, ex);
          }
          
          try {
              rs = stmt0.executeQuery(JCode.crtSelect(tabColumnsToExport));
          } catch (SQLException e) {
              e.printStackTrace();
          }
        
          String query = "INSERT INTO "+tabColumnsToExport.get(0)[1].tabName + "(";
          int j=0;
          for(int i=0;i<tabColumnsToExport.size()-1;i++){
              query = query + tabColumnsToExport.get(i)[1].columnName + ", ";
              j++;
          }
        
          query = query + tabColumnsToExport.get(j)[1].columnName+") " + "values (";
        
          String insQuery;
          Utils.encoding1 = (String) eForm.cbEncoding[0].getSelectedItem();
          Utils.encoding2 = (String) eForm.cbEncoding[1].getSelectedItem();
        
          try {
              while(rs.next()){
                  insQuery = "";
                  j = 0;
                  for(int i=0;i<tabColumnsToExport.size()-1;i++){
                      if(DbTypes.numeric.contains(tabColumnsToExport.get(i)[1].columnType.toLowerCase())){
                          insQuery = insQuery + Utils.intToInsert(rs.getString(i+1)) + ", ";
                      }else{
                          insQuery = insQuery + Utils.strToInsert(rs.getString(i+1)) + ", ";
                      }
                      j++;
                  }
                
                  if(DbTypes.numeric.contains(tabColumnsToExport.get(j)[1].columnType.toLowerCase())){
                      insQuery = insQuery + Utils.intToInsert(rs.getString(j+1)) + ")";
                  }else{
                      insQuery = insQuery + Utils.strToInsert(rs.getString(j+1)) + ")";
                  }
                  try{
                      stmt1.executeQuery(query+insQuery);
                  } catch (SQLException e) {
                      e.printStackTrace();
                  } 
              }
          }catch (SQLException e) {
              e.printStackTrace();
          }
      }
  }
  
  public static void main(String[] args){ 
      
      init();
      
      eForm.cbDBMS[0].addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0){
              dBAreaComboBoxChanged(DBMS.valueOf(eForm.cbDBMS[0].getSelectedItem().toString()),0);
          }
      });
      
      eForm.cbDBMS[1].addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0){
              dBAreaComboBoxChanged(DBMS.valueOf(eForm.cbDBMS[1].getSelectedItem().toString()),1);
          }
      });
      
      eForm.btnOnOff[0].addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0){
              dbConnect(DBMS.valueOf(eForm.cbDBMS[0].getSelectedItem().toString()),0);
          }
      });
      
      eForm.btnOnOff[1].addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0){
              dbConnect(DBMS.valueOf(eForm.cbDBMS[1].getSelectedItem().toString()),1);
          }
      });
      
      eForm.cbTables[0].addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0){
              if(!db[0].isFrozen()) getColumns(0);
          }
      });
      
      eForm.cbTables[1].addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0){
              if(!db[1].isFrozen()) getColumns(1);
          }
      });
      
      eForm.lstColumns[0].addListSelectionListener(new ListSelectionListener() {
          @Override
          public void valueChanged(ListSelectionEvent arg0){
              if (arg0.getValueIsAdjusting ()||eForm.lstMdlColumns[0].isEmpty()) return;
              getFKeyData(0);
          }
      });
      
      eForm.lstColumns[1].addListSelectionListener(new ListSelectionListener() {
          @Override
          public void valueChanged(ListSelectionEvent arg0){
              if (arg0.getValueIsAdjusting ()||eForm.lstMdlColumns[1].isEmpty()) return;
              getFKeyData(1);
              if(!eForm.lstMdlColumns[0].isEmpty()) transferColumn();
          }
      });
      
      eForm.lstOperation.addListSelectionListener(new ListSelectionListener() {
          @Override
          public void valueChanged(ListSelectionEvent arg0){
              if (arg0.getValueIsAdjusting ()||eForm.lstMdlOperation.isEmpty()) return;
              eForm.lblWarning.setText(((ImageListElement)eForm.lstOperation.getSelectedValue()).getWarning());
          }
      });
      
      eForm.btnImport.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0){
              if(tabColumnsToExport.size()<1) return;
              exportTable(eForm.chbCode.isSelected(), eForm.chbTransfer.isSelected());
              resetAllVariables();
          }
      });
      
      eForm.btnIntuit.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0){
              intuitTransfer();
              JCode.create("Convert", true);
//              createSimilarDB();
          }
      });
  } 
  
  public static void createSimilarDB(){
      String code = "";
      for(int i = 0;i < eForm.cbMdlTables[0].getSize();i++){
          eForm.cbTables[0].setSelectedIndex(i);
          code += "CREATE TABLE " + eForm.cbMdlTables[0].getElementAt(i).toString().toLowerCase() + "(";
          for(int j = 0; j < tableColumns[0].size(); j++){
              code += "\n  " + tableColumns[0].get(j).columnName.toLowerCase() + " " + DbTypes.sybPostgreSimilarTypes.get(tableColumns[0].get(j).columnType) + ",";
          }
          code = code.substring(0, code.length() - 1) + "\n);\n\n";
      }
      File flt = new File("CreateDBQuery.txt");
      PrintWriter out = null;
      try {
          out = new PrintWriter(new BufferedWriter(new FileWriter(flt)));
      } catch (IOException e) {
          e.printStackTrace();
      }
      out.print(code);
      out.flush();
      out.close();
  }
  
  public static void intuitTransfer(){
      boolean hasMV;
      String missingValues = "";
      for(int i = 0;i < eForm.cbMdlTables[0].getSize();i++){
          hasMV = false;
          eForm.cbTables[0].setSelectedIndex(i);
          boolean exRec = false;
          for(int h = 0; h < eForm.cbTables[1].getItemCount();h++){
              if(eForm.cbTables[1].getItemAt(h).toString().toLowerCase().trim().equals(eForm.cbTables[0].getSelectedItem().toString().toLowerCase().trim())){
                  exRec = true;
                  break;
              }
          }
          if(!exRec){
              missingValues+="\tтаблица " + db[1].getDbms() + "."+eForm.cbTables[0].getSelectedItem().toString().toLowerCase()+" отсутствует\n";
              continue;
          }
          tableColumns[1] = db[1].importColumnsFromTable(eForm.cbTables[0].getSelectedItem().toString().toUpperCase(),
                                                         eForm.lstMdlColumns[1]);
          ArrayList<String> sss = new ArrayList<>();
          for(int j = 0; j < tableColumns[0].size();j++){
              
              //temporary block//
              if(tableColumns[0].get(j).columnName.toLowerCase().equals("date_update"))
                  continue;
              ///////////////////
              
              boolean exist = false;
              for(int k = 0; k < tableColumns[1].size(); k++){
                  if(tableColumns[1].get(k).columnName.toLowerCase().trim().equals(tableColumns[0].get(j).columnName.toLowerCase().trim())){
                      exist = true;
                      sss.add(tableColumns[1].get(k).columnName);
                      
                      TableColumnMod[] tbCols = new TableColumnMod[2];
                      tbCols[0] = tableColumns[0].get(j);
                      tbCols[1] = tableColumns[1].get(k);
                      
                      tabColumnsToExport.add(tbCols);  
                      break;
                  }
              }
              if(exist==false){
                  hasMV = true;
                  missingValues += db[0].getDbms() + "." + tableColumns[0].get(j).tabName+"."+tableColumns[0].get(j).columnName+"\n";
              }
          }
          
          for(int j = 0; j < tableColumns[1].size();j++){
              if(!sss.contains(tableColumns[1].get(j).columnName)){
                  hasMV = true;
                  missingValues += "\t" + db[1].getDbms() + "."+tableColumns[1].get(j).tabName+"."+tableColumns[1].get(j).columnName+"\n";
              }
          }
          eForm.btnImport.doClick();
          if(hasMV)
              missingValues+="\n";
      }
      File flt = new File("Missing data.txt");
      PrintWriter out = null;
      try {
          out = new PrintWriter(
                  new BufferedWriter(
                    new FileWriter(flt)));
      } catch (IOException e) {
          e.printStackTrace();
      }
      out.print(missingValues);
      out.flush();
      out.close();
  }
} 