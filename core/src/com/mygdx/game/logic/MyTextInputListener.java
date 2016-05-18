package com.mygdx.game.logic;

import com.badlogic.gdx.Input;
import com.mygdx.game.managers.FlagManager;

public class MyTextInputListener implements Input.TextInputListener {
    FlagManager flagManager;
    String inputedText;

    public MyTextInputListener(FlagManager flagManager){
        this.flagManager=flagManager;
    }

    @Override
    public void input (String text) {
        System.out.print(text);
        flagManager.inputed=true;
        inputedText=text;
    }

    @Override
    public void canceled () {
        inputedText=null;
        flagManager.inputed=true;

    }

    public String getIntputedText(){
        return inputedText;
    }
    public void setIntputedText(String text){
        inputedText=text;
    }
}