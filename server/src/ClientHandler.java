/**
 * Created by kopec on 2016-03-01.
 */


import java.net.*;
import java.io.*;

public class ClientHandler extends Thread{
    private Socket client;
    private String recv;

    public ClientHandler(Socket c, String r){
        client = c;
        recv = r;
    }
    public void run(){
        while(true){
            try{
                DataInputStream in = new DataInputStream(client.getInputStream());
                recv = in.readUTF();
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
