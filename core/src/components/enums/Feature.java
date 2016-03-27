package components.enums;

import java.util.HashMap;
import java.util.Map;


public enum Feature {
    DEAD(1),
    CARNIVORE(2),
    SHARPSIGHT(3),
    SCAVENGER(4),
    SPEED(5),
    HIBERNATION(6),
    SYMBIOSIS(7),
    TAILTOSS(8),
    COMMUNICATION(9),
    PARASITE(10),
    ROAR(11),
    MASSIVE(12),
    PASTURAGE(13),
    AQUATIC(14),
    TOXIC(15),
    CAMOUFLAGE(16),
    COOPERATION(17),
    MIMICRY(18),
    PIRACY(19),
    
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
