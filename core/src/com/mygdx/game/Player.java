package com.mygdx.game;

import java.util.Queue;

/**
 * Created by Woronko on 2016-03-04.
 */
public class Player {
    // wez sie nie wyglupiaj i uzyj vectora ladniej sie iteruje po nim
    // i elastyczniej pracuje
    private String cards[];
    public boolean alive;

    public Player(){
        // serio unikaj wypisywania gdziekolwiek stalych tak jak tutaj 20
        // bo sie nie da nic zmienic potem
        // daltego uzywaj wektora
        cards=new String[20];
        for(int i=0; i<20; i++){
            cards[i]="NULL";
        }
    }

    public void addCard(String card){
        for(int i=0; i<20;i++){
            // bardzo pieknie to wyglada ziom xd
            if(cards[i]=="NULL") {
                cards[i] = card;
                break;
            }
        }
    }
    public void setStartingCards(String s){
        String [] card= s.split(" ");
        for(int j=3; j<3+Integer.parseInt(card[2]); j++) {
            for (int i = 0; i < 20; i++) {
                if (cards[i] == "NULL") {
                    cards[i] = card[j];
                    System.out.println(card[j]);
                    break;
                }
            }
        }
    }

    public String getCards(int i) {
        return cards[i];
    }
}
