/**
 * Created by kopec on 2016-03-01.
 */
import java.net.*;
import java.io.*;
import java.util.*;

// klasa tworzona w watku obslugujaca odbieranie danych od serwera
public class ClientRecv extends Thread{
    Queue recvQueue;
    String recv;
    Socket client;
    public ClientRecv(Socket c,Queue s){
        // przypisanie referencji do obiektow klasy client
        client = c;
        recvQueue = s;
    }
    // funckja wykonywana po odpaleniu watku
    public void run(){
        // petala odbierajaca dane
        while(true) {
            try {
                //odbieranie danych
                DataInputStream in = new DataInputStream(client.getInputStream());
                recv = in.readUTF();
                //dodanie danych do kolejki
                recvQueue.add(recv);
                //zamykanie gniazdka na zadanie serwera
                if(recv.equals("END")){
                    // informujemy serwer ze juz sie nie bawimy
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());
                    out.writeUTF("END");
                    client.close();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
