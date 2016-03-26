package server.src;

import java.util.*;
import components.enums.*;
import components.*;

public class GameManager{
    private EvoServer server;

    private int numberOfPlayers = 0;
    private Queue [] recv;

    private static final int ALL = -1;

    private int numberOfCards;

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

    public GameManager(EvoServer s, int n, Queue [] r){
        server = s;
        numberOfPlayers = n;
        recv = r;
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
        for(int num = 0 ; num < numberOfPlayers; num++){
            server.send(Command.NUMBER.getId()+" "+num,num);
        }
        // popros wszystkich o imiona
        server.send(Command.GET.getId()+" NAME",ALL);

        String name;
        for(int num = 0 ; num < numberOfPlayers; num++){
            while(true){
                if(recv[num].peek()!=null){
                    System.out.println(recv[num].peek());
                    players[num] = new Player();
                    players[num].name = (String) recv[num].peek();
                    players[num].number = num;
                    name = (String) recv[num].poll();
                    break;
                }
                try {
                    Thread.sleep(10);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            server.send(Command.NAME.getId()+" "+Integer.toString(num)+" "+name,ALL);
            name = "";
        }
        // wyslij wszystkim ze zaczynamy gre
        server.send(Command.STATE.getId()+" BEGIN",ALL);
        // wyslij wszystkim ich karty
        String setOfCards = "";
        for(int num = 0 ; num < numberOfPlayers ; num++){
            for(int card = 0; card < 6 ; card++){
                setOfCards = setOfCards + deck.getCardFromDeck() + " ";
            }
            // CARDS [karta] [karta]
            server.send(Command.CARDS.getId()+" " + num + " "+setOfCards,num);
            setOfCards = "";
        }
        // czekamy az wszyscy sie przedstawia i rozsylamy wiesci o tym kto jest kto to jest numer porzatkowy i imie

        // ustalamy kto zaczyna
        // TURN [numer porzatkowy gracza]
        server.send(Command.TURN.getId()+" 0",ALL);
        turn = 0;
        whoBeginPhase = turn;
        nowTurn = turn;
        // przechodzimy do rozgrywki
        server.send(Command.STATE.getId()+" EVOLUTION",ALL);
    }
    public void evolutionPhase(){
        // jesli ten czyja tura przyslal dane
        if(recv[turn].peek()!=null){
            if(whoPassed[turn]){
                nextOneTakeTurn();
            }
            else{
                //tablica stringow do obslugi danych przychodzacych
                String [] data = ((String)recv[turn].poll()).split(" ");
                command = Command.fromInt(Integer.parseInt(data[0]));
                if(command == Command.ADD){
                    System.out.print("lolol");
                    //dodaj zwierze
                    server.send(Command.ADD.getId()+" "+data[2]+turn,ALL);
                    nextOneTakeTurn();
                }


                else if(command == Command.EVOLUTION){
                    //dodaj ceche
                    server.send(Command.ADD.getId() + " " + turn,ALL);
                }

                else if(command == Command.PASS){
                    //ktos pasuje
                    whoPassed[turn]=true;
                    server.send(Command.PASS.getId() + " " + turn,ALL);
                }

                else{
                    //jak ktos przysle chujowe dane to mu o tym piszemy
                    server.send(Command.NONE.getId()+" "+turn,turn);
                }
            }
        }
    }
    public void feedingPhase(){

    }

    //funckja ustawiajaca ilosc jedzenia i przygotowujemy faze zywienia
    public void setFoodAndPrepareFeedingPhase(){
        server.send(Command.FOOD+" "+Integer.toString(numberOfPlayers+6),ALL);
        amountOfFood = numberOfPlayers;

        // zerujemy tablice passow
        whoPassed = new boolean[numberOfPlayers];
        for(int i = 0; i < numberOfPlayers; i++){
            whoPassed[i] = false;
        }
        // i ustawiamy kolejke na tego kto zaczyna faze
        turn = whoBeginPhase;
        server.send(Command.TURN+" " + Integer.toString(turn),ALL);
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
    }
    public void handleMassages()
    {
        for(int i=0; i<numberOfPlayers;i++){
            try{
                if(recv[i].peek().equals("RECOVER")){
                    recover(i);
                    recv[i].poll();
                }
            } catch (NullPointerException e){
               // System.out.println("SOMEONE IS OUT");
            }
        }
    }
    //
    //  prace trwaja
    //
    public void recover(int num){
        for(int i=0; i<numberOfPlayers; i++){
            if(num == i){
                String cardsSet = "";
                for(String card: players[num].cards){
                    cardsSet = cardsSet + " " + card;
                }
                server.send(Command.CARDS.getId() + " " + num + " " +cardsSet, num);
            }
        }
    }

    public void setGameState(){

    }

    public void test(){

    }
    // do przerobienia
    public class Deck{
        Vector<String> deck = new Vector<String>();
        public Deck(){
            String c = new String("pasturage pasturage pasturage pasturage parasitec parasitec parasitec parasitec"+
                    " tail tail tail tail symbiosis symbiosis symbiosis symbiosis parasitef parasitef parasitef parasitef"+
                    " massivef massivef massivef massivef massivec massivec massivec massivec"+
                    " aquatic aquatic aquatic aquatic aquatic aquatic aquatic aquatic"+
                    " speed speed speed speed toxic toxic toxic toxic coopc coopc coopc coopc coopf coopf coopf coopf"+
                    " camouflage camouflage camouflage camouflage communication communication communication communication"+
                    " roar roar roar roar scavenger scavenger scavenger scavenger piracy piracy piracy piracy"+
                    " hibernation hibernation hibernation hibernation sharp sharp sharp sharp mimicry mimicry mimicry mimicry");
            String [] cards = c.split(" ");
            numberOfCards = cards.length;
            for(String card : cards) {
                deck.addElement(card);
            }
        }
        // zwraca randomowa pozycje z deck i ja usuwa
        public String getCardFromDeck(){
            int randInt = randomGenerator.nextInt(deck.size()-1);
            String s = (String) deck.get(randInt);
            deck.removeElementAt(randInt);
            return s;
        }
    }
}

