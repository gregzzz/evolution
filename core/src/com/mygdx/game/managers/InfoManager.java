package com.mygdx.game.managers;



import components.enums.Card;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InfoManager {
    public Map<Card,String> descriptions;

    public InfoManager(){
        descriptions = new HashMap<Card,String>();
        try {
            File file = new File("core/assets/info.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            line = bufferedReader.readLine();
            descriptions.put(Card.AQUATIC, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.CAMOUFLAGE, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.COMMUNICATION, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.COOPERATIONC, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.COOPERATIONF, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.HIBERNATION, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.MASSIVEC, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.MASSIVEF, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.MIMICRY, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.PARASITEC, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.PARASITEF, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.PASTURAGE, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.PIRACY, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.ROAR, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.SCAVENGER, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.SHARPSIGHT, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.SPEED, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.SYMBIOSIS, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.TAILTOSS, line);
            line = bufferedReader.readLine();
            descriptions.put(Card.TOXIC, line);
            fileReader.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDescription(Card cardName){
        return descriptions.get(cardName);
    }

    public Set entrySet(){
        return descriptions.entrySet();
    }
}
