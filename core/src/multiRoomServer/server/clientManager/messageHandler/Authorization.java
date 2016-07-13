package multiRoomServer.server.clientManager.messageHandler;


import multiRoomServer.server.clientManager.Client;
import multiRoomServer.server.Database;

/**
 * Created by kopec on 2016-07-11.
 */
public class Authorization {
    Database database;
    int fieldSize = 10;
    public Authorization(){
        database = Database.getDatabase();
    }
    public boolean serve(Client client, Message message) {
        client.login = message.getStringFromData(1,fieldSize);
        if(database.checkLoginAndPassword(client.login,message.getStringFromData(1+fieldSize,fieldSize))){
            client.authenticated = true;
            client.points = database.getScore(client.login);
            client.games = database.getGames(client.login);
            client.send(Message.ok());
            return true;
        }
        return false;
    }

    public boolean logout(Client client){
        client.authenticated = false;
        client.send(Message.ok());
        return false;
    }
}
