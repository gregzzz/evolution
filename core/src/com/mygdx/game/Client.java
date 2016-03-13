/**
 * Created by kopec on 2016-03-01.
 */
package com.mygdx.game;
import java.util.*;
import java.net.*;
import java.io.*;

public class Client extends Thread {
    private String serverName;
    private int port;
    Socket client;
    GameManager manager;
    //gameManager

    public Client(String s,int p, GameManager m){
        serverName = s;
        port = p;
        manager = m;
        // Nawiazanie polaczenia
        connect();
        // Watek odbierajacy dane od serwera i zapisujacy je w recv
        Thread t = new ClientRecv();


        t.start();
    }
    // laczenie z serwerem
    public void connect(){
        try
        {
            System.out.println("Connecting to " + serverName +
                    " on port " + port);
            client = new Socket(serverName, port);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    // wysylanie stringa do serwera
    public void send(String s){
        try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeUTF(s);
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void handleData(String recv){
        manager.handleData(recv);
    }

    public class ClientRecv extends Thread{
        // funckja wykonywana po odpaleniu watku
        public void run(){
            // petala odbierajaca dane
            String r;
            while(true) {
                try {
                    //odbieranie danych
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    r = in.readUTF();
                    handleData(r);

                    //zamykanie gniazdka na zadanie serwera
                    if(r.equals("END")){
                        // informujemy serwer ze juz sie nie bawimy
                        DataOutputStream out = new DataOutputStream(client.getOutputStream());
                        out.writeUTF("END");
                        client.close();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
