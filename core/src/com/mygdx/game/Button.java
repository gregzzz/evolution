package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Button {
    Texture graphic;

    int positionX;
    int positionY;

    int mouseX;
    int mouseY;

    public Button (Texture texture, int x, int y, int mx, int my){
        graphic = texture;

        positionX=x;
        positionY=y;

        mouseX = mx;
        mouseY = my;
    }

    public boolean isTouched (){
        if(mouseX>positionX && mouseX<positionX+graphic.getWidth() && mouseY>positionY && mouseY<positionY+graphic.getHeight()){
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
