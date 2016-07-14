package multiRoomServer.server;

import multiRoomServer.server.clientManager.Client;
import multiRoomServer.server.clientManager.ClientManager;
import multiRoomServer.server.clientManager.messageHandler.Message;

import java.util.Scanner;
import static multiRoomServer.server.clientManager.messageHandler.Functions.*;
/**
 * Created by kopec on 2016-07-13.
 */
public class AdminInrterface extends Thread {
    private ServerMain server;
    private ClientManager manager;

    public static boolean printLogs = false;
    public static boolean serverRun = true;
    public AdminInrterface(ServerMain serverMain, ClientManager clientManager){
        server = serverMain;
        manager = clientManager;
    }

    public void run(){
        Scanner scan = new Scanner(System.in);
        String option = "";
        while(!option.equals("close")){
            option = scan.next();
            if(option.equals("insert")) {
                byte[] message = {4};
                String login = scan.next();
                message = concat(message, addZeros(login.getBytes(), 10));
                String email = scan.next();
                message = concat(message, addZeros(email.getBytes(), 10));
                String password = scan.next();
                message = concat(message, addZeros(password.getBytes(), 10));
                manager.handleAuthenticated(new Message(message, -1));
            }
            if(option.equals("print")){
                printLogs = false;
            }
            if(option.equals("stop print")){
                printLogs = false;
            }
        }
        serverRun = false;
    }
}
