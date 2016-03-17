package server.src;
import java.net.*;
import java.io.*;
import java.util.*;

public class EvoServer extends Thread{
    private ServerSocket serverSocket;
    private Socket [] clients;
    private int numberOfPlayers;
    private Queue [] recvFromClients;

    private static final int ALL = -1;

    public EvoServer(int p, int n) throws IOException {
        serverSocket = new ServerSocket(p);
        serverSocket.setSoTimeout(100000);

        numberOfPlayers = n;
        clients = new Socket[numberOfPlayers];

        recvFromClients = new LinkedList[numberOfPlayers];
        for(int i = 0; i<numberOfPlayers; i++){
            recvFromClients[i] = new LinkedList();
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
                clients[num] = serverSocket.accept();
                System.out.println("Just connected to " +
                        clients[num].getRemoteSocketAddress());
                // tworzenie watku odbierajacego dane od wlasnie polaczonego clienta
                Thread t = new ClientHandler(num);
                t.start();
                // dodawanie clienta do tablicy gniazdek

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

        GameManager game = new GameManager(this,numberOfPlayers,recvFromClients);

        game.setGame();
        while(true){
            if(game.phase.equals("EVOLUTION")){
                game.evolutionPhase();
            }
            else if(game.phase.equals("FEEDING")){
                game.feedingPhase();
            }

            //jesli skonczy sie faza ewolucji
            if(game.everyonePassed() && game.phase.equals("EVOLUTION")){
                game.setFoodAndPrepareFeedingPhase();
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
    public void send(String s, int n){
        if(n<0) {
            for (int i = 0; i < numberOfPlayers; i++) {
                try {
                    DataOutputStream out = new DataOutputStream(clients[i].getOutputStream());
                    out.writeUTF(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            try {
                DataOutputStream out = new DataOutputStream(clients[n].getOutputStream());
                out.writeUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // odbiera dane od clienta
    public class ClientHandler extends Thread{
        private int playerNumber;
        private String recv;
        public ClientHandler(int n){
            // przypisanie referencji do obiektow stworzonych w serwerze
            playerNumber = n;

        }

        // petala wykonywana w watku odbierajacym dane od clienta
        public void run(){
            while(true){
                try{
                    // odbieranie danych
                    DataInputStream in = new DataInputStream(clients[playerNumber].getInputStream());
                    recv = in.readUTF();
                    // dodanie stringa do kolejki jesli jest rozny od null
                    recvFromClients[playerNumber].offer(recv);

                    // zamykanie gniazdka na zadanie clienta
                    if(recv.equals("END")){
                        clients[playerNumber].close();
                        break;
                    }

                }catch(IOException e){
                    e.printStackTrace();
                    break;
                }
            }

        }
    }
    // funkcja main do rozbudowania o obsluge wielu gier itd ipt
    public static void main(String [] args){
        //serwer ustawiony na jednego gracza
        int port = 5055;
        int number = 1;
        try{
            Thread t = new EvoServer(port,number);
            t.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
