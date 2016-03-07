package com.mygdx.game;

import java.util.Queue;

/**
 * Created by Woronko on 2016-03-04.
 */
public class Player {
    // wez sie nie wyglupiaj i uzyj vectora ladniej sie iteruje po nim
    // i elastyczniej pracuje
    private String cards[];

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

    public String getCards(int i) {
        return cards[i];
    }
}
