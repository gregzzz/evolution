package components.objects;

import components.enums.Card;
import components.enums.Feature;

import java.util.*;


public class Animal {

    public int owner;
    public int number;
    public boolean exists = false;
    public boolean feeded = false;
    public boolean poisoned = false;
    public boolean carnivore = false;
    public int food=0;
    public int foodNeeded = 1;
    public int fat=0;
    public int fatTotal=0;
    public boolean piracy=false;
    public boolean pasturage=false;
    public boolean hibernation=false;
    public boolean hibernationUsed=false;
    public boolean scavenger=false;

    public Integer coopWith[]=new Integer[2];
    public Integer commWith[]=new Integer[2];
    public Integer symbiosis[]=new Integer[2];


    public Vector<Card> features = new Vector<Card>();

    public Animal(int owner, int number){
        this.number=number;
        this.owner = owner;
        for(int i=0;i<2;i++)coopWith[i]=null;
    }

    public void resetFoodData(){
        food=0;
        feeded=false;
    }

    public void feed(int food){
        if(food==-1){
            this.food--;
        }
        for(int i=0;i<food;i++) {
            if(this.isFeeded()){
                if(fat<fatTotal){
                    fat++;
                }
            }else {
                this.food++;
            }
        }
    }

    public void eatFat(){
        while(!this.isFeeded() && fat>0){
            food++;
            fat--;
        }
    }

    public boolean isFeeded(){
        if(food>=foodNeeded){
            return true;
        }else{
            return false;
        }
    }

    public void addFeature(Card cardName){
        if(cardName==Card.CARNIVORE){
            carnivore=true;
            foodNeeded++;
        }
        if(cardName==Card.FAT)fatTotal++;
        if(cardName==Card.MASSIVEC || cardName==Card.MASSIVEF)foodNeeded++;
        if(cardName==Card.PARASITEC || cardName==Card.PARASITEF)foodNeeded+=2;
        features.addElement(cardName);
    }

    public boolean have(Card feature){
        if(features.contains(feature)){
            return true;
        }
        return false;
    }

    public Card getFeature(int featureNumber){
        return features.elementAt(featureNumber);
    }

    public void removeFeature(int cardNumber){
        features.remove(cardNumber);
    }

    public boolean canBeAttacked(Animal attacker){
        if(this.have(Card.MASSIVEC) && (!attacker.have(Card.MASSIVEC) && !attacker.have(Card.MASSIVEF))){
            return false;
        }else if(this.have(Card.MASSIVEF) && (!attacker.have(Card.MASSIVEF) && !attacker.have(Card.MASSIVEC))){
            return false;
        }else if(this.have(Card.ROAR) && this.isFeeded()){
            return false;
        }else if(this.have(Card.AQUATIC) && !attacker.have(Card.AQUATIC)){
            return false;
        }else if(this.have(Card.CAMOUFLAGE) && !attacker.have(Card.SHARPSIGHT)){
            return false;
        }else if(!this.have(Card.AQUATIC) && attacker.have(Card.AQUATIC)){
            return false;
        }else{
            return true;
        }
    }

        //zwraca 0 jesli atak nieudany, 1 jesli udany, 2 jesli odrzucenie ogona
    public int attack(Animal target){
        if(target.have(Card.SPEED)){
            Random generator = new Random();
            if(generator.nextInt(2)==1){
                return 0;
            }else{
                return 1;
            }
        }else if(target.have(Card.TAILTOSS)){
            Random generator = new Random();
            int randInt=generator.nextInt(target.features.size());
            if(target.features.elementAt(randInt)==Card.MASSIVEC || target.features.elementAt(randInt)==Card.MASSIVEF){
                target.foodNeeded--;
            }
            if(target.features.elementAt(randInt)==Card.PARASITEC || target.features.elementAt(randInt)==Card.PARASITEF){
                target.foodNeeded-=2;
            }
            target.removeFeature(randInt);
            return 2;
        }else if(target.have(Card.TOXIC)){
            this.poisoned=true;
            return 1;
        }else{
            return 1;
        }
    }


};



