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
    private String recv = ""; // dane odebrane od serwera
    Socket client;
    Queue recvQueue = new LinkedList();

    public Client(String s,int p){
        serverName = s;
        port = p;
        // Nawiazanie polaczenia
        connect();
        // Watek odbierajacy dane od serwera i zapisujacy je w recv
        Thread t = new ClientRecv(client,recvQueue);
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
    // zwraca dane aktualnie przechowywane w bufforze
    public String readRecv(){
        String r;
        // wyciagamy stringa z kolejki
        r = (String) recvQueue.poll();
        // jesli nie jest to null zwracamy go
        if(r!=null){
            return r;
        }
        // a jesli jest to pusty string
        else{
            return "";
        }
    }
}
