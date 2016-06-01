package com.mygdx.game.managers;

import com.mygdx.game.logic.Client;
import components.objects.Player;
import components.enums.*;
import static components.fun.*;

import java.io.*;
import java.util.Vector;

public class GameManager {
    boolean clientConnected = false;
    Client c;
    public Player player = new Player();
    public Vector<Player> otherPlayers = new Vector<Player>();
    public boolean turnStart=false;
    public boolean newTurn=false;
    public String playerName="Username";
    public String serverAdress="localhost";
    public String messanger;
    public String chatMessageContent;
    public boolean chatMessageDelivered;

    public GameState state = GameState.BEGIN;
    public Command command = Command.NONE;
    // zeby nie byl to null
    public int turn = -1;
    public int amountOfFood;
    //cialo dla padlinozercy
    public boolean corpse=false;

    public void openConfiguration(){
        try {
            File file = new File("core/assets/conf.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            line = bufferedReader.readLine();
            playerName=line;
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            serverAdress=line;
            fileReader.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    //zapisywanie w pliku ustawien gry
    public void saveConfiguration(){
        try {
            File file = new File("core/assets/conf.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            bufferedWriter.write(playerName.substring(0,playerName.length()-1));
            bufferedWriter.newLine();
            bufferedWriter.write("Password");
            bufferedWriter.newLine();
            bufferedWriter.write(serverAdress);
            bufferedWriter.newLine();


            bufferedWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameManager(){
        openConfiguration();
    }

    public void startClient() {
        if (!clientConnected) {
            clientConnected = true;
            c = new Client(serverAdress, 5055, this);
        }
    }

    public void disconnect() {
        if (clientConnected) {
            clientConnected = false;
            c = null;
        }
    }

    public void addAnimal(int place) {
        c.send(new byte[]{Command.ADD.getId(), (byte) place, (byte)player.number});
    }

    public void addFeature(int playerNumber, int place, Card cardName) {
        c.send(new byte[]{Command.EVOLUTION.getId(), (byte) playerNumber, (byte) place, (byte) cardName.getId()});
    }

    public void addDouble(int place, Card cardName){
        c.send(new byte[]{Command.EVOLUTION2.getId(), (byte) place, (byte) cardName.getId()});
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
    public void poison(int animal) {
        c.send(new byte[]{Command.POISON.getId(), (byte) animal});
    }

    public void chatMessage(String message) {
        c.send(concat(new byte[]{Command.CHAT.getId(), (byte)player.number}, message.getBytes()));
    }

    public void tailToss(int player,int animal, int feature){
        c.send(new byte[]{Command.TAILTOSS.getId(), (byte) player, (byte)animal, (byte)feature });
    }

    public void handleData(byte[] recv) {
        // jesli chcesz sprawdzic caly napis uzywasz recvData
        // jesli jego czesc uzywasz recv

        command = Command.fromInt((int) recv[0]);
        System.out.println(command);
        if (command == Command.GETNAME) {
            c.send(concat(new byte[]{Command.NAME.getId()}, playerName.getBytes()));
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
            }else if( recv[1] == GameState.GAMEOVER.getId()){
                state = GameState.fromInt(recv[1]);
                turnStart=true;
            } else {
                state = GameState.fromInt(recv[1]);
            }
        }
        // ustawia czyja tura
        else if (command == Command.TURN) {
            turn = recv[1];
            newTurn=true;
            if(recv[1]==player.number) {
                turnStart = true;
            }
        }
        else if (command == Command.CHAT) {
            if (recv[1] != player.number) {
                for (Player otherPlayer : otherPlayers) {
                    if (otherPlayer.number == recv[1]) {
                        messanger=otherPlayer.name;
                    }
                }
            }
            else{
                messanger=player.name;
            }
            chatMessageContent=stringFromBytes(recv,2,recv.length-2);
            chatMessageDelivered=true;
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
                for (Player otherPlayer : otherPlayers) {
                    if (otherPlayer.number == recv[1]) {
                        otherPlayer.killAnimal(recv[2]);
                        corpse = true;
                    }
                }
                if (player.number == recv[1]) {
                    // ubijamy
                    player.killAnimal(recv[2]);
                    corpse=true;
                }
            }
        }
        else if (command == Command.STEAL) {
            if (recv[3] != player.number) {
                for (Player otherPlayer : otherPlayers) {
                    if (otherPlayer.number == recv[1]) {
                        otherPlayer.animals[recv[2]].feed(-1);
                    }
                }
                if (player.number == recv[1]) {
                    player.animals[recv[2]].feed(-1);
                }
            }
        }
        else if (command == Command.TAILTOSS) {
            if (recv[4] != player.number) {
                for (Player otherPlayer : otherPlayers) {
                    if (otherPlayer.number == recv[1]) {
                        otherPlayer.animals[recv[2]].removeFeature((int)recv[3]);
                    }
                }
                if (player.number == recv[1]) {
                    player.animals[recv[2]].removeFeature((int)recv[3]);
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
        else if (command == Command.POISON) {
            if (recv[2] != player.number) {
                for (Player otherPlayer : otherPlayers) {
                    if (otherPlayer.number == recv[2]) {
                        // karmimy
                        otherPlayer.animals[recv[1]].poisoned=true;
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
                    player.animals[recv[3]].addFeature(Card.fromInt((int)recv[4]));
                }
                for (Player player : otherPlayers) {
                    if (player.number == recv[2]) {
                        // dodajemy ceche
                        player.animals[recv[3]].addFeature(Card.fromInt((int)recv[4]));
                    }
                }
            }

        }
        else if (command == Command.EVOLUTION2) {
            if (recv[1] != player.number) {
                for (Player otherPlayer : otherPlayers) {
                    if (otherPlayer.number == recv[4]) {
                        // bo dodal zwierze
                        otherPlayer.numberOfCards -= 1;
                        // dodajemy zwierze
                        otherPlayer.addDoubleCard(Card.fromInt((int) recv[3]), (int) recv[2]);
                    }
                }
            }
        }
    }

    public void hungerDeaths(){
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
                    if (!otherPlayer.animals[i].isFeeded() || otherPlayer.animals[i].poisoned) {
                        otherPlayer.killAnimal(i);
                    } else {
                        otherPlayer.animals[i].resetFoodData();
                    }
                }
            }
        }
    }
}

