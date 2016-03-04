package com.mygdx.game;

import java.io.IOException;

/**
 * Created by Woronko on 2016-02-28.
 */
public class GameManager {
    boolean serverIsUp=false;
    boolean clientConnected=false;
    Player player=new Player();
    Client c;

    public void startServer(){
        if(!serverIsUp) {
            try {
                Thread t = new EvoServer(5000, 1);
                t.start();
                serverIsUp=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Player startClient(){
        if(!clientConnected) {
            clientConnected=true;
            //serwer ustawiony na jednego gracza
            c = new Client("localhost",5005);
            String recv = "";
            // w kazdej petli obdlugujacej gre wolasz metode readRecv na obiekcie clienta i jesli nie jest rowna ""
            // obslugujesz jej zawartosc
            while(!recv.equals("END")){
                //chca imie
                if(recv.equals("GET NAME"))
                    // wysylam im
                    c.send("TOBI");
                //dostanie poczatkowych kart
                if(recv.equals("STARTING CARDS")){
                    for(int i=0;i<6;i++){
                        player.addCard(c.readRecv());

                    }

                }

                if(recv.equals("PHASE EVOLUTION")){
                    return player;
                }

                recv = c.readRecv();

                if(!recv.equals(""))
                    System.out.println(recv);
            }

        }
        return player;
    }
}
