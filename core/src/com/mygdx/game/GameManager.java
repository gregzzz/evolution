package com.mygdx.game;

import components.Player;
import components.enums.*;

import java.util.Vector;

public class GameManager {
    boolean clientConnected=false;
    Client c;
    public Player player = new Player();
    public Vector otherPlayers = new Vector();

    public GameState state = GameState.BEGIN;
    public Commands command = Commands.NONE;
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
        command = Commands.fromInt(Integer.parseInt(recv[0]));
        System.out.println(command);
        if(command == Commands.GET) {
            if(recv[1].equals("NAME")) {
                // wysylam im
                c.send(Commands.NAME+" TOBI");
            }
        }

        else if(command == Commands.NUMBER){
            player.number = Integer.parseInt(recv[1]);
        }

        else if(command == Commands.NAME){
            if(recv[1].equals(Integer.toString(player.number))){
                player.name = recv[2];
            } else{
                Player otherPlayer = new Player();
                otherPlayer.number = Integer.parseInt(recv[1]);
                otherPlayer.name = recv[2];
                otherPlayer.numberOfCards = 6;
                otherPlayers.addElement(otherPlayer);
            }
        }

        else if(command == Commands.CARDS) {
            if(Integer.parseInt(recv[1]) == player.number){
                for(int i=2;i<recv.length;i++){
                    player.addCard(recv[i]);
                }
                player.numberOfCards = 6;
            } else {
                // tu tez przejebane luskanie xd
                Player otherPlayer = (Player) otherPlayers.elementAt(Integer.parseInt(recv[1]));
                otherPlayer.numberOfCards += Integer.parseInt(recv[2]);
            }
        }

        else if(command == Commands.PHASE) {
            if (recv[1].equals("EVOLUTION")) {
                state = GameState.EVOLUTION;
            } else if (recv[1].equals("FEEDING")) {
                state = GameState.FEEDING;
            } else if (recv[1].equals("BEGIN")) {
                state = GameState.BEGIN;
            } else if (recv[1].equals("END")) {
                state = GameState.END;
            } else if (recv[1].equals("PLAYEROUT")) {
                state = GameState.PLAYEROUT;
            } else if (recv[1].equals("WAIT")) {
                state = GameState.WAIT;
            } else if (recv[1].equals("ERROR")) {
                state = GameState.ERROR;
            }
        }
        // ustawia czyja tura
        else if(command == Commands.TURN){
            turn = Integer.parseInt(recv[1]);
        }
        else if(command == Commands.ADD){
            // przejebane luskanie xd
            Player otherPlayer = (Player)otherPlayers.elementAt(Integer.parseInt(recv[1]));
            // bo dodal zwierze
            otherPlayer.numberOfCards -= 1;
            // dodajemy zwierze
            otherPlayer.addAnimal();

        }

    }
}
