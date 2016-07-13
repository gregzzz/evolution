package multiRoomServer.clientTest;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class Client extends Thread {
    private String serverName;
    private int port;
    Socket client;

    public Client(String s,int p){
        serverName = s;
        port = p;
        connect();
        Thread t = new ClientRecv();
        t.start();
    }

    public void send(byte [] s){
        try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeInt(s.length);
            out.write(s);
        }catch(IOException e){
            e.printStackTrace();
        }
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

    public class ClientRecv extends Thread{

        public void run(){
            byte[] message;
            try {
                while(true) {

                    DataInputStream in = new DataInputStream(client.getInputStream());
                    int length = in.readInt();
                    if(length>0) {
                        message = new byte[length];
                        in.readFully(message, 0, message.length); // read the message
                        System.out.println("Server: "+ Arrays.toString(message));
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection lost.");
            }

        }
    }
}
