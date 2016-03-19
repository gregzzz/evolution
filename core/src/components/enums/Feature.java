package components.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kopec on 2016-03-19.
 */

// enumy takie super

public enum Feature {
    DEAD(1),

    NONE(-1);

    private final int id;

    private Feature(int id){
        this.id = id;
    }

    private static final Map<Integer, Feature> intToTypeMap = new HashMap<Integer, Feature>();

    static {
        for (Feature type : Feature.values()) {
            intToTypeMap.put(type.id, type);
        }
    }

    public int getId(){
        return this.id;
    }

    public static Feature fromInt(int i) {
        Feature type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return Feature.NONE;
        return type;
    }

}
