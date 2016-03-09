package com.mygdx.game;

import java.util.Vector;


public class Player {
    private Vector cards;
    public boolean alive;

    public Player(){
        cards=new Vector();
        alive=false;
    }

    public void setStartingCards(String s){
        String [] card= s.split(" ");
        for(int j=3; j<3+Integer.parseInt(card[2]); j++) {
            cards.addElement(card[j]);
            System.out.println(card[j]);
        }
        alive=true;
    }

    public Object getCards(int i) {
        return cards.get(i);
    }

    public int cardsNumber(){
        return cards.size();
    }
}
