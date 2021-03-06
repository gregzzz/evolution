
package com.mygdx.game.logic;
import com.mygdx.game.managers.GameManager;
import components.enums.Code;

import static components.fun.*;


import java.net.*;
import java.io.*;
import java.util.Arrays;

public class Client extends Thread {
    private String serverName;
    private int port;
    Socket client;
    GameManager manager;
    //gameManager
    public void login(){
        byte[] message2 = { Code.AUTHORIZATION.getId() };
        String login2 = "tobi";
        message2 = concat(message2,addZeros(login2.getBytes(),10));
        String password2 = "tobi";
        message2 = concat(message2,addZeros(password2.getBytes(),10));
        send(message2);
        send(new byte[]{Code.FINDROOM.getId()});
    }
    public Client(String s,int p, GameManager m){
        serverName = s;
        port = p;
        manager = m;
        // Watek odbierajacy dane od serwera i zapisujacy je w recv
        Thread t = new ClientRecv();
        t.start();
    }
    // wysylanie stringa do serwera
    public void send(byte [] s){
        try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeInt(s.length);
            out.write(s);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public class ClientRecv extends Thread{
        // funckja wykonywana po odpaleniu watku
        public void connect(){
            try
            {
                System.out.println("Connecting to " + serverName +
                        " on port " + port);
                client = new Socket(serverName, port);
                System.out.println("Just connected to "
                        + client.getRemoteSocketAddress());
            }catch(IOException e) {
                System.out.println("Fail. 5 seconds to retry attempt..");
                try {
                    Thread.sleep(5000);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                connect();
            }
        }

        public void run(){
            // petala odbierajaca dane
            connect();
            login();
            byte[] message;
            try {
                while(true) {

                    //odbieranie danych
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    int length = in.readInt();
                    if(length>0) {
                        message = new byte[length];
                        in.readFully(message, 0, message.length); // read the message
                        manager.handleData(message);
                        System.out.println("server: " + Arrays.toString(message));
                    }

                }
            } catch (IOException e) {
                System.out.println("Connection lost.");
                connect();
            }

        }
    }
}
