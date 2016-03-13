package com.mygdx.game;

import java.util.Vector;


public class Player {
    private Vector cards;
    int playerNumber;
    String playerName;

    public Player(){
        cards=new Vector();
        playerName="Tobi";
    }

    public void setStartingCards(String s){
        String [] card= s.split(" ");
        for(int j=3; j<3+Integer.parseInt(card[2]); j++) {
            cards.addElement(card[j]);
            System.out.println(card[j]);
        }
    }

    public Object getCards(int i) {
        return cards.get(i);
    }

    public int cardsNumber(){
        return cards.size();
    }
}
