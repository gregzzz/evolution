package multiRoomServer.server.clientManager.roomManager;

import multiRoomServer.server.AdminInrterface;
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
    private int id;


    private Vector<Client> clients = new Vector<>();
    public Game game = new Game(clients);

    public Room(Client client,int roomId, int sizeOfRoom){
        clients.addElement(client);
        size = sizeOfRoom;
        id = roomId;
        if(AdminInrterface.printLogs)
            System.out.println("client <"+client.getClientId()+"> created room <"+id+">");
    }

    public void addClient(Client client){
        clients.addElement(client);
        System.out.println("client <"+client.getClientId()+"> joined room <"+id+">");
        if(clients.size() == size) {
            game.start();
            full = true;
            if(AdminInrterface.printLogs)
                System.out.println("game in room <"+id+"> has started");
        }
    }

    public void removeClient(Client client){
        clients.remove(client);
        if(AdminInrterface.printLogs)
            System.out.println("client <"+client.getClientId()+"> left room <"+id+">");
        full = false;
        if(clients.size() == 0){
            game.interrupt(); //?
            if(AdminInrterface.printLogs)
                System.out.println("romm <"+id+"> is now closed");
        }
    }
}
