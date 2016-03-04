
import java.net.*;
import java.io.*;
import java.util.*;

// tworze nowa klase do obslugi gry
// zeby byl mniejszy burdel
public class GameManager{
    private int numberOfPlayers = 0;
    private Socket [] clients;
    private Queue [] recv;
    private int numberOfCards;
    private Vector deck = new Vector(numberOfCards);
    private Random randomGenerator = new Random();
    public String phase = "BEGIN";

    public GameManager(int n, Socket [] c, Queue [] r){
        // przepisuje referencje
        // nie chce mi sie kminic jak sie dziedziczy
        numberOfPlayers = n;
        clients = c;

        recv = r;
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

        sendToAll("GET NAME");
        // wyslij wszystkim ze zaczynamy gre
        /*
        sendToAll("PHASE BEGIN");
        // wyslij wszystkim ich karty
        */
        for(int num = 0 ; num < clients.length ; num++){
            sendToOne("STARTING CARDS",num);
            for(int card = 0; card < 6 ; card++){
                sendToOne(getCardFromDeck(),num);
            }
            // CARDS [karta] [karta]
        }
        // czekamy az wszyscy sie przedstawia i rozsylamy wiesci o tym kto jest kto to jest numer porzatkowy i imie
        String name;
        for(int num = 0 ; num < clients.length ; num++){
                while(true){
                    if(recv[num].peek()!=null){
                        name = (String) recv[num].poll();
                        break;
                    }
                }

            sendToAll("NAME "+ name + " " + Integer.toString(num));

            name = "";
        }
        // przechodzimy do rozgrywki
        sendToAll("PHASE EVOLUTION");
        phase = "EVOLUTION";
    }
    public void evolutionPhase(){

    }

    // kopiuje funkcje wysylajaca bo nie chce mi sie kminic jak sie dziedziczy
    public void sendToAll(String s){
        int num = 0;
        while(num<numberOfPlayers){
            try {
                DataOutputStream out = new DataOutputStream(clients[num].getOutputStream());
                out.writeUTF(s);
                num += 1;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void sendToOne(String s, int n){
        try {
            DataOutputStream out = new DataOutputStream(clients[n].getOutputStream());
            out.writeUTF(s);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}