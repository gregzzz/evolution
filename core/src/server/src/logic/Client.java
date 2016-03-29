package server.src.logic;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by kopec on 2016-03-22.
 */
public class Client {
    public Socket socket;

    private int number = 0;
    private Queue<byte []> recvFromClient = new LinkedList<byte []>();

    public Client (Socket socket, int number){
        this.socket = socket;
        this.number = number;
    }

    public byte[] poll(){
        return recvFromClient.poll();
    }
    public boolean peek(){
        if(recvFromClient.peek() != null){
            return true;
        }
        else{
            return false;
        }
    }
    public void send(byte [] s){
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(s.length);
            out.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumber() { return number; }

    public void offer(byte [] recv){
        recvFromClient.offer(recv);
    }


}