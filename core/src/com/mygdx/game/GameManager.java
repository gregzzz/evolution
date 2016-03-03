package com.mygdx.game;

import java.io.IOException;

/**
 * Created by Woronko on 2016-02-28.
 */
public class GameManager {
    boolean serverIsUp=false;
    boolean clientConnected=false;

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

    public void startClient(){
        if(!clientConnected) {
            Client client = new Client("localhost", 5000);
            clientConnected=true;
        }
    }
}
