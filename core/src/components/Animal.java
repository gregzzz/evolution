package components;

import components.enums.Card;
import components.enums.Feature;

import java.util.*;


public class Animal {

    public int owner;

    public boolean exists = false;
    public boolean feeded = false;
    public int foodNeeded = 1;


    public Vector<Feature> features = new Vector<Feature>();

    public Animal(int owner){
        this.owner = owner;
    }


    public void addFeature(Card cardName){

        Feature feature=Feature.NONE;
        switch(cardName){
            case SHARPSIGHT:
                feature=Feature.SHARPSIGHT;
                break;
            case SCAVENGER:
                feature=Feature.SCAVENGER;
                break;
            case SPEED:
                feature=Feature.SPEED;
                break;
            case HIBERNATION:
                feature=Feature.HIBERNATION;
                break;
            case PARASITEF:
                feature=Feature.PARASITE;
                break;
            case PARASITEC:
                feature=Feature.PARASITE;
                break;
            case ROAR:
                feature=Feature.ROAR;
                break;
            case MASSIVEC:
                feature=Feature.MASSIVE;
                break;
            case MASSIVEF:
                feature=Feature.MASSIVE;
                break;
            case PASTURAGE:
                feature=Feature.PASTURAGE;
                break;
            case AQUATIC:
                feature=Feature.AQUATIC;
                break;
            case TOXIC:
                feature=Feature.TOXIC;
                break;
            case CAMOUFLAGE:
                feature=Feature.CAMOUFLAGE;
                break;
            case MIMICRY:
                feature=Feature.MIMICRY;
                break;
            case PIRACY:
                feature=Feature.PIRACY;
                break;


        }

        features.addElement(feature);
    }

    public boolean have(Feature feature){
        if(features.contains(feature)){
            return true;
        }
        return false;
    }

    public void removeFeature(Feature feature){
        features.remove(feature);
    }


};



