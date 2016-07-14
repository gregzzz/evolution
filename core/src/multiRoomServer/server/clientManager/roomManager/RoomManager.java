package multiRoomServer.server.clientManager.roomManager;

import multiRoomServer.server.clientManager.Client;
import multiRoomServer.server.clientManager.messageHandler.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kopec on 2016-07-12.
 */
public class RoomManager {
    private Map<Client, Room> rooms = new HashMap<>();
    private final static int pointsDifference = 20;
    private static int roomId = 0;
    public RoomManager(){

    }
    public void findRoom(Client client){
        for(Room room: rooms.values())
            if(Math.abs(room.averagePoints - client.points) <= pointsDifference && !room.full ) {
                room.addClient(client);
                rooms.put(client,room);
                return;
            }
            createRoom(client, Message.defaultRoom());
    }
    public void createRoom(Client client, Message message){
        Room room = new Room(client, roomId ++, message.data[1]);
        rooms.put(client,room);
    }
    public void leaveRoom(Client client){
        if(rooms.containsKey(client)) {
            rooms.get(client).removeClient(client);
            rooms.remove(client);
        }
    }
    public void sendRooms(Client client){

    }
    public void serveGame(Client client, Message message){
        rooms.get(client).game.newData(client,message);
    }
}
