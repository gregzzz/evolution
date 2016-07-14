package multiRoomServer.server.clientManager.roomManager.game;

import components.enums.Card;
import components.enums.Command;
import components.enums.GameState;
import components.objects.Player;
import multiRoomServer.server.AdminInterface;
import multiRoomServer.server.clientManager.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import static components.fun.concat;
import static components.fun.stringFromBytes;

public class GameManager{
    private Game server;
    private int numberOfPlayers;
    private int amountOfFood;
    private Vector<Client> clients;
    private Random randomGenerator = new Random();
    private Deck deck;
    private Command command = Command.NONE;

    public Map<Client, Player> players = new HashMap<>();
    public GameState state;

    private int whoBeginPhase;
    private int turn;
    public int nowTurn;

    public GameManager(Game game, Vector<Client> clientsVector){
        server = game;
        clients = clientsVector;
    }
    public void setGame()
    {
        numberOfPlayers = clients.size();
        for(Client client: clients) {
            Player player =  new Player();
            player.number = client.getClientId();
            player.numberOfCards = 6;
            players.put(client, player);
        }
        server.send(new byte [] {Command.GETNAME.getId()});

        state = GameState.BEGIN;
        server.send(new byte[] {Command.STATE.getId(),(byte)GameState.BEGIN.getId()});

        deck = new Deck();
        for(Client client:clients){
            byte [] cards = {};
            for(int card = 0; card < 6 ; card++){
                cards = concat(cards,new byte[] {(byte)deck.getCardFromDeck().getId()});
            }
            server.send(concat(new byte []{Command.CARDS.getId(),(byte)client.getClientId()},cards));
        }

        turn = 0;
        server.send(new byte [] {Command.TURN.getId(),(byte)clients.get(turn).getClientId()});

        whoBeginPhase = turn;
        nowTurn = turn;

        state = GameState.EVOLUTION;
        server.send(new byte [] {Command.STATE.getId(),(byte)GameState.EVOLUTION.getId()});
    }

    public void evolutionPhase() {
        for (Client client: clients) {
            if (players.get(client).recv.peek() != null) {
                byte [] data = players.get(client).recv.poll();
                command = Command.fromInt(data[0]);
                AdminInterface.printLog(command.toString());

                if (command == Command.ADD) {
                    server.send(new byte[] {Command.ADD.getId(),data[1],data[2]});
                    nextOneTakeTurn();
                }
                if (command == Command.NAME) {
                    players.get(client).name = stringFromBytes(data,1,data.length-2);

                    server.send(concat(new byte[] {Command.NAME.getId(),
                            (byte)client.getClientId()}, players.get(client).name.getBytes() ));
                }
                if(command == Command.GETGAME){
                    //id
                    client.send(new byte[] {Command.ID.getId(),(byte)client.getClientId()});
                    //imie
                    client.send(concat(new byte[] {Command.NAME.getId(), (byte)client.getClientId()}, players.get(client).name.getBytes() ));
                    byte [] cards = {};
                    for(Card card: players.get(client).cards) {
                        cards = concat(cards, new byte[]{(byte)card.getId()});
                    }
                    client.send(concat(new byte []{Command.CARDS.getId(),(byte)client.getClientId()},cards));

                }
                if (command == Command.EVOLUTION) {
                    //dodaj ceche
                    server.send(new byte[] {Command.EVOLUTION.getId(),(byte)client.getClientId(),data[1] , data[2], data[3]});
                    nextOneTakeTurn();
                }
                if (command == Command.EVOLUTION2) {
                    //dodaj ceche
                    server.send(new byte[] {Command.EVOLUTION2.getId(),(byte)client.getClientId(),data[1] , data[2], (byte)clients.elementAt(turn).getClientId()});
                    nextOneTakeTurn();
                }
                if (command == Command.KILL) {
                    server.send(new byte[] {Command.KILL.getId(),data[1],data[2],(byte)clients.elementAt(turn).getClientId()});
                }

                if (command == Command.CHAT) {
                    String message=stringFromBytes(data, 2, data.length-2);
                    server.send(concat(new byte[]{Command.CHAT.getId(),data[1]}, message.getBytes()));
                }

                if (command == Command.HOWMANYANIMALS) {
                    byte [] cards = {};
                    int amountOfCardsToSend;
                    //ile wyslac kart
                    if((data[1]==0&&data[2]>0) || data[1]>0){
                        amountOfCardsToSend=data[1]+1;
                    }else{
                        amountOfCardsToSend=6;
                    }
                    //stworzenie tablicy kart do wyslania
                    for(int card = 0; card < amountOfCardsToSend ; card++){
                        cards = concat(cards,new byte[] {(byte)deck.getCardFromDeck().getId()});
                    }
                    //wyslanie
                    server.send(concat(new byte []{Command.CARDS.getId(),data[3]},cards));
                }
                if (command == Command.PASS) {
                    if(client.getClientId() == clients.elementAt(turn).getClientId())
                        players.get(client).pass = true;

                    server.send(new byte[] {Command.PASS.getId(),(byte)clients.elementAt(turn).getClientId()});
                    if(!everyonePassed()){
                        nextOneTakeTurn();
                    }
                }
            }
        }
    }


