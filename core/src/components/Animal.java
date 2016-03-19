package components;

import components.enums.Feature;

import java.util.*;

/**
 * Created by Woronko on 2016-02-27.
 */
public class Animal {
    public int owner;
    public boolean feeded = false;
    public int foodNeeded = 1;

    // moim zdaniem wektor z cechami to lepsza opcja
    // sprawdzasz czy zawiera dany element i juz
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



