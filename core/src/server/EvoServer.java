package server;

import components.enums.Command;
import components.enums.GameState;
import server.logic.Client;
import server.logic.GameManager;

import java.net.*;
import java.io.*;
import java.util.*;

import static components.enums.GameState.*;

public class EvoServer extends Thread{
    private ServerSocket serverSocket;
    private Vector<Client> clients;
    private int numberOfPlayers;

    private Queue<byte[]> [] recvFromClients;

    private static final int ALL = -1;

    public EvoServer(int p, int n) throws IOException {
        serverSocket = new ServerSocket(p);
        serverSocket.setSoTimeout(1000000);
        numberOfPlayers = n;
        clients = new Vector<Client>();

        recvFromClients = new LinkedList[numberOfPlayers];
        for(int i = 0; i<numberOfPlayers; i++){
            recvFromClients[i] = new LinkedList<byte[]>();
        }
    }
    // iteruje po tablicy socketow i wysyla do kazdego clienta
    // obsluga gry wykonywana w watku
    public void run(){
        int num = 0;

        System.out.println("Waiting for clients on port " +
                serverSocket.getLocalPort() + " ...");
        // oczekiwanie na graczy az sie polacza
        while(true) {
            try {
                // oczekiwanie na polaczenie
                Client client = new Client(serverSocket.accept(),num);
                clients.addElement(client);
                System.out.println("Just connected to " +
                        client.socket.getRemoteSocketAddress());
                // tworzenie watku odbierajacego dane od wlasnie polaczonego clienta
                Thread t = new ClientHandler(client);
                t.start();
                // dodawanie clienta do tablicy gniazdek
                client.send(new byte [] {(byte)Command.ID.getId(),(byte)num});
                // zwiekszamy numer porzadkowy
                num += 1;
                // jesli numer porzadowy rowny ilosc graczy zakoncz petle
                if (num == numberOfPlayers) {
                    System.out.println("All players connected");
                    break;
                }
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        // glowna petal zarzadzajaca gra

        GameManager game = new GameManager(this,numberOfPlayers,clients);

        game.setGame();
        while(true){
            // odbieranie czatu i info niezaleznego od kolejki
            if(game.state == EVOLUTION){
                game.evolutionPhase();
            }
            else if(game.state == FEEDING){
                game.feedingPhase();
            }

            //jesli skonczy sie faza ewolucji
            if(game.everyonePassed() && game.state == EVOLUTION){
                game.setFoodAndPrepareFeedingPhase();
            }

            if(game.everyonePassed() && game.state == FEEDING){
                game.prepareForEvolution();
            }
            // usypiaj watek za kazdym razem gdy sprawdzasz czy nowe dane sie pojawily
            // generaleni w kazdej petli
            try {
                Thread.sleep(10);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
    // wysyla w zaleznosci od zmiennej n jesli -1( zdefiniowana stala ALL) wysyla do wzystkich jesli numer gracza wysyla do gracza
    public void send(byte [] s){
        try {
            for (Client client : clients) {
                DataOutputStream out = new DataOutputStream(client.socket.getOutputStream());
                out.writeInt(s.length);
                out.write(s);
            }
        } catch (IOException e) {
            for (Client client : clients) {
                if(!client.socket.isClosed()) {
                    while(!client.socket.isClosed()){
                        try {
                            Thread.sleep(1000);                 //1000 milliseconds is one second.
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println("The player is back");

                }
            }
        }
    }
    // chyba do wyjebania
    public void send(byte [] s, int n){
        try {
            for (Client client : clients) {
                if (client.getNumber() == n) {
                    DataOutputStream out = new DataOutputStream(client.socket.getOutputStream());
                    out.writeInt(s.length);
                    out.write(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // odbiera dane od clienta
    public class ClientHandler extends Thread{
        private Client client;
        public ClientHandler(Client c){
            client = c;
        }

        // petala wykonywana w watku odbierajacym dane od clienta
        public void run(){
            byte [] message;
            try{
                while(true){

                    // odbieranie danych
                    DataInputStream in = new DataInputStream(client.socket.getInputStream());
                    int length = in.readInt();
                    if(length>0) {
                        message = new byte[length];
                        in.readFully(message, 0, message.length);

                        client.offer(message);
                    }
                }
            }catch(IOException e){
                // obsluga wyjatkow
                for(Client client: clients){
                    if(client.getNumber() != this.client.getNumber()){
                        client.send(new byte[] {Command.STATE.getId(), (byte) GameState.PLAYEROUT.getId(), (byte)this.client.getNumber() });
                    }
                }
                reconnect();
            }


        }
        public void reconnect(){
            try {
                client.socket.close();
                client.socket = serverSocket.accept();
                System.out.println("Just connected to " +
                        client.socket.getRemoteSocketAddress());

                Thread t = new ClientHandler(client);
                t.start();

                client.send(new byte [] {(byte)Command.STATE.getId(),(byte)GameState.ERROR.getId()});

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
            } catch (IOException e) {
                //po ptokach
                e.printStackTrace();
            }
        }
    }
    // funkcja main do rozbudowania o obsluge wielu gier itd ipt
    public static void main(String [] args){
        //serwer ustawiony na jednego gracza
        int port = 5055;
        int number = 2;
        try{
            Thread t = new EvoServer(port,number);
            t.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}