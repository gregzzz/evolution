package multiRoomServer.server.clientManager.messageHandler;


import components.enums.Code;

/**
 * Created by kopec on 2016-07-11.
 */
public class Message {
    public int clientId;
    public byte[] data;

    private Code code;
    public Message(Code c, int id){
        code = c;
        clientId = id;
    }
    public Message(byte[] recvData, int id){
        data = recvData;
        clientId = id;
        code = Code.fromInt(data[0]);
    }
    public Code getCode(){
        return code;
    }
    public String getStringFromData(int start, int end){
        return Functions.stringFromBytes(data,start,end);
    }

    public static byte[] fromCode(Code i) { return new byte[]{(byte) i.getId()};}
    public static byte[] id(int id){
        return new byte [] { Code.ID.getId(),(byte)id};
    }
    public static byte[] ok() { return new byte[] {Code.OK.getId() };}
    public static byte[] empty() { return new byte[] {-1};}
    public static Message defaultRoom() { return new Message(new byte[]{0,2},0);}
}
