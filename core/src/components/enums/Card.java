package components.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kopec on 2016-03-19.
 */

/* przekonalem sie
 lepsza rozszerzalnosc i czytelnosc
 ale trzeba kod pozmieniac
 */

public enum Card {
    PASTURAGE(1),
    PARASITEC(2),
    TAIL(3),
    SYMBIOSIS(4),
    //...

    NONE(-1);

    private final int id;

    private Card(int id){
        this.id = id;
    }

    private static final Map<Integer, Card> intToTypeMap = new HashMap<Integer, Card>();

    static {
        for (Card type : Card.values()) {
            intToTypeMap.put(type.id, type);
        }
    }

    public int getId(){
        return this.id;
    }

    public static Card fromInt(int i) {
        Card type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return Card.NONE;
        return type;
    }
}
