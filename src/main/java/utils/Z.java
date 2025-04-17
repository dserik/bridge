package utils;

import constants.DbTypes;
import constants.TypeOfComp;
import java.sql.Types;

public class Z {
    
    public static TypeOfComp compTypes(String type1, String type2){
        return getWarn(typeOf(type1),typeOf(type2));
    }
    
    private static int typeOf(String valueType){
        if(DbTypes.hasValue(valueType.trim(),DbTypes.binary)){
            return Types.BINARY;
        }
        if(DbTypes.hasValue(valueType.trim(),DbTypes.numeric)){
            return Types.NUMERIC;
        }
        if(DbTypes.hasValue(valueType.trim(),DbTypes.date)){
            return Types.DATE;
        }
        if(DbTypes.hasValue(valueType.trim(),DbTypes.string)){
            return Types.NVARCHAR;
        }
        return Types.NULL;
    }
    
    private static TypeOfComp getWarn(int type1, int type2){
        switch(type1){
            case Types.BINARY:
                switch(type2){
                    case Types.BINARY: return TypeOfComp.FINE;
                    case Types.NUMERIC: return TypeOfComp.POSSIBLE;
                    case Types.DATE: return TypeOfComp.IMPOSSIBLE;
                    case Types.NVARCHAR: return TypeOfComp.POSSIBLE;
                    case Types.NULL: return TypeOfComp.NOT_FOUND;
                }
            case Types.NUMERIC:
                switch(type2){
                    case Types.BINARY: return TypeOfComp.IMPOSSIBLE;
                    case Types.NUMERIC: return TypeOfComp.FINE;
                    case Types.DATE: return TypeOfComp.IMPOSSIBLE;
                    case Types.NVARCHAR: return TypeOfComp.POSSIBLE;
                    case Types.NULL: return TypeOfComp.NOT_FOUND;
                }
            case Types.DATE:
                switch(type2){
                    case Types.BINARY: return TypeOfComp.IMPOSSIBLE;
                    case Types.NUMERIC: return TypeOfComp.IMPOSSIBLE;
                    case Types.DATE: return TypeOfComp.FINE;
                    case Types.NVARCHAR: return TypeOfComp.POSSIBLE;
                    case Types.NULL: return TypeOfComp.NOT_FOUND;
                }
            case Types.NVARCHAR:
                switch(type2){
                    case Types.BINARY: return TypeOfComp.IMPOSSIBLE;
                    case Types.NUMERIC: return TypeOfComp.IMPOSSIBLE;
                    case Types.DATE: return TypeOfComp.IMPOSSIBLE;
                    case Types.NVARCHAR: return TypeOfComp.FINE;
                    case Types.NULL: return TypeOfComp.NOT_FOUND;
                }
            default:
                return TypeOfComp.NOT_FOUND;
        }
    }
}
