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
    }

    public String getIntputedText(){
        return inputedText;
    }
}