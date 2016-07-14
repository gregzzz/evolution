package multiRoomServer.server.clientManager.roomManager;

import multiRoomServer.server.AdminInterface;
import multiRoomServer.server.clientManager.roomManager.game.Game;
import multiRoomServer.server.clientManager.Client;

import java.util.Vector;

/**
 * Created by kopec on 2016-07-11.
 */

// ROOM MUST BE FULL
public class Room {
    public int size;
    public int averagePoints;
    public boolean full = false;
    public boolean started = false;
    private int id;

    private Vector<Client> clients = new Vector<>();
    public Game game = new Game(clients);

    public Room(Client client,int roomId, int sizeOfRoom){
        clients.addElement(client);
        size = sizeOfRoom;
        id = roomId;
         AdminInterface.printLog("client <"+client.getClientId()+"> created room <"+id+">");
    }

    public void addClient(Client client){
        clients.addElement(client);
        AdminInterface.printLog("client <"+client.getClientId()+"> joined room <"+id+">");
        if(clients.size() == size) {
            game.start();
            started = true;
            full = true;
            if(AdminInterface.printLogs)
                System.out.println("game in room <"+id+"> has started");
        }
    }

    public void removeClient(Client client){
        clients.remove(client);
        AdminInterface.printLog("client <"+client.getClientId()+"> left room <"+id+">");
        full = false;
        if(clients.size() == 0){
            AdminInterface.printLog("romm <"+id+"> is now closed");
        }
    }
}
