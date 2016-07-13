package multiRoomServer.server.clientManager.messageHandler;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class Functions {
    public static byte[] concat(byte[] a, byte[] b){
        int aLen = a.length;
        int bLen = b.length;
        byte[] c= new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
    public static byte[] addZeros(byte[] a, int arraySize){
        byte[] array = new byte[arraySize-a.length];
        Arrays.fill(array, (byte) 0);
        return concat(a,array);
    }

    public static int bytesToInt(byte[] bytes,int s, int e){
        byte [] out = new byte[ e ];
        System.arraycopy(bytes,s,out,0,e);
        return ByteBuffer.wrap(out).getInt();
    }

    public static String stringFromBytes(byte [] bytes, int s, int i){
        byte[] c = new byte[i];
        System.arraycopy(bytes, s, c, 0, i);
        return new String(c);
    }
}

