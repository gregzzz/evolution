package multiRoomServer.server.clientManager.messageHandler;


import multiRoomServer.server.clientManager.Client;
import multiRoomServer.server.Database;

/**
 * Created by kopec on 2016-07-12.
 */
public class Registration {
    Database database;
    private int fieldSize = 10;
    public Registration(){
        database = Database.getDatabase();
    }
    public boolean serve(Client client, Message message){
        client.login = message.getStringFromData(1,fieldSize);
        client.email = message.getStringFromData(1+fieldSize,fieldSize);
        if(database.insert(client.login,client.email,message.getStringFromData(1+2*fieldSize,fieldSize))){
            client.send(Message.ok());
            return true;
        }
        return false;
    }
}
