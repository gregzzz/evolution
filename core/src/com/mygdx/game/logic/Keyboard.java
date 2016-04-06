package com.mygdx.game.logic;


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
