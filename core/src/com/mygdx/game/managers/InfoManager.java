package com.mygdx.game.managers;



import components.enums.Card;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InfoManager {
    public Map<Card,String> descriptions;

    public InfoManager(){
        descriptions = new HashMap<Card,String>();
        descriptions.put(Card.AQUATIC,"This animal can only attack and be attacked by animals with this perk.");
        descriptions.put(Card.CAMOUFLAGE,"Only an animal with the Sharp Sight perk can attack this animal.");
        descriptions.put(Card.COMMUNICATION,"Both animals with this perk eat at the same time.");
        descriptions.put(Card.COOPERATIONC,"If one of these animals recieves a food token, the other recieves one for free.");
        descriptions.put(Card.COOPERATIONF,"If one of these animals recieves a food token, the other recieves one for free.");
        descriptions.put(Card.HIBERNATION,"Once every two turns this animal may be considered fed without the need to eat.");
        descriptions.put(Card.MASSIVEC,"This animal can only be attacked by animals with this perk.It also needs an additional food token.");
        descriptions.put(Card.MASSIVEF,"This animal can only be attacked by animals with this perk.It also needs an additional food token.");
        descriptions.put(Card.MIMICRY,"If this animal is attacked, the player may choose another viable target to be attacked in its stead.");
        descriptions.put(Card.PARASITEC,"Adds 2 to the number of food needed. Can only be attached to enemy animals.");
        descriptions.put(Card.PARASITEF,"Adds 2 to the number of food needed. Can only be attached to enemy animals.");
        descriptions.put(Card.PASTURAGE,"This animal may substract one food token from the pool each turn.");
        descriptions.put(Card.PIRACY,"This animal can steal one food token from an animal not yet fed each turn.");
        descriptions.put(Card.ROAR,"This animal cannot be attacked once it is fed.");
        descriptions.put(Card.SCAVENGER,"Whenever an animal is eaten, this animal recieves an additional food token");
        descriptions.put(Card.SHARPSIGHT,"An animal with this perk can attack animals with the Camouflage perk.");
        descriptions.put(Card.SPEED,"This animal has a 50% chance of escaping each attacker.");
        descriptions.put(Card.SYMBIOSIS,"The protected animal may only be attacked if the other one is dead, however it can only eat once the other one is fed.");
        descriptions.put(Card.TAILTOSS,"When this animal is attacked, it can discard one perk instead of being eaten.The attacker recieves one food token.");
        descriptions.put(Card.TOXIC,"An animal that has eaten this animal will die at the end of this feeding phase.");

    }

    public String getDescription(Card cardName){
        return descriptions.get(cardName);
    }

    public Set entrySet(){
        return descriptions.entrySet();
    }
}
