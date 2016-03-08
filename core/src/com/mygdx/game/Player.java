package com.mygdx.game;

import java.util.*;

/**
 * Created by Woronko on 2016-03-04.
 */
public class Player {
    public int number;
    public String name;

    public int numberOfCards = 0;
    public Vector cards = new Vector();
    public Vector animals = new Vector();

    public void addCard(String card){
        cards.addElement(card);
    }
    public void addAnimal(){
        animals.addElement(new Animal());
    }

    public String getCards(int i){
        return (String)cards.elementAt(i);
    }
}
