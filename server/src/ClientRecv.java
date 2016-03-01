/**
 * Created by kopec on 2016-03-01.
 */
import java.net.*;
import java.io.*;

public class ClientRecv extends Thread{
    String recv;
    Socket client;
    public ClientRecv(Socket c,String s){
        client = c;
        recv = s;
    }
    public void run(){
        // Odbiernie danych i przekazywanie ich do obiektu Data
        while(true) {
            try {
                DataInputStream in = new DataInputStream(client.getInputStream());
                recv = in.readUTF();
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                out.writeUTF("ACK");
                if(recv.equals("END")){
                    out = new DataOutputStream(client.getOutputStream());
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
