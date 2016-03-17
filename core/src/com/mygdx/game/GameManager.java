package com.mygdx.game;

import java.io.IOException;
import java.util.Vector;

public class GameManager {
    boolean clientConnected=false;
    Client c;
    public Player player = new Player();
    public Vector otherPlayers = new Vector();

    public boolean gameStarted = false;
    public String screen = "menu";
    private String gamePhase = "BEGIN";

    // zeby nie byl to null
    public int turn = -1;

    public void startClient(){
        if(!clientConnected) {
            clientConnected = true;
            c = new Client("localhost", 5055, this);
        }
    }
    public void handleData(String recvData){
        // jesli chcesz sprawdzic caly napis uzywasz recvData
        // jesli jego czesc uzywasz recv
        String [] recv = recvData.split(" ");
        if(recvData.equals("GET NAME")) {
            // wysylam im
            c.send("TOBI");
        }
        else if(recv[0].equals("NUMBER")){
            player.number = Integer.parseInt(recv[1]);
        }
        else if(recv[0].equals("NAME")){
            if(recv[1].equals(Integer.toString(player.number))){
                player.name = recv[2];
            }
            else{
                Player otherPlayer = new Player();
                otherPlayer.number = Integer.parseInt(recv[1]);
                otherPlayer.name = recv[2];
                otherPlayer.numberOfCards = 6;
                otherPlayers.addElement(otherPlayer);
            }
        }
        else if(recv[0].equals("CARDS")) {
            if(Integer.parseInt(recv[1]) == player.number){
                for(int i=2;i<recv.length;i++){
                    player.addCard(recv[i]);
                }
                player.numberOfCards = 6;
            }
            else {
                // tu tez przejebane luskanie xd
                Player otherPlayer = (Player) otherPlayers.elementAt(Integer.parseInt(recv[1]));
                otherPlayer.numberOfCards += Integer.parseInt(recv[2]);
            }
        }
        else if(recvData.equals("PHASE EVOLUTION")){

                gamePhase = "EVOLUTION";
            }
            // ustawia czyja tura
            else if(recv[0].equals("TURN")){
                turn = Integer.parseInt(recv[1]);
            }
            else if(recv[0].equals("ADD")){
                // przejebane luskanie xd
                Player otherPlayer = (Player)otherPlayers.elementAt(Integer.parseInt(recv[1]));
                // bo dodal zwierze
                otherPlayer.numberOfCards -= 1;
                // dodajemy zwierze
                otherPlayer.addAnimal();

            }

        }
}
