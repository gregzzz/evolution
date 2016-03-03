/**
 * Created by kopec on 2016-03-01.
 */


import java.net.*;
import java.io.*;
import java.util.*;

// klasa tworzona jest w watku i obsluguje odbieranie danych od
// polaczonego clienta
public class ClientHandler extends Thread{
    private Socket client;
    private String recv;
    // kolejka przyjmujaca stringi
    Queue recvQueue;
    public ClientHandler(Socket c, Queue r){
        // przypisanie referencji do obiektow stworzonych w serwerze
        client = c;
        recvQueue = r;
    }
    // petala wykonywana w watku odbierajacym dane od clienta
    public void run(){
        while(true){
            try{
                // odbieranie danych
                DataInputStream in = new DataInputStream(client.getInputStream());
                recv = in.readUTF();
                // dodanie stringa do kolejki jesli jest rozny od null
                recvQueue.offer(recv);
                // zamykanie gniazdka na zadanie clienta
                if(recv.equals("END")){
                    client.close();
                    break;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }
}
