package components.enums;

/**
 * Created by kopec on 2016-03-17.
 */
import java.util.*;
public enum Commands {
    GET(0),
    NAME(1),
    PHASE(2),
    CARDS(3),
    TURN(4),
    ADD(5),
    EVOLUTION(6),
    PASS(7),
    NUMBER(8),
    FOOD(9),

    NONE(-1);

    private final int id;

    private Commands(int id){
        this.id = id;
    }

    private static final Map<Integer, Commands> intToTypeMap = new HashMap<Integer, Commands>();

    static {
        for (Commands type : Commands.values()) {
            intToTypeMap.put(type.id, type);
        }
    }

    public int getId(){
        return this.id;
    }

    public static Commands fromInt(int i) {
        Commands type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return Commands.NONE;
        return type;
    }
}
