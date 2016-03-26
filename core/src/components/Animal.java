package components;

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


    public void addFeature(Feature feature){
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



