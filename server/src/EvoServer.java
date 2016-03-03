/**
 * Created by kopec on 2016-03-01.
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class EvoServer extends Thread{
    private ServerSocket serverSocket;
    private Socket [] clients;
    private int numberOfPlayers;
    private Queue [] recvFromClients;

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
    // obsluga gry wykonywana w watku
    public void run(){
        int num = 0;

        System.out.println("Waiting for clients on port " +
                serverSocket.getLocalPort() + " ...");
        // oczekiwanie na graczy az sie polacza
        while(true) {
            try {
                // oczekiwanie na polaczenie
                Socket client = serverSocket.accept();
                System.out.println("Just connected to " +
                        client.getRemoteSocketAddress());
                // tworzenie watku odbierajacego dane od wlasnie polaczonego clienta
                Thread t = new ClientHandler(client, recvFromClients[num]);
                t.start();
                // dodawanie clienta do tablicy gniazdek
                clients[num] = client;
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

        GameManager game = new GameManager(numberOfPlayers,clients,recvFromClients);
        game.setGame();
        while(true){
            if(game.phase.equals("EVOLUTION")){
                game.evolutionPhase();
            }
        }
    }
    // funkcja main do rozbudowania o obsluge wielu gier itd ipt
    public static void main(String [] args){
	//serwer ustawiony na jednego gracza
        int port = 5005;
        int number = 1;
        try{
            Thread t = new EvoServer(port,number);
            t.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
