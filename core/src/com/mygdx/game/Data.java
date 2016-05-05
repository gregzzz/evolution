package com.mygdx.game;

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

/**
 * Created by kopec on 2016-04-26.
 */
public class Data {
    int screenWidth=1200;
    int screenHeight=800;

    String line;
    BufferedReader in;
    SpriteBatch batch;
    Texture card;
    Sprite sprite;
    BitmapFont font;

    Button cardButtons[]=new Button[12];
    Button cardChoices[]=new Button[3];
    Button feedChoices[]=new Button[6];
    Button animalPlaces[]=new Button[5];
    Button animalButtons[][]=new Button[4][5];
    Button endRound;
    Button pass;
    Button cancelButton;




    private Mouse mouse = new Mouse();
    private Keyboard keyboard = new Keyboard();

    Flag flagManager;
    Game gameManager=new Game();
    Textures textures;
    LayoutManager layout = new LayoutManager(textures);
    Info infomanager = new Info();

    Player player;
    Player otherPlayer;

    // zmienne do do funkcji z uzyciem kart i wybranego zwierzęcia
    int chosenCard=99;
    int selectedAnimal=99;

    boolean secondaryPerk;

    boolean actionDone=false;

    boolean getText = false;


}
