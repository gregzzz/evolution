package server.src.logic;

import java.util.*;

import components.enums.*;
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

    private int commandId = 0;

    public GameManager(EvoServer s, int n, Vector<Client> r){
        server = s;
        numberOfPlayers = n;
        clients = r;
        players = new Player[n];

        whoPassed = new boolean[n];
        for(int i = 0; i < n; i++){
            whoPassed[i] = false;
        }
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
                        players[client.getNumber()] = new Player(client.getNumber(),stringFromBytes(data,1,data.length-2),6);
                        server.send(concat(new byte[] {Command.NAME.getId(),
                                (byte)client.getNumber()}, players[client.getNumber()].name.getBytes() ));
                        }
                    else if(command == Command.GETGAME){
                        // wyslac cale info o grze
                    }
                    else if (command == Command.EVOLUTION) {
                        //dodaj ceche
                        server.send(new byte[] {Command.EVOLUTION.getId(),data[1], data[2]});
                        nextOneTakeTurn();
                    }
                    else if (command == Command.PASS) {
                        //ktos pasuje
                        whoPassed[turn] = true;
                        server.send(new byte[] {Command.PASS.getId(),(byte) turn});
                    }
                    else {
                        //jak ktos przysle chujowe dane to mu o tym piszemy
                        server.send(new byte[] {Command.NONE.getId()}, turn);
                    }
                }
            }
        }
    public void feedingPhase(){

    }

    //funckja ustawiajaca ilosc jedzenia i przygotowujemy faze zywienia
    public void setFoodAndPrepareFeedingPhase(){
        // napisz funckje do ustalania ilosci jedzenia
        amountOfFood = 4;
        server.send(new byte[] {(byte) amountOfFood});

        // zerujemy tablice passow
        whoPassed = new boolean[numberOfPlayers];
        for(int i = 0; i < numberOfPlayers; i++){
            whoPassed[i] = false;
        }
        // i ustawiamy kolejke na tego kto zaczyna faze
        turn = whoBeginPhase;
        server.send(new byte [] {Command.TURN.getId(), (byte)turn});
    }
    // sprawdza czy juz wszyscy spasowali
    public boolean everyonePassed(){
        for(int i = 0 ; i < numberOfPlayers ; i++){
            if(!whoPassed[i])
                return false;
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

