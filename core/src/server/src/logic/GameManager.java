package server.src.logic;

import java.util.*;

import components.enums.*;
import components.objects.Animal;
import components.objects.Player;
import server.src.EvoServer;
import server.src.logic.Client;

import static components.fun.*;

public class GameManager{
    private EvoServer server;

    private int numberOfPlayers = 0;
    private Vector<Client> clients;

    private static final int ALL = -1;


    private Random randomGenerator = new Random();

    private Player[] players;
    public GameState state = GameState.BEGIN;
    private Command command = Command.NONE;
    private int whoBeginPhase;
    private int turn;
    public int nowTurn;
    private boolean [] whoPassed;

    Deck deck = new Deck();

    private int amountOfFood;

    public GameManager(EvoServer s, int n, Vector<Client> r){
        server = s;
        numberOfPlayers = n;
        clients = r;
        players = new Player[n];

    }

    // ustawianie kart w talli

    // funkcja wywolana po polaczeniu sie wszystkich graczy
    public void setGame()
    {
        server.send(new byte [] {Command.GETNAME.getId()});
        // wyslij wszystkim ze zaczynamy gre
        server.send(new byte[] {Command.STATE.getId(),(byte)GameState.BEGIN.getId()});
        // wyslij wszystkim ich karty
        for(int num = 0 ; num < numberOfPlayers ; num++){
            byte [] cards = {};
            for(int card = 0; card < 6 ; card++){
                cards = concat(cards,new byte[] {(byte)deck.getCardFromDeck().getId()});
            }
            server.send(concat(new byte []{Command.CARDS.getId(),(byte)num},cards));
        }
        server.send(new byte [] {Command.TURN.getId(),0});
        turn = 0;
        whoBeginPhase = turn;
        nowTurn = turn;
        // przechodzimy do rozgrywki
        server.send(new byte [] {Command.STATE.getId(),(byte)GameState.EVOLUTION.getId()});
        state = GameState.EVOLUTION;
    }

    public void evolutionPhase() {
        for (Client client: clients) {
            if (client.peek()) {
                    byte [] data = client.poll();
                    command = Command.fromInt(data[0]);
                    System.out.println(command);

                    if (command == Command.ADD) {
                        server.send(new byte[] {Command.ADD.getId(),data[1],(byte)turn});
                        nextOneTakeTurn();
                    }
                    if (command == Command.NAME) {
                        Player player = new Player(client.getNumber(),stringFromBytes(data,1,data.length-2),6);
                        players[client.getNumber()] = player;
                        client.player = player;

                        server.send(concat(new byte[] {Command.NAME.getId(),
                                (byte)client.getNumber()}, players[client.getNumber()].name.getBytes() ));
                    } else if(command == Command.GETGAME){
                        //id
                        client.send(new byte[] {Command.ID.getId(),(byte)client.getNumber()});
                        //imie
                        client.send(concat(new byte[] {Command.NAME.getId(), (byte)client.getNumber()}, players[client.getNumber()].name.getBytes() ));
                        byte [] cards = {};
                        for(Card card: client.player.cards) {
                            cards = concat(cards, new byte[]{(byte)card.getId()});
                        }
                        client.send(concat(new byte []{Command.CARDS.getId(),(byte)client.getNumber()},cards));

                        for(Player player: players){

                        }
                    }
                    else if (command == Command.EVOLUTION) {
                        //dodaj ceche
                        server.send(new byte[] {Command.EVOLUTION.getId(),(byte)client.getNumber(),data[1], data[2]});
                        nextOneTakeTurn();
                    }
                    else if (command == Command.PASS) {
                        //ktos pasuje
                        client.player.pass = true;
                        server.send(new byte[] {Command.PASS.getId(),(byte) turn});
                        if(!everyonePassed()){
                            nextOneTakeTurn();
                        }
                    }
                    else {
                        //jak ktos przysle chujowe dane to mu o tym piszemy
                        server.send(new byte[] {Command.NONE.getId()}, turn);
                    }
            }
        }
    }


    public void feedingPhase(){
        for (Client client: clients) {
            if (client.peek()) {
                byte [] data = client.poll();
                command = Command.fromInt(data[0]);
                System.out.println(command);

                if (command == Command.FEED) {
                    server.send(new byte[] {Command.FEED.getId(),data[1],data[2],data[3],(byte)turn});
                }
                if (command == Command.KILL) {
                    server.send(new byte[] {Command.KILL.getId(),data[1],data[2],(byte)turn});
                }

                if (command == Command.PASS) {
                    //ktos pasuje
                    client.player.pass = true;
                    server.send(new byte[] {Command.PASS.getId(),(byte) turn});
                    if(!everyonePassed()){
                        nextOneTakeTurn();
                    }
                }
                if (command == Command.ENDROUND) {
                    //ktos konczy
                    server.send(new byte[] {Command.ENDROUND.getId(),(byte) turn});
                    nextOneTakeTurn();
                }
                else {
                    //jak ktos przysle chujowe dane to mu o tym piszemy
                    server.send(new byte[] {Command.NONE.getId()}, turn);
                }
            }
        }

    }

    //funckja ustawiajaca ilosc jedzenia i przygotowujemy faze zywienia
    public void setFoodAndPrepareFeedingPhase(){
        // napisz funckje do ustalania ilosci jedzenia
        if(numberOfPlayers==2) {
            amountOfFood = randomGenerator.nextInt(6) + 3;
        }else if(numberOfPlayers==3){
            amountOfFood = randomGenerator.nextInt(6)+ randomGenerator.nextInt(6) + 2;
        }
        else if(numberOfPlayers==4){
            amountOfFood = randomGenerator.nextInt(6)+ randomGenerator.nextInt(6) + 4;
        }
        server.send(new byte[] {Command.FOOD.getId(),(byte) amountOfFood});

        // zerujemy tablice passow
        whoPassed = new boolean[numberOfPlayers];
        for(Player player: players){
            if(player != null)
                player.pass = false;
        }
        // i ustawiamy kolejke na tego kto zaczyna faze
        turn = whoBeginPhase;
        server.send(new byte [] {Command.TURN.getId(), (byte)turn});
        server.send(new byte [] {Command.STATE.getId(),(byte)GameState.FEEDING.getId()});
        state = GameState.FEEDING;
    }

    // sprawdza czy juz wszyscy spasowali
    public boolean everyonePassed(){
        for(Player player: players) {
            if (player != null) {
                if (!player.pass)
                    return false;
            } else {
                return false;
            }
        }
        return true;

    }
    // ustawia kolejke na nastepna osobe gdy dostanie "TURN [kto to mowi] NEXT"
    // i gdy "PASS [kto pasuje]
    public void nextOneTakeTurn(){
        turn += 1;
        if (turn >= numberOfPlayers) {
            turn = 0;
        }
        if(players[turn].pass){
            nextOneTakeTurn();
        }
        server.send(new byte[] {Command.TURN.getId(),(byte)turn});
    }

    //
    //  prace trwaja
    //

    public void setGameState(){

    }

    public void test(){

    }
    // do przerobienia
    public class Deck{
        public Vector<Card> deck= new Vector<Card>();


        public Deck(){
            //deck
            for(int i=1;i<=20;i++){
                for(int j=0;j<4;j++) {
                    deck.addElement(Card.fromInt(i));
                }
            }
        }
        // zwraca randomowa pozycje z deck i ja usuwa
        public Card getCardFromDeck(){
            int randInt = randomGenerator.nextInt(deck.size()-1);
            Card s = deck.get(randInt);
            deck.removeElementAt(randInt);
            return s;
        }
    }
}

