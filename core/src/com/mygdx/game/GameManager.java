package com.mygdx.game;

import java.io.IOException;

public class GameManager {
    boolean serverIsUp=false;
    boolean clientConnected=false;
    public Player player=new Player();
    Client c;

    public Player startClient(){
        if(!clientConnected) {
            clientConnected = true;
            c = new Client("localhost", 5055, this);

        }
        return player;
    }
    public void handleData(String recvData){
            // jesli chcesz sprawdzic caly napis uzywasz recvData
            // jesli jego czesc uzywasz recv
            String [] recv = recvData.split(" ");
            if(recvData.equals("GET NAME"))
                // wysylam im
                c.send("TOBI");
            //dostanie poczatkowych kart
            else if(recv[0].equals("CARDS")) {
                player.setStartingCards(recvData);
                player.alive=true;
            }

            else if(recvData.equals("PHASE EVOLUTION")){
                //return player;
            }

    }
}
