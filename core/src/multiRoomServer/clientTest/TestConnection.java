package multiRoomServer.clientTest;


import java.util.Scanner;

/**
 * Created by kopec on 2016-07-11.
 */

/*
public class TestConnection {
    public static void main(String [] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("enter an integer");
        Client client = new Client("localhost", 5005);
        int s = 1;
        while(s != 0) {
            s = scan.nextInt();
            switch (s) {
                case 1:
                    byte[] message = { 4 };
                    String login = scan.next();
                    message = concat(message,addZeros(login.getBytes(),10));
                    String email = scan.next();
                    message = concat(message,addZeros(email.getBytes(),10));
                    String password = scan.next();
                    message = concat(message,addZeros(password.getBytes(),10));
                    client.send(message);
                    break;
                case 2:
                    byte[] message2 = { 1 };
                    String login2 = scan.next();
                    message2 = concat(message2,addZeros(login2.getBytes(),10));
                    String password2 = scan.next();
                    message2 = concat(message2,addZeros(password2.getBytes(),10));
                    client.send(message2);
                    break;
                case 3:
                    client.send(new byte[]{Code.FINDROOM.getId()});
            }
        }
    }
}
*/
