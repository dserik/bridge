package utils;

import bridge.Bridge;
import constants.DbTypes;
import environment.TableColumnMod;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class JCode {
    public static ArrayList<String> mthdLst = new ArrayList<>();
    public static String tablesToTruncateLst = "";
    public static ArrayList<String> mthdRlsLst = new ArrayList<>(); 
    public static String code = "";
    
    public static String crtInsert(ArrayList<TableColumnMod[]> tabColumnsToExport){
        String query = "INSERT INTO "+tabColumnsToExport.get(0)[1].tabName + "(";
        int j=0;
        for(int i=0;i<tabColumnsToExport.size()-1;i++){
            query = query + tabColumnsToExport.get(i)[1].columnName + ", ";
            j++;
        }
        query = query + tabColumnsToExport.get(j)[1].columnName+") values ( ";
        String insQueryCode ="";
        
        j=0;
        for(int i=0;i<tabColumnsToExport.size()-1;i++){
            if(DbTypes.numeric.contains(tabColumnsToExport.get(i)[1].columnType.toLowerCase())){
                insQueryCode+=
"                intToInsert(rs.getString(\""+tabColumnsToExport.get(i)[0].columnName+"\"))+\", \"+        \n";
            }else{
                insQueryCode+=
"                strToInsert(rs.getString(\""+tabColumnsToExport.get(i)[0].columnName+"\"))+\", \"+        \n";
            }
            j++;
        }
        
        if(DbTypes.numeric.contains(tabColumnsToExport.get(j)[1].columnType.toLowerCase())){
            insQueryCode+=
"                intToInsert(rs.getString(\""+tabColumnsToExport.get(j)[0].columnName+"\"))+\") \";                                       \n";
        }else{
            insQueryCode+=
"                strToInsert(rs.getString(\""+tabColumnsToExport.get(j)[0].columnName+"\"))+\") \";                                       \n";
        }
        return "\""+query+"\" + \n"+insQueryCode;
    }
    
    public static String crtSelect(ArrayList<TableColumnMod[]> tabColumnsToExport){
        String query = "SELECT ";
        int j=0;
        for(int i=0;i<tabColumnsToExport.size()-1;i++){
            query = query + tabColumnsToExport.get(i)[0].columnName + ", ";
            j++;
        }
        query = query + tabColumnsToExport.get(j)[0].columnName;
        query = query + " FROM "+tabColumnsToExport.get(0)[0].tabName;
        return query;
    }
    
    public static void add(ArrayList<TableColumnMod[]> tabColumnsToExport){
        mthdLst.add("load_" + tabColumnsToExport.get(0)[0].tabName.toLowerCase()+"()");
        
        tablesToTruncateLst += 
"        truncate(\"" + tabColumnsToExport.get(0)[0].tabName.toLowerCase() + "\");\n";
        
        mthdRlsLst.add( 
"    public static void load_" + tabColumnsToExport.get(0)[0].tabName.toLowerCase()+"(){        \n" +
"        nativeOutput(\"loading table \" + tabColumnsToExport.get(0)[0].tabName.toLowerCase() + \"...\"); \n" + 
"        Statement stmt1 = null, stmt2 = null;                                                  \n\n" +
                
"        String query = \""+crtSelect(tabColumnsToExport)+"\";                                  \n\n" +

"        try {                                                                                  \n"+
"            stmt1 = conn1.createStatement();                                                   \n" +  
"            stmt2 = conn2.createStatement();                                                   \n" +
"            rs = stmt1.executeQuery(query);                                                    \n"+
"        }catch (SQLException e){                                                               \n"+
"            e.printStackTrace();                                                               \n"+
"        }                                                                                      \n\n" +

"        try{                                                                                   \n"+
"            while(rs.next()){                                                                  \n"+
"                query = "+crtInsert(tabColumnsToExport)+"                                      \n"+
"                try{                                                                           \n"+
"                    stmt2.executeUpdate(query);                                                \n" +
"                }catch(SQLException e){                                                        \n" +
"                    e.printStackTrace();                                                       \n" +
"                }                                                                              \n" +
"            }                                                                                  \n" +
"            stmt1.close();                                                                     \n" +
"            stmt2.close();                                                                     \n" +
"            rs.close();                                                                        \n" +
"        }catch(SQLException ex){                                                               \n" +
"            ex.printStackTrace();                                                              \n" +
"        }                                                                                      \n"+
"    }                                                                                          \n"                
        );
    }
    
    public static String create(String clssName, boolean toFile){
        String mthds = "";
        String mthdsRls = "";
        for(int i = 1; i < mthdLst.size(); i++){
            mthds += "\n" + 
"        "+mthdLst.get(i)+";";
        }
        
        for(int i = 1; i < mthdRlsLst.size(); i++){
            mthdsRls += "\n" + mthdRlsLst.get(i);
        }
        
        code = 
"import java.sql.Connection;                                                                    \n" +
"import com.sybase.jdbc3.jdbc.SybDriver;                                                        \n" + 
"import java.sql.DriverManager;                                                                 \n" +
"import java.sql.ResultSet;                                                                     \n" +
"import java.sql.SQLException;                                                                  \n" +
"import java.sql.Statement;                                                                     \n\n" +
                
"public class " + clssName + " {                                                                \n" +
"    static Connection conn1 = null, conn2 = null;                                              \n" +
//"    static Statement stmt1 = null, stmt2 = null;                                               \n" +
"    static ResultSet rs = null;                                                                \n\n" +

"    public static void main(String[] args){                                                    \n" +
"        try{                                                                                   \n"+
             Bridge.db[0].getConnUrl().replace("DB_CONNECTION", "conn1") + 
"        }catch(Exception e){                                                                \n"+
"            e.printStackTrace();                                                               \n"+
"        }                                                                                      \n"+
//"        try{                                                                                   \n"+
//"            stmt1 = conn1.createStatement();                                                   \n" +
//"        }catch(SQLException e){                                                                \n"+
//"            e.printStackTrace();                                                               \n"+
//"        }                                                                                      \n\n"+
  
"        try{                                                                                   \n"+
             Bridge.db[1].getConnUrl().replace("DB_CONNECTION", "conn2") + 
"        }catch(Exception e){                                                                \n"+
"            e.printStackTrace();                                                               \n"+
"        }                                                                                      \n"+
//"        try{                                                                                   \n"+
//"            stmt2 = conn2.createStatement();                                                   \n\n" +
//"        }catch(SQLException e){                                                                \n"+
//"            e.printStackTrace();                                                               \n"+
//"        }                                                                                      \n"+

         mthds + "                                                                              \n" +
"    }                                                                                          \n" +
     mthdsRls + "                                                                               \n" +
"    public static void truncate(String tableName){                                             \n" +
"        try{                                                                                   \n" +
"            Statement stmt = conn2.createStatement();                                          \n" +
"            stmt.executeUpdate(\"truncate table public.\" + tableName + \" cascade\");          \n" +
"            stmt.close();                                                                      \n" +
"        }catch(Exception e){                                                                   \n" +
"            e.printStackTrace();                                                               \n" +
"        }                                                                                      \n" +
"    }                                                                                          \n\n" +
                
"    public static String strToInsert(String text){                                             \n" +
"        if(text==null||text.trim().length()==0)return \"null\";                                \n" +
"        return \"'\"+text.trim().replace(\"'\", \"''\")+\"'\";                                 \n" +
"    }                                                                                          \n\n" +
                
"    public static void nativeOutput(String text){                                              \n" +
"        if(text!=null&&text.trim().length()!=0)                                                \n" +
"        System.out.println(text);                                                              \n" +
"    }                                                                                          \n\n" +
        
"    public static String intToInsert(String text){                                             \n" + 
"        if(text==null||text.trim().length()==0)return \"null\";                                \n" +
"        return text.trim();                                                                    \n" +
"    }                                                                                          \n\n" +
                
"    public static void truncateTables(){                                                       \n" + 
        tablesToTruncateLst +
"    }                                                                                          \n\n" + 
"}";
        
        if(toFile){
            File flt = new File(clssName+".java");
            PrintWriter out = null;
            try {
                out = new PrintWriter(
                        new BufferedWriter(
                          new FileWriter(flt)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.print(code);
            out.flush();
            out.close();
        }
        
        return code;
    }
}