    public void feedingPhase(){
        for (Client client: clients) {
            if (players.get(client).recv.peek()!=null) {
                byte [] data = players.get(client).recv.poll();
                command = Command.fromInt(data[0]);
                AdminInterface.printLog(command.toString());

                if (command == Command.FEED) {
                    server.send(new byte[] {Command.FEED.getId(),data[1],data[2],data[3],(byte)client.getClientId()});
                }
                if (command == Command.POISON) {
                    server.send(new byte[] {Command.POISON.getId(),data[1],(byte)client.getClientId()});
                }
                if (command == Command.KILL) {
                    server.send(new byte[] {Command.KILL.getId(),data[1],data[2],(byte)client.getClientId()});
                }
                if (command == Command.STEAL) {
                    server.send(new byte[] {Command.STEAL.getId(),data[1],data[2],(byte)client.getClientId()});
                }
                if (command == Command.TAILTOSS) {
                    server.send(new byte[] {Command.TAILTOSS.getId(),data[1],data[2],data[3],(byte)client.getClientId()});
                }
                if (command == Command.SCAVENGE) {
                    server.send(new byte[] {Command.SCAVENGE.getId(),(byte)client.getClientId()});
                }

                if (command == Command.PASS) {
                    //ktos pasuje
                    for(Player player: players.values()){
                        if(player.number==turn) {
                            player.pass = true;
                        }
                    }
                    server.send(new byte[] {Command.PASS.getId(),(byte)clients.elementAt(turn).getClientId()});
                    if(!everyonePassed()){
                        nextOneTakeTurn();
                    }
                }
                if (command == Command.ENDROUND) {
                    //ktos konczy
                    server.send(new byte[] {Command.ENDROUND.getId(),(byte)clients.elementAt(turn).getClientId()});
                    nextOneTakeTurn();
                }
                if (command == Command.CHAT) {
                    String message=stringFromBytes(data, 2, data.length-2);
                    server.send(concat(new byte[]{Command.CHAT.getId(),data[1]}, message.getBytes()));
                }

                if (command == Command.HOWMANYANIMALS) {
                    byte [] cards = {};
                    int amountOfCardsToSend;
                    //ile wyslac kart
                    if((data[1]==0&&data[2]>0) || data[1]>0){
                        amountOfCardsToSend=data[1]+1;
                    }else{
                        amountOfCardsToSend=6;
                    }
                    //stworzenie tablicy kart do wyslania
                    for(int card = 0; card < amountOfCardsToSend ; card++){
                        cards = concat(cards,new byte[] {(byte)deck.getCardFromDeck().getId()});
                    }
                    //wyslanie
                    server.send(concat(new byte []{Command.CARDS.getId(),data[3]},cards));
                }

            }
        }

    }

    public void prepareForEvolution(){
        if(deck.getDeckSize()>6*players.size()) {
            for (Player player : players.values()) {
                if (player != null)
                    player.pass = false;
            }
            // i ustawiamy kolejke na tego kto zaczyna faze
            if (whoBeginPhase == numberOfPlayers - 1) {
                whoBeginPhase = 0;
                turn = 0;
            } else {
                whoBeginPhase++;
                turn = whoBeginPhase;
            }
            server.send(new byte[]{Command.HOWMANYANIMALS.getId()});
            server.send(new byte[]{Command.TURN.getId(), (byte)clients.elementAt(turn).getClientId()});
            server.send(new byte[]{Command.STATE.getId(), (byte) GameState.EVOLUTION.getId()});
            state = GameState.EVOLUTION;
        }
        else{
            server.send(new byte[]{Command.STATE.getId(), (byte) GameState.GAMEOVER.getId()});
            state = GameState.GAMEOVER;
        }

    }

    public void setFoodAndPrepareFeedingPhase(){
        if(numberOfPlayers==2) {
            amountOfFood = randomGenerator.nextInt(6) + 3;
        }
        else if(numberOfPlayers==3){
            amountOfFood = randomGenerator.nextInt(6)+ randomGenerator.nextInt(6) + 2;
        }
        else if(numberOfPlayers==4){
            amountOfFood = randomGenerator.nextInt(6)+ randomGenerator.nextInt(6) + 4;
        }

        server.send(new byte[] {Command.FOOD.getId(),(byte) amountOfFood});

        for(Player player: players.values())
            if(player != null)
                player.pass = false;

        turn = whoBeginPhase;
        server.send(new byte [] {Command.TURN.getId(), (byte)clients.elementAt(turn).getClientId()});

        state = GameState.FEEDING;
        server.send(new byte [] {Command.STATE.getId(),(byte)GameState.FEEDING.getId()});
    }

    public boolean everyonePassed(){
        for(Player player: players.values())
            if (!player.pass)
                return false;
        return true;

    }
    public void nextOneTakeTurn(){
        if (++turn >= numberOfPlayers)
            turn = 0;
        if(players.get(clients.elementAt(turn)).pass)
            nextOneTakeTurn();
        server.send(new byte[] {Command.TURN.getId(),(byte)clients.elementAt(turn).getClientId()});
    }
    public void gameOver(){
        server.send(new byte[]{Command.STATE.getId(), (byte) GameState.GAMEOVER.getId()});
        state = GameState.GAMEOVER;
    }

    public class Deck{
        public Vector<Card> deck= new Vector<Card>();

        public Deck(){
            for(int i=1;i<=20;i++)
                for(int j=0;j<players.size();j++)
                    deck.addElement(Card.fromInt(i));
        }
        public Card getCardFromDeck(){
            int randInt = randomGenerator.nextInt(deck.size()-1);
            Card s = deck.get(randInt);
            deck.removeElementAt(randInt);
            return s;
        }
        public int getDeckSize(){
            return deck.size();
        }
    }
}