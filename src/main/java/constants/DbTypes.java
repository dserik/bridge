package constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DbTypes {

    public static Set<String> numeric = new HashSet<>();
    public static Set<String> date = new HashSet<>();
    public static Set<String> binary = new HashSet<>();
    public static Set<String> string = new HashSet<>();
    public static HashMap<String, String> sybPostgreSimilarTypes = new HashMap<>();
    
    public static boolean hasValue(String value, Set<String> type){
        Iterator <String> iterator = type.iterator();
        while (iterator.hasNext()) {
            if(value.toLowerCase().startsWith(iterator.next().toString())){
                return true;
            }
        }
        return false;
    }
    
    public static void initVars(){
        sybPostgreSimilarTypes.put("numeric", "double precision");
        sybPostgreSimilarTypes.put("numericn", "double precision");
        sybPostgreSimilarTypes.put("nvarchar", "character varying");
        sybPostgreSimilarTypes.put("timestamp", "timestamp without time zone");
        sybPostgreSimilarTypes.put("datetimn", "timestamp without time zone");
        sybPostgreSimilarTypes.put("varbinary", "varbinary");
        sybPostgreSimilarTypes.put("tinyint", "smallint");
        sybPostgreSimilarTypes.put("intn", "integer");
        sybPostgreSimilarTypes.put("nchar", "character varying");
        sybPostgreSimilarTypes.put("int", "integer");
        sybPostgreSimilarTypes.put("text", "character varying");
        
        numeric.add("intn");
        numeric.add("integer");
        numeric.add("int");
        numeric.add("smallint");
        numeric.add("real");
        numeric.add("float");
        numeric.add("double");
        numeric.add("tinyint"); 
        numeric.add("decimal");
        numeric.add("numeric");
        numeric.add("numericn");
        numeric.add("money");
        numeric.add("smallmoney");
        numeric.add("bigint");
        numeric.add("serial");
        numeric.add("bigserial");
        numeric.add("double precision");
        
        date.add("date");
        date.add("time");
        date.add("timestamp");
        date.add("datetime");
        date.add("smalldatetime");
        date.add("datetimen");
        date.add("interval");
        date.add("timestamp without time zone");
        
        binary.add("bit varying");
        binary.add("boolean");
        binary.add("bytea");
        binary.add("binary");
        binary.add("long binary");
        binary.add("image");
        binary.add("blob");
        
        string.add("char");
        string.add("nchar");
        string.add("text");
        string.add("varchar");
        string.add("nvarchar");
        string.add("character varying");
        string.add("character");
        string.add("varchar_ignorcase");
    }
}
