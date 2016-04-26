package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.logic.Button;
import com.mygdx.game.logic.Keyboard;
import com.mygdx.game.logic.Mouse;
import com.mygdx.game.managers.*;
import components.objects.Player;

import java.io.BufferedReader;
import java.util.Vector;

/**
 * Created by kopec on 2016-04-26.
 */
public class Data {
    SpriteBatch batch;
    BitmapFont font;

    public int screenWidth=1200;
    public int screenHeight=800;

    String line;
    BufferedReader in;

    Texture card;
    Sprite sprite;





    public Mouse mouse = new Mouse();
    public Keyboard keyboard = new Keyboard();

    Flag flagManager;
    Game gameManager=new Game();
    Textures textures;

    Info infomanager = new Info();

    public Player player;
    public Player otherPlayer;

    // zmienne do do funkcji z uzyciem kart i wybranego zwierzęcia
    int chosenCard=99;
    int selectedAnimal=99;

    boolean secondaryPerk;

    boolean actionDone=false;

    boolean getText = false;

    private static Data holder;

    public void Data(){
        holder = this;

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
    }
    public static Data getInstance() { return holder; }

    public static void setBatchAndFont(SpriteBatch b, BitmapFont f){
        b = getInstance().batch;
        f = getInstance().font;
    }

    public static void setManagers(Textures t, Game g, Info i, Flag f){
        t = getInstance().textures;
        g = getInstance().gameManager;
        i = getInstance().infomanager;
        f = getInstance().flagManager;
    }


}
