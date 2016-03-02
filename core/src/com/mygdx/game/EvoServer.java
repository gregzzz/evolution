package com.mygdx.game;

/**
 * Created by kopec on 2016-03-01.
 */

import java.net.*;
import java.io.*;

public class EvoServer extends Thread{
    private ServerSocket serverSocket;
    private Socket [] clients;
    private String [] recvFromClients;
    private int numberOfPlayers;

    public EvoServer(int p, int n) throws IOException {
        serverSocket = new ServerSocket(p);
        serverSocket.setSoTimeout(100000);
        numberOfPlayers = n;
        clients = new Socket[numberOfPlayers];
        recvFromClients = new String[numberOfPlayers];
    }
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

    public void run(){
        int num = 0;
        // oczekiwanie na graczy az sie polacza
        System.out.println("Waiting for clients on port " +
                serverSocket.getLocalPort() + " ...");
        while(true) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("Just connected to " +
                        client.getRemoteSocketAddress());
                Thread t = new ClientHandler(client, recvFromClients[num]);
                clients[num] = client;
                t.start();
                num += 1;
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
        sendToAll("Siema");
        sendToAll("co tam?");
        sendToAll("END");
    }
}
