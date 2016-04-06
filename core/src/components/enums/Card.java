package components.enums;

import java.util.HashMap;
import java.util.Map;



public enum Card {

    MASSIVEC(1),
    COOPERATIONC(2),

    SHARPSIGHT(3),
    SCAVENGER(4),
    SPEED(5),
    HIBERNATION(6),
    SYMBIOSIS(7),
    TAILTOSS(8),
    COMMUNICATION(9),
    PARASITEF(10),
    ROAR(11),
    MASSIVEF(12),
    PASTURAGE(13),
    AQUATIC(14),
    TOXIC(15),
    CAMOUFLAGE(16),
    COOPERATIONF(17),
    MIMICRY(18),
    PIRACY(19),
    PARASITEC(20),
    CARNIVORE(21),
    FAT(22),

    //kARTY NIE KARTY, DO PRZECHOWYWANIA W MAPIE TEKSTUR
    BACKGROUND1(-2),
    BACKGROUND2(-3),
    SPACE(-4),
    ANIMAL(-5),
    CHOICE(-6),
    RAMKA(-7),
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
