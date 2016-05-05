package com.mygdx.game.managers;

import com.mygdx.game.logic.Client;
import components.objects.Player;
import components.enums.*;
import static components.fun.*;

import java.util.Vector;

public class GameManager {
    boolean clientConnected = false;
    Client c;
    public Player player = new Player();
    public Vector<Player> otherPlayers = new Vector<Player>();
    public boolean turnStart=true;

    public GameState state = GameState.BEGIN;
    public Command command = Command.NONE;
    // zeby nie byl to null
    public int turn = -1;
    public int amountOfFood;
    //cialo dla padlinozercy
    public boolean corpse=false;

    public void startClient() {
        if (!clientConnected) {
            clientConnected = true;
            c = new Client("localhost", 5055, this);
        }
    }

    public void addAnimal(int place) {
        c.send(new byte[]{Command.ADD.getId(), (byte) place, (byte)player.number});
    }

    public void addFeature(int playerNumber, int place, Card cardName) {
        c.send(new byte[]{Command.EVOLUTION.getId(), (byte) playerNumber, (byte) place, (byte) cardName.getId()});
    }

    public void kill(int player, int place){
        c.send(new byte[]{Command.KILL.getId(),(byte) player, (byte) place});
    }
    public void steal(int player, int place){
        c.send(new byte[]{Command.STEAL.getId(),(byte) player, (byte) place});
    }

    public void pass() {
        c.send(new byte[]{Command.PASS.getId()});
    }

    public void scavenge() {
        c.send(new byte[]{Command.SCAVENGE.getId()});
    }

    public void endRound() {
        c.send(new byte[]{Command.ENDROUND.getId()});
    }

    public void feed(int animal, int amount) {
        c.send(new byte[]{Command.FEED.getId(), (byte) animal, (byte) amount, (byte) amountOfFood});
    }

    public void handleData(byte[] recv) {
        // jesli chcesz sprawdzic caly napis uzywasz recvData
        // jesli jego czesc uzywasz recv

        command = Command.fromInt((int) recv[0]);
        System.out.println(command);
        if (command == Command.GETNAME) {
            c.send(concat(new byte[]{Command.NAME.getId()}, new String ("TOBI").getBytes()));
        }
        else if (command == Command.ID) {
            player.number = recv[1];
        }
        else if (command == Command.NAME) {
            if (recv[1] == player.number) {
                player.name = stringFromBytes(recv, 2, recv.length - 2);
            } else {
                otherPlayers.addElement(new Player(recv[1], stringFromBytes(recv, 2, recv.length - 2), 6));
            }
        }
        else if (command == Command.CARDS) {
            if (recv[1] == player.number) {

                for (int i = 2; i < recv.length; i++) {
                    player.addCard(Card.fromInt(recv[i]));
                }
                player.numberOfCards += recv.length - 1;
            }
            else {
                for (Player player : otherPlayers) {
                    if (player.number == recv[1]) {
                        player.numberOfCards += recv[2];
                    }
                }
            }
        }
        else if (command == Command.FOOD){
            amountOfFood=recv[1];
        }
        else if (command == Command.HOWMANYANIMALS){
            hungerDeaths();
            c.send(new byte[] {Command.HOWMANYANIMALS.getId(), (byte)player.animalsNumber(), (byte)player.cardsNumber(), (byte)player.number});
        }
        else if (command == Command.STATE) {
            if( recv[1] == GameState.ERROR.getId()){
                c.send(new byte[] {Command.GETGAME.getId()});
            }
            else {
                state = GameState.fromInt(recv[1]);
            }
        }
        // ustawia czyja tura
        else if (command == Command.TURN) {
            turn = recv[1];
            turnStart=true;
        }
        else if (command == Command.ADD) {
            if (recv[2] != player.number) {
                for (Player otherPlayer : otherPlayers) {
                    if (otherPlayer.number == recv[2]) {
                        // bo dodal zwierze
                        otherPlayer.numberOfCards -= 1;
                        // dodajemy zwierze
                        otherPlayer.addAnimal(recv[1]);
                    }
                }
            }
        }
        else if (command == Command.KILL) {
            if (recv[3] != player.number) {
                if (player.number == recv[1]) {
                    // ubijamy
                    player.killAnimal(recv[2]);
                    corpse=true;
                }
            }
        }
        else if (command == Command.STEAL) {
            if (recv[3] != player.number) {
                if (player.number == recv[1]) {
                    player.animals[recv[2]].feed(-1);
                }
            }
        }
        else if (command == Command.SCAVENGE) {
            if (recv[1] != player.number) {
                corpse=false;
            }
        }
        else if (command == Command.FEED) {
            if (recv[4] != player.number) {
                for (Player otherPlayer : otherPlayers) {
                    if (otherPlayer.number == recv[4]) {
                        // karmimy
                        otherPlayer.animals[recv[1]].feed(recv[2]);
                        amountOfFood=recv[3];
                    }
                }
            }
        }
        else if (command == Command.EVOLUTION) {
            if (recv[1] != player.number) {
                for (Player player : otherPlayers) {
                    if (player.number == recv[1]) {

                        // bo dodal zwierze
                        player.numberOfCards -= 1;
                    }
                }
                if(recv[2]==player.number){
                    player.animals[recv[3]].addFeature(Card.fromInt(recv[4]));
                }
                for (Player player : otherPlayers) {
                    if (player.number == recv[2]) {
                        // dodajemy ceche
                        player.animals[recv[3]].addFeature(Card.fromInt(recv[4]));
                    }
                }
            }

        }
    }

    private void hungerDeaths(){
        Player otherPlayer;
        for(int j=0; j<otherPlayers.size()+1;j++) {
            if(j!=0) {
                otherPlayer=otherPlayers.elementAt(j-1);
            }else{
                otherPlayer=player;
            }
            for (int i = 0; i < 5; i++) {
                if (otherPlayer.animals[i] != null) {
                    otherPlayer.animals[i].eatFat();
                    if (otherPlayer.animals[i].isFeeded() && !otherPlayer.animals[i].poisoned) {
                        otherPlayer.animals[i].resetFoodData();
                    } else {
                        otherPlayer.killAnimal(i);
                    }
                }
            }
        }
    }
}

