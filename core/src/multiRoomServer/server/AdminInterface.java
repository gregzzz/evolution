package multiRoomServer.server;

import multiRoomServer.server.clientManager.ClientManager;
import multiRoomServer.server.clientManager.messageHandler.Message;
import multiRoomServer.server.clientManager.messageHandler.Registration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import static multiRoomServer.server.clientManager.messageHandler.Functions.*;
/**
 * Created by kopec on 2016-07-13.
 */
public class AdminInterface extends Thread {
    private ServerMain server;
    private ClientManager manager;
    private AdminClient admin;
    public static Boolean printLogs = new Boolean(false);

    private Scanner scan = new Scanner(System.in);

    public AdminInterface(ServerMain serverMain, ClientManager clientManager){
        server = serverMain;
        manager = clientManager;
    }
    public void run(){
        String option = "";
        admin = new AdminClient("localhost",5055);

        while(!option.equals("c")){
            option = scan.next();
            if(option.equals("i")) {
                addUser();
            }
            if(option.equals("p")){
                printLogs = true;
            }
            if(option.equals("s")){
                printLogs = false;
            }
        }
    }
    public void addUser(){
        byte[] message = {104};
        String login = scan.next();
        message = concat(message, addZeros(login.getBytes(), 10));
        String email = scan.next();
        message = concat(message, addZeros(email.getBytes(), 10));
        String password = scan.next();
        message = concat(message, addZeros(password.getBytes(), 10));
        admin.send(message);
    }
    public static void printLog(String logMessage){
        synchronized(printLogs){
            if(printLogs){
                System.out.println(logMessage);
            }
        }
    }
}

class AdminClient {
    private String serverName;
    private int port;
    Socket client;
    public AdminClient(String s,int p){
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
            client = new Socket(serverName, port);
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
                        AdminInterface.printLog("Server: "+ Arrays.toString(message));
                    }
                }
            } catch (IOException e) {
            }

        }
    }
}
