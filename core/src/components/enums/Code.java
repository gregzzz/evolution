package components.enums;


import java.util.HashMap;
import java.util.Map;

public enum Code {
    ID(100),
    AUTHORIZATION(101),
    LOGOUT(102),
    MESSAGE(103),
    REGISTRATION(104),
    OK(105),

    GETROOMS(110),
    CREATEROOM(111),
    DELETEROOM(112),
    FINDROOM(113),
    LEAVEROOM(114),

    GAME(120),

    NONE(-1);

    private final int id;

    private Code(int id){
        this.id = id;
    }

    private static final Map<Integer, Code> intToTypeMap = new HashMap<Integer, Code>();

    static {
        for (Code type : Code.values()) {
            intToTypeMap.put(type.id, type);
        }
    }

    public byte getId(){
        return (byte)this.id;
    }

    public static Code fromInt(int i) {
        Code type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return Code.NONE;
        return type;
    }
}
