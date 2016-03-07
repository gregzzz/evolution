package server.src;

import java.net.*;
import java.io.*;
import java.util.*;

// tworze nowa klase do obslugi gry
// zeby byl mniejszy burdel
public class GameManager{
    private EvoServer server;

    private int numberOfPlayers = 0;
    private Queue [] recv;

    private static final int ALL = -1;

    private int numberOfCards;
    private Vector deck = new Vector(numberOfCards);
    private Random randomGenerator = new Random();

    private PlayerInfo [] players;
    public String phase = "BEGIN";
    private int whoBeginPhase;
    private int turn;
    private boolean [] whoPassed;

    private int amountOfFood;

    public GameManager(EvoServer s, int n, Queue [] r){
        // przepisuje referencje
        // nie chce mi sie kminic jak sie dziedziczy
        server = s;
        numberOfPlayers = n;
        recv = r;
        players = new PlayerInfo[n];

        whoPassed = new boolean[n];
        for(int i = 0; i < n; i++){
            whoPassed[i] = false;
        }
        setDeck();
    }

    // ustawianie kart w talli
    public void setDeck(){
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
    // funkcja wywolana po polaczeniu sie wszystkich graczy
    public void setGame()
    {
        // popros wszystkich o imiona
        server.send("GET NAME",ALL);
        String name;
        for(int num = 0 ; num < numberOfPlayers; num++){
            while(true){
                if(recv[num].peek()!=null){
                    System.out.println(recv[num].peek());
                    players[num] = new PlayerInfo();
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
            server.send("NAME "+ name + " " + Integer.toString(num),ALL);
            name = "";
        }
        // wyslij wszystkim ze zaczynamy gre
        server.send("PHASE BEGIN",ALL);
        // wyslij wszystkim ich karty
        String setOfCards = "";
        for(int num = 0 ; num < numberOfPlayers ; num++){
            for(int card = 0; card < 6 ; card++){
                setOfCards = setOfCards + getCardFromDeck() + " ";
            }
            // CARDS [karta] [karta]
            server.send("CARDS " + num +" 6 " + setOfCards,num);
            setOfCards = "";
        }
        // czekamy az wszyscy sie przedstawia i rozsylamy wiesci o tym kto jest kto to jest numer porzatkowy i imie

        // ustalamy kto zaczyna
        // TURN [numer porzatkowy gracza]
        server.send("TURN 0",ALL);
        turn = 0;
        whoBeginPhase = turn;
        // przechodzimy do rozgrywki
        server.send("PHASE EVOLUTION",ALL);
        phase = "EVOLUTION";
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
                if(data[0].equals("ADD")){
                    //dodaj zwierze
                    server.send("ADD "+turn,ALL);
                    nextOneTakeTurn();
                }
                else if(data[0].equals("EVOLUTION")){
                    //dodaj ceche
                    server.send("EVOLUTION "+turn,ALL);
                }
                else if(data[0].equals("PASS")){
                    //ktos pasuje
                    whoPassed[turn]=true;
                    server.send("PASS "+turn,ALL);
                }
                else{
                    //jak ktos przysle chujowe dane to mu o tym piszemy
                    server.send("BAD "+turn,turn);
                }
            }
        }
    }
    public void feedingPhase(){

    }

    //funckja ustawiajaca ilosc jedzenia i przygotowujemy faze zywienia
    public void setFoodAndPrepareFeedingPhase(){
        server.send("FOOD "+Integer.toString(numberOfPlayers+6),ALL);
        amountOfFood = numberOfPlayers;

        // zerujemy tablice passow
        whoPassed = new boolean[numberOfPlayers];
        for(int i = 0; i < numberOfPlayers; i++){
            whoPassed[i] = false;
        }
        // i ustawiamy kolejke na tego kto zaczyna faze
        turn = whoBeginPhase;
        server.send("TURN " + Integer.toString(turn),ALL);
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
    // kopiuje funkcje wysylajaca bo nie chce mi sie kminic jak sie dziedziczy

}