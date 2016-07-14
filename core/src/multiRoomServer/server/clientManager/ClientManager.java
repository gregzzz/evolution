package multiRoomServer.server.clientManager;

import multiRoomServer.server.ServerMain;
import multiRoomServer.server.clientManager.messageHandler.Authorization;
import multiRoomServer.server.clientManager.messageHandler.Message;
import multiRoomServer.server.clientManager.messageHandler.Registration;
import multiRoomServer.server.clientManager.roomManager.RoomManager;
import multiRoomServer.server.clientManager.messageHandler.Messanger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by kopec on 2016-07-11.
 */
public class ClientManager extends Thread{
    private int numberOfClients = 0;

    private Map<Integer,Client> clients = new HashMap<>();
    public Queue<Message> messages = new LinkedList<>();

    private RoomManager roomManager = new RoomManager();

    private Authorization authorization = new Authorization();
    private Registration registration = new Registration();
    private Messanger messanger = new Messanger();

    public void addClient(Client newClient){
        clients.put(newClient.getClientId(),newClient);
        numberOfClients ++;
        newClient.send(Message.id(newClient.getClientId()));
    }
    public void removeClient(Client client){
                roomManager.leaveRoom(client);
                client.close();
                clients.remove(client.getClientId(),client);
                numberOfClients --;

    }
    public void run()
    {
         while(ServerMain.serverUp) {
            if (messages.peek() != null) {
                Message message = messages.poll();
                if (clients.get(message.clientId).authenticated)
                    handleAuthenticated(message);
                else
                    handleUnuthenticated(message);
            }
             try {
                 Thread.sleep(10);                 //1000 milliseconds is one second.
             } catch(InterruptedException ex) {
                 Thread.currentThread().interrupt();
             }
        }

    }

    public void handleAuthenticated(Message message){
        Client client = clients.get(message.clientId);
        switch(message.getCode()){
            case LOGOUT:
                authorization.logout(client);
                break;
            case MESSAGE:
                messanger.serve(message);
                break;
            case FINDROOM:
                roomManager.findRoom(client);
                break;
            case GETROOMS:
                roomManager.sendRooms(client);
                break;
            case CREATEROOM:
                roomManager.createRoom(client, message);
                break;
            case DELETEROOM:
            case LEAVEROOM:
                roomManager.leaveRoom(client);
                break;
            default:
                roomManager.serveGame(client, message);
        }
    }
    public void handleUnuthenticated(Message message){
        Client client = clients.get(message.clientId);
        switch(message.getCode()){
            case AUTHORIZATION:
                authorization.serve(client, message);
                break;
            case REGISTRATION:
                registration.serve(client, message);
                break;
        }
    }

    public int getNumberOfClients(){
        return numberOfClients;
    }
}
