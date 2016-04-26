package com.mygdx.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Data;
import com.mygdx.game.drawing.game.Info;
import com.mygdx.game.logic.Button;
import com.mygdx.game.managers.*;
import components.enums.Card;
import components.enums.GameState;
import components.objects.Player;
import com.mygdx.game.drawing.game.*;
/**
 * Created by kopec on 2016-04-26.
 */
public class Game {
    Buttons buttons = new Buttons();
    Info info = new Info();

    SpriteBatch batch;
    BitmapFont font;

    Player player;
    Texture card;

    int screenWidth;
    int screenHeight;

    Textures textures;
    com.mygdx.game.managers.Game gameManager;
    Flag flagManager;
    com.mygdx.game.managers.Info infoManager;

    Button cardButtons[]=new Button[12];
    Button cardChoices[]=new Button[3];
    Button feedChoices[]=new Button[6];
    Button animalPlaces[]=new Button[5];
    Button animalButtons[][]=new Button[4][5];
    Button endRound;
    Button pass;
    Button cancelButton;

    public Game() {
        Data.setBatchAndFont(batch,font);
        Data.setManagers(textures, gameManager, infoManager, flagManager);

        player = Data.getInstance().player;

        screenHeight = Data.getInstance().screenHeight;
        screenWidth = Data.getInstance().screenWidth;
        createButtons();
    }

    public void createButtons(){
        card = textures.getTexture(Card.CHOICE);
        cancelButton = new Button(card, ((screenWidth - card.getWidth()) / 2),(screenHeight - card.getHeight()) / 2 );
        for (int i = 0; i < 3; i++) {
            cardChoices[i] = new Button(card, ((screenWidth - card.getWidth()) / 2) + (i-1) * card.getWidth(), (screenHeight - card.getHeight()) / 2 - card.getHeight(),mouse.x,mouse.y);
        }
        for (int i = 0; i < 6; i++) {
            feedChoices[i] = new Button(card, (screenWidth / 2) + (i-3) * card.getWidth(), (screenHeight - card.getHeight()) / 2 - card.getHeight(),mouse.x,mouse.y);
        }
        for (int i = 0; i < player.cardsNumber(); i++) {
            cardButtons[i] = new Button(textures.getTexture( player.getCards(i)), i * textures.getTexture(player.getCards(i)).getWidth(), 0, mouse.x, mouse.y);
        }
        card = textures.getTexture(Card.SPACE);
        for (int i = 0; i < 5; i++) {
            animalPlaces[i]=new Button(card,((screenWidth - card.getWidth()) / 2) + (i-2) * card.getWidth(), 100,mouse.x,mouse.y);
        }
    }

    public void drawBackgroud(){
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // rysul tlo

        batch.draw(textures.getTexture(Card.BACKGROUND1), 0, 0);
        batch.draw(textures.getTexture(Card.BACKGROUND2), 0, 0);
    }

