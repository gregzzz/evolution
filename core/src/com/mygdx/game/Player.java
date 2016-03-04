package com.mygdx.game;

import java.util.Queue;

/**
 * Created by Woronko on 2016-03-04.
 */
public class Player {

    private String cards[];

    public Player(){
        cards=new String[20];
        for(int i=0; i<20; i++){
            cards[i]="NULL";
        }
    }

    public void addCard(String card){
        for(int i=0; i<20;i++){
            if(cards[i]=="NULL") {
                cards[i] = card;
                break;
            }
        }
    }

    public String getCards(int i) {
        return cards[i];
    }
}
