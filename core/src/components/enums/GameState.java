package components.enums;

import java.util.HashMap;
import java.util.Map;


public enum GameState {
    BEGIN(1),
    EVOLUTION(2),
    FEEDING(3),
    END(4),
    ERROR(5),
    WAIT(6),
    PLAYEROUT(7),
    RECOVER(8),
    MENU(9),
    OPTIONS(10),
    LOGIN(11),
    SERVER(12),
    GAMEOVER(13),

    NONE(-1);

    private final int id;

    private GameState(int id){
        this.id = id;
    }

    private static final Map<Integer, GameState> intToTypeMap = new HashMap<Integer, GameState>();

    static {
        for (GameState type : GameState.values()) {
            intToTypeMap.put(type.id, type);
        }
    }

    public int getId(){
        return this.id;
    }

    public static GameState fromInt(int i) {
        GameState type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return GameState.NONE;
        return type;
    }
}
