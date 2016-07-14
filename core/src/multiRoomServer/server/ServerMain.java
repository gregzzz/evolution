package multiRoomServer.server;

import multiRoomServer.server.clientManager.Client;
import multiRoomServer.server.clientManager.ClientManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;


public class ServerMain extends Thread {
    private ServerSocket serverSocket;
    private Database database = new Database();
    private ClientManager clientManager = new ClientManager();
    private AdminInterface admin = new AdminInterface(this, clientManager);

    public static Boolean serverUp = true;

    public ServerMain(int socketNumber){
        try {
            serverSocket = new ServerSocket(socketNumber);
            serverSocket.setSoTimeout(1000);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        int clientId = 0;
        clientManager.start();
        admin.start();
        System.out.println("Waiting for clients on port " + serverSocket.getLocalPort() + " ...");
        while(serverUp) {
            try {
                Client client = new Client(serverSocket.accept(),clientId++,clientManager);
                clientManager.addClient(client);

                AdminInterface.printLog("Just connected to " + client.socket.getRemoteSocketAddress());
            } catch (SocketTimeoutException s) {

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Bye bye : )");
    }

    public static void main(String [] args){
        Thread server = new ServerMain(5055);
        server.start();
    }

}

