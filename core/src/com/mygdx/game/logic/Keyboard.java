package com.mygdx.game.logic;

/**
 * Created by kopec on 2016-03-30.
 */
public class Keyboard {
    private String text = "";

    public String getText(){
        return text;
    }

    public void clear(){
        text = "";
    }

    public void addChar(char c){
        text = text + c;
    }
}
