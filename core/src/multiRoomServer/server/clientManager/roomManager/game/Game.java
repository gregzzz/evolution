package multiRoomServer.server.clientManager.roomManager.game;

import components.enums.GameState;
import multiRoomServer.server.ServerMain;
import multiRoomServer.server.clientManager.Client;

import multiRoomServer.server.clientManager.messageHandler.Message;

import java.util.Vector;

/**
 * Created by kopec on 2016-07-12.
 */
public class Game extends Thread {
    Vector<Client> clients;
    public int size;
    GameManager game;

    public Game(Vector<Client> c){
        clients = c;
        game = new GameManager(this,clients);
    }
    public void run(){
        game.setGame();
        size = clients.size();
        while(ServerMain.serverUp){
            if(size != clients.size()) {
                game.gameOver();
                break;
            }

            if(game.state == GameState.EVOLUTION)
                game.evolutionPhase();

            else if(game.state == GameState.FEEDING)
                game.feedingPhase();

            if(game.everyonePassed() && game.state == GameState.EVOLUTION)
                game.setFoodAndPrepareFeedingPhase();

            if(game.everyonePassed() && game.state ==GameState.FEEDING)
                game.prepareForEvolution();

            sleep(10);
        }
    }
    public void sleep(int i){
        try {
            Thread.sleep(i);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    public void send(byte [] s){
        for (Client client : clients)
            client.send(s);
    }
    public void newData(Client client, Message message){
        game.players.get(client).recv.offer(message.data);
    }

}
