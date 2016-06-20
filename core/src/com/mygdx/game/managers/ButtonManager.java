package com.mygdx.game.managers;

import com.mygdx.game.logic.Button;
import components.enums.Card;
import components.objects.Player;
import com.badlogic.gdx.graphics.Texture;


public class ButtonManager {
    public Button cardButtons[] = new Button[12];
    public Button cardChoices[] = new Button[3];
    public Button feedChoices[] = new Button[6];
    public Button animalPlaces[] = new Button[5];
    public Button animalButtons[][] = new Button[4][5];
    public Button menuButtons[] = new Button[6];
    public Button chat;
    public Button chatMessage;
    public Button endRound;
    public Button pass;
    public Button cancelButton;
    public Button yes;
    public Button no;


    public GameManager gameManager;
    public TextureManager textures;

    int screenWidth;
    int screenHeight;

    Player player;

    public ButtonManager(GameManager gameManager, TextureManager textures, int screenWidth, int screenHeight){
        this.gameManager=gameManager;
        this.textures=textures;
        this.screenHeight=screenHeight;
        this.screenWidth=screenWidth;
        this.player=gameManager.player;
    }

    public void addAnimalButton(int place){
        animalButtons[0][place]=new Button(textures.getTexture(Card.ANIMAL),animalPlaces[place].getPositionX(),animalPlaces[place].getPositionY());
    }

    //guziki kart
    public void updateCardButtons(){
        for (int i = 0; i < player.cardsNumber(); i++) {
            cardButtons[i] = new Button(textures.getTexture(player.getCards(i)), i * textures.getTexture(player.getCards(i)).getWidth(), 0);
        }
    }

    public void createButtons() {
        Texture card = textures.getTexture(Card.RAMKA);
        chatMessage=new Button(card,(screenWidth - card.getWidth()) / 2, screenHeight/2 - card.getHeight()*4);
        card = textures.getTexture(Card.CHOICE);
        yes=new Button(card, ((screenWidth - card.getWidth()) / 2) - card.getWidth()/2, (screenHeight - card.getHeight()) / 2 - card.getHeight());
        chat=new Button(card, card.getWidth(), screenHeight-card.getHeight());
        no=new Button(card, ((screenWidth - card.getWidth()) / 2) + card.getWidth()/2 , (screenHeight - card.getHeight()) / 2 - card.getHeight());
        cancelButton = new Button(card, ((screenWidth - card.getWidth()) / 2), (screenHeight - card.getHeight()) / 2 - 2*card.getHeight());
        for (int i = 0; i < 3; i++) {
            cardChoices[i] = new Button(card, ((screenWidth - card.getWidth()) / 2) + (i - 1) * card.getWidth(), (screenHeight - card.getHeight()) / 2 - card.getHeight());

        }
        for (int i = 0; i < 6; i++) {
            feedChoices[i] = new Button(card, (screenWidth / 2) + (i - 3) * card.getWidth(), (screenHeight - card.getHeight()) / 2 - card.getHeight());

        }

        for (int i = 0; i < player.cardsNumber(); i++) {
            cardButtons[i] = new Button(textures.getTexture(player.getCards(i)), i * textures.getTexture(player.getCards(i)).getWidth(), 0);
        }
        for (int i = 0; i < 6; i++) {
            menuButtons[i] = new Button(card, 100, screenHeight - 100 - i * card.getHeight());
        }
        card = textures.getTexture(Card.SPACE);
        for (int i = 0; i < 5; i++) {
            animalPlaces[i] = new Button(card, ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth(), 100);
        }
    }

    //aktualizuje wszystkie przyciski zwierzat, wywolywane jesli kogos zabiles i na poczatku kazdej tury
    public void updateAnimalButtons(){
        Texture card;
        Player otherPlayer;
        for(int i=0;i<4;i++) {
            for (int j = 0; j < 5; j++) {
                animalButtons[i][j]=null;
            }
        }
        card = textures.getTexture(Card.ANIMAL);
        for (int i = 0; i < 5; i++) {
            if (player.animals[i] != null) {
                animalButtons[0][i]=new Button(card, ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth(), 100);
            }
        }
        if (gameManager.otherPlayers.size() > 0) {
            otherPlayer = gameManager.otherPlayers.elementAt(0);
            for (int i = 0; i < 5; i++) {
                if (otherPlayer.animals[i] != null) {
                    animalButtons[1][i]=new Button(card, ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth(), screenHeight - card.getHeight());
                }
            }
        }
        if (gameManager.otherPlayers.size() > 1) {
            otherPlayer = gameManager.otherPlayers.elementAt(1);
            for (int i = 0; i < 5; i++) {
                if (otherPlayer.animals[i] != null) {
                    animalButtons[2][i]=new Button(card, 0, (screenHeight + 100 - card.getHeight()) / 2 + (i - 2) * card.getHeight());
                }
            }
        }
        if (gameManager.otherPlayers.size() > 2) {
            otherPlayer = gameManager.otherPlayers.elementAt(2);
            for (int i = 0; i < 5; i++) {
                if (otherPlayer.animals[i] != null) {
                    animalButtons[3][i]=new Button(card, screenWidth - card.getWidth(), (screenHeight + 100 - card.getHeight()) / 2 + (i - 2) * card.getHeight());
                }
            }
        }
    }
}