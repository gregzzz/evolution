package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Button {
    Texture graphic;
    int positionX;
    int positionY;
    public Button (Texture texture, int x, int y){
        graphic = texture;
        positionX=x;
        positionY=y;
    }

    public boolean isTouched (int x, int y){
        if(x>positionX && x<positionX+graphic.getWidth() && y>positionY && y<positionY+graphic.getHeight()){
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
