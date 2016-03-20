package components.enums;

/**
 * Created by kopec on 2016-03-17.
 */
import java.util.*;
public enum Command {
    GET(0),
    NAME(1),
    STATE(2),
    CARDS(3),
    TURN(4),
    ADD(5),
    EVOLUTION(6),
    PASS(7),
    NUMBER(8),
    FOOD(9),

    NONE(-1);

    private final int id;

    private Command(int id){
        this.id = id;
    }

    private static final Map<Integer, Command> intToTypeMap = new HashMap<Integer, Command>();

    static {
        for (Command type : Command.values()) {
            intToTypeMap.put(type.id, type);
        }
    }

    public int getId(){
        return this.id;
    }

    public static Command fromInt(int i) {
        Command type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return Command.NONE;
        return type;
    }
}