    public void cancelButton(){
        if(flagManager.printCancelButton) {
            buttons.draw(cancelButton, "cancel");
        }
    }
    public void chosenAnimal(){
        if (flagManager.printSelectedAnimal) {
            for (int i = 0; i < player.animals[selectedAnimal].features.size(); i++) {
                card = textures.getTexture(player.animals[selectedAnimal].getFeature(i));
                if (player.animals[selectedAnimal].features.size() % 2 == 0) {
                    batch.draw(card, ((screenWidth - player.animals[selectedAnimal].features.size()) / 2) + card.getWidth() * (i - (player.animals[selectedAnimal].features.size()) / 2), card.getHeight() + (screenHeight - card.getHeight()) / 2);
                } else {
                    batch.draw(card, ((screenWidth - player.animals[selectedAnimal].features.size()) / 2) + card.getWidth() * (i - (player.animals[selectedAnimal].features.size()) / 2) - card.getWidth() / 2, card.getHeight() + (screenHeight - card.getHeight()) / 2);
                }
            }
        }
    }
    public void animalSpots(){
        if (flagManager.printAnimalsSlots) {
            card = textures.getTexture(Card.SPACE);
            //rysuje miejsca na zwierzaka
            for (int i = 0; i < 5; i++) {
                batch.draw(animalPlaces[i].getGraphic(), animalPlaces[i].getPositionX(), animalPlaces[i].getPositionY());
            }
        }
    }
    public void animals(){
        card = textures.getTexture(Card.ANIMAL);
        for (int i = 0; i < 5; i++) {
            if (player.animals[i] != null) {
                batch.draw(card, ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth(), 100);
                font.draw(batch, "Perks: "+Integer.toString(player.animals[i].features.size()), ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth() + 20, 130+card.getHeight()/2);
                font.draw(batch, "Food: "+Integer.toString(player.animals[i].food)+"/"+Integer.toString(player.animals[i].foodNeeded), ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth() + 15, 110+card.getHeight()/2);
                font.draw(batch, "Fat: "+Integer.toString(player.animals[i].fat)+"/"+Integer.toString(player.animals[i].fatTotal), ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth() + 20, 90+card.getHeight()/2);
            }
        }
    }
    }

    public void otherPlayersAnimals(){
        card = textures.getTexture(Card.ANIMAL);
        for(int j=1;j<gameManager.otherPlayers.size()+1;j++){
            otherPlayer = gameManager.otherPlayers.elementAt(j-1);
            for (int i = 0; i < 5; i++) {
                if (animalButtons[j][i] != null) {
                    batch.draw(card, animalButtons[j][i].getPositionX(), animalButtons[j][i].getPositionY());
                    font.draw(batch, "Perks: "+Integer.toString(otherPlayer.animals[i].features.size()), animalButtons[j][i].getPositionX() + 20, animalButtons[j][i].getPositionY() + 70);
                    font.draw(batch, "Food: "+Integer.toString(otherPlayer.animals[i].food)+"/"+Integer.toString(otherPlayer.animals[i].foodNeeded), animalButtons[j][i].getPositionX() + 15, animalButtons[j][i].getPositionY() + 50);
                    font.draw(batch, "Fat: "+Integer.toString(otherPlayer.animals[i].fat)+"/"+Integer.toString(otherPlayer.animals[i].fatTotal), animalButtons[j][i].getPositionX() + 20, animalButtons[j][i].getPositionY() + 30);
                }
            }
        }

    }

    public void playerCards(){
        //karty gracza
        for (int i = 0; i < player.cardsNumber(); i++) {
            if(cardButtons[i]!=null) {
                batch.draw(cardButtons[i].getGraphic(), cardButtons[i].getPositionX(), cardButtons[i].getPositionY());
            }
        }
    }

    public void feedingOptions(){
        if (flagManager.printFeedingChoices) {
            for (int i = 0; i < 6; i++) {
                batch.draw(feedChoices[i].getGraphic(), feedChoices[i].getPositionX(), feedChoices[i].getPositionY());
            }
            card = textures.getTexture(Card.CHOICE);
            font.draw(batch, "Eat", feedChoices[0].getPositionX() + 40, feedChoices[0].getPositionY() + 30);
            font.draw(batch, "Carnivore", feedChoices[1].getPositionX() + 15, feedChoices[1].getPositionY() + 30);
            font.draw(batch, "Piracy", feedChoices[2].getPositionX() + 25, feedChoices[2].getPositionY() + 30);
            font.draw(batch, "Pasturage", feedChoices[3].getPositionX() + 15, feedChoices[3].getPositionY() + 30);
            font.draw(batch, "Hibernation", feedChoices[4].getPositionX() + 10, feedChoices[4].getPositionY() + 30);
            font.draw(batch, "Scavenger", feedChoices[5].getPositionX() + 15, feedChoices[5].getPositionY() + 30);
        }
    }
    public void passButton(){
        buttons.draw(pass, "pass");
    }
    public void endRoundButton(){
        buttons.draw(endRound, "end round");
    }

    public void choice() {
        if (flagManager.printChoosenCard) {
            for (int i = 0; i < player.cardsNumber(); i++) {
                if (cardButtons[i].isTouched(mouse)) {
                    card = textures.getTexture(player.getCards(i));
                    batch.draw(card, (screenWidth - card.getWidth()) / 2, card.getHeight() + (screenHeight - card.getHeight()) / 2);

                    card = textures.getTexture(Card.RAMKA);
                    batch.draw(card, (screenWidth - card.getWidth()) / 2, (screenHeight - card.getHeight()) / 2);
                    font.draw(batch, infoManager.getDescription(player.getCards(i)), 600 - 3 * infoManager.getDescription(player.getCards(i)).length(), 5 + screenHeight / 2);

                    buttons.draw(cardChoices[0], "Add Animal");
                    buttons.draw(cardChoices[1], "Add Perk 1");
                    buttons.draw(cardChoices[2], "Add Perk 2");
                }
            }
        }
    }

    public void whosTurn(){
        info.setInfoPosition(screenWidth - 2 * card.getWidth(), screenHeight - card.getHeight(),15,card.getHeight()-20);
        if (gameManager.turn == player.number) {
            info.draw(textures.getTexture(Card.CHOICE), "Your turn");
        } else {
            for (Player player : gameManager.otherPlayers) {
                if (player.number == gameManager.turn)
                    if (player.name != null)
                info.draw(textures.getTexture(Card.CHOICE), player.name);
            }
        }
    }

    public void amoutOfFood(){
        Texture card = textures.getTexture(Card.CHOICE);
        info.setInfoPosition(screenWidth - card.getWidth(), 100,12,5+card.getHeight()/2);
        if(gameManager.state== GameState.EVOLUTION){
            if(gameManager.otherPlayers.size()==1) {
                info.draw(card, "1x dice + 2");
            }else if(gameManager.otherPlayers.size()==2) {
                info.draw(card, "2x dice");
            }else if(gameManager.otherPlayers.size()==3) {
                info.draw(card, "2x dice + 2");
            }
        }else if(gameManager.state==GameState.FEEDING){
            info.draw(card,Integer.toString(gameManager.amountOfFood));
        }
    }
}
