package components.objects;

import components.enums.Card;
import components.enums.Feature;

import java.util.*;


public class Animal {

    public int owner;

    public boolean exists = false;
    public boolean feeded = false;
    public int foodNeeded = 1;


    public Vector<Card> features = new Vector<Card>();

    public Animal(int owner){
        this.owner = owner;
    }


    public void addFeature(Card cardName){
        features.addElement(cardName);
    }

    public boolean have(Feature feature){
        if(features.contains(feature)){
            return true;
        }
        return false;
    }

    public Card getFeature(int featureNumber){
        return features.elementAt(featureNumber);
    }

    public void removeFeature(Card feature){
        features.remove(feature);
    }


};



