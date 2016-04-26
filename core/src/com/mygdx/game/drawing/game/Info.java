package com.mygdx.game.drawing.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Data;
import com.mygdx.game.logic.Button;

/**
 * Created by kopec on 2016-04-26.
 */
public class Info {
        SpriteBatch batch;
        BitmapFont font;

        int x,y,xt,yt;

        public Info(){
            Data.setBatchAndFont(batch,font);
        }
        public void setInfoPosition(int x, int y, int xt, int yt){
            this.x = x;
            this.y = y;
            this.xt = x + xt;
            this.yt = y + yt;
        }
        public void draw(Texture texture, String text){
            batch.draw(texture, x, y);
            font.draw(batch, text, xt, yt);
        }
}
