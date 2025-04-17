package utils;

import java.io.UnsupportedEncodingException;

public class Utils {
    
    public static String encoding1 = "";
    public static String encoding2 = "";
    
    public static String strToInsert(String text){
        if(text==null||text.trim().length()==0)return "null";
        try {
            return "'"+new String(text.getBytes(encoding1),encoding2).replace("'", "''")+"'";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "null";
    }
    
    public static String intToInsert(String text){ 
        if(text==null||text.trim().length()==0)return "null";
        return text.trim();
    }
    
}
