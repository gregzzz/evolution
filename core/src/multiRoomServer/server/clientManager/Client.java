package multiRoomServer.server.clientManager;


import components.enums.Code;
import multiRoomServer.server.AdminInterface;
import multiRoomServer.server.clientManager.messageHandler.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by kopec on 2016-07-11.
 */
public class Client {
    private Client me;
    public Socket socket;
    public boolean authenticated = false;

    private ClientManager manager;
    private int id;

    public String login;
    public String email;
    public int points;
    public int games;

    public Client (Socket socketObject, int playerId, ClientManager clientsManager){
        me = this;
        manager = clientsManager;
        socket = socketObject;
        id = playerId;

        Thread t = new ClientHandler();
        t.start();
    }
    public void send(byte [] s){
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(s.length);
            out.write(s);
        } catch (IOException e) {

        }
    }
    public int getClientId() {
        return id;
    }

    public boolean close(){
        try{
            socket.close();
            return true;
        } catch (IOException e){
            return false;
        }
    }

    public class ClientHandler extends Thread{
        public void run(){
            byte [] recvData;
            try{
                while(true){
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    int length = in.readInt();
                    if(length>0) {
                        recvData = new byte[length];
                        in.readFully(recvData, 0, recvData.length);
                        id = getClientId();
                        manager.messages.offer(new Message(recvData,id));
                        System.out.println(AdminInterface.printLogs);
                        if(AdminInterface.printLogs)
                            System.out.println("client <" + id + ">: " + Arrays.toString(recvData));
                    }
                }
            }catch(IOException e){

            }
        }
    }
}

