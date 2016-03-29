package com.mygdx.game.logic;

import com.badlogic.gdx.graphics.Texture;

public class Button {
    Texture graphic;

    int positionX;
    int positionY;


    public Button (Texture texture, int x, int y, int mx, int my){
        graphic = texture;

        positionX=x;
        positionY=y;
    }
    public Button (Texture texture, int x, int y){
        graphic = texture;

        positionX=x;
        positionY=y;
    }

    public boolean isTouched (int [] mouse){
        if(mouse[0]>positionX && mouse[0]<positionX+graphic.getWidth() && mouse[1]>positionY && mouse[1]<positionY+graphic.getHeight()){
            return true;
        }else{
            return false;
        }
    }

    public int getPositionX(){
        return positionX;
    }

    public int getPositionY(){
        return positionY;
    }

    public Texture getGraphic(){
        return graphic;
    }
}
