package multiRoomServer.server;

import com.mongodb.*;

import java.net.UnknownHostException;

public class Database {
    MongoClient mongo;
    DB db;
    DBCollection table;

    static Database handler;
    public static Database getDatabase(){
        return handler;
    }

    public Database(){
        handler = this;
        try {
            mongo = new MongoClient("localhost", 27017);

            /**** Get database ****/
            // if database doesn't exists, MongoDB will create it for you
            db = mongo.getDB("evolution");

            /**** Get collection / table from 'testdb' ****/
            // if collection doesn't exists, MongoDB will create it for you
            table = db.getCollection("users");


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public boolean insert(String login, String email, String password){
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("login", login);

        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            return false;                               // niepowodzenie dodania bo juz jest ktos taki
        }

        searchQuery = new BasicDBObject();
        searchQuery.put("email", email);

        cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            return false;                               // niepowodzenie taki email juz jest
        }

        BasicDBObject document = new BasicDBObject();
        document.put("login", login);
        document.put("email", email);
        document.put("password", password);
        document.put("score", 0);
        document.put("games", 0);
        table.insert(document);

        return true;
    }

    public boolean checkLoginAndPassword(String login, String password){
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("login", login);
        searchQuery.put("password", password);

        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            //System.out.println(cursor.next());
            return true;
        }
        return false;
    }
    public int getScore(String login){
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("login", login);

        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            return (int)cursor.next().get("score");
        }
        return -1;              // brak takiego loginu mozna rzucic wyjatek
    }

    public int getGames(String login){
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("login", login);

        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            return (int)cursor.next().get("games");
        }
        return -1;              // brak takiego loginu mozna rzucic wyjatek
    }

    public void updateScore(String login, int points){
        BasicDBObject newDocument1 = new BasicDBObject().append("$inc",
                new BasicDBObject().append("score", points));
        BasicDBObject newDocument2 = new BasicDBObject().append("$inc",
                new BasicDBObject().append("games", 1));

        table.update(new BasicDBObject().append("login", login), newDocument1);
        table.update(new BasicDBObject().append("login", login), newDocument2);
    }

    public void printTable(){
        DBCursor cursor = table.find();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    public void deleteAll(){
        DBCursor cursor = table.find();
        while (cursor.hasNext()) {
            table.remove(cursor.next());
        }
    }
}
