package com.mygdx.game.logic;


import java.io.*;

public class Configuration {
    public String playerName="Username";
    public String serverAdress="localhost";

    //otwieranie ustawien
    public void openConfiguration() {
        try {
            File file = new File("core/assets/conf.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            line = bufferedReader.readLine();
            playerName = line;
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            serverAdress = line;
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //zapisywanie w pliku ustawien gry
    public void saveConfiguration(){
        try {
            File file = new File("core/assets/conf.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            bufferedWriter.write(playerName.substring(0,playerName.length()-1));
            bufferedWriter.newLine();
            bufferedWriter.write("Password");
            bufferedWriter.newLine();
            bufferedWriter.write(serverAdress);
            bufferedWriter.newLine();


            bufferedWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
