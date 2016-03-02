package com.mygdx.game; /**
 * Created by kopec on 2016-03-01.
 */
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.net.*;
import java.io.*;

public class Client extends Thread {
    private String serverName;
    private int port;
    private String recv = "";
    Socket client;

    public Client(String s,int p){
        serverName = s;
        port = p;
        // Nawiazanie polaczenia
        connect();
        // Odbiernie danych i przekazywanie ich do obiektu Data
        Thread t = new ClientRecv(client,recv);
        t.start();
    }
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
    public void send(String s){
        try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeUTF(s);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public String returnRecv(){
        return recv;
    }
}
