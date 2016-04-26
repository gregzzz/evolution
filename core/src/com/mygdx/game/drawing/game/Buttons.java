package com.mygdx.game.drawing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Data;
import components.enums.Card;
import components.enums.GameState;
import components.objects.Player;
import com.mygdx.game.logic.Button;
/**
 * Created by kopec on 2016-04-26.
 */
public class Buttons {
    SpriteBatch batch;
    BitmapFont font;

    public Buttons(){
        Data.setBatchAndFont(batch,font);
    }

    public void draw(Button button, String text){
        batch.draw(button.getGraphic(), button.getPositionX(), button.getPositionY());
        font.draw(batch, text, button.getPositionX() + 30, button.getPositionY() + 30);
    }


}
