package com.mygdx.game;

import java.io.IOException;

public class GameManager {
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
                c.send(player.playerName);
            //dostanie poczatkowych kart
            else if(recv[0].equals("CARDS")) {
                player.setStartingCards(recvData);
            }else if(recv[0].equals("NAME")){
                if(recv[1].equals(player.playerName)){
                    player.playerNumber=Integer.parseInt(recv[2]);
                }
                System.out.println(player.playerName+" "+player.playerNumber);
            }

            else if(recvData.equals("PHASE EVOLUTION")){
            }

    }
}
