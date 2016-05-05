package com.mygdx.game.drawing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import components.enums.Card;
import components.enums.GameState;
import components.objects.Player;

/**
 * Created by kopec on 2016-04-26.
 */
public class Buttons {
    /*
    public void drawGame(){
        //najpierw tlo
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(gameManager.turnStart){
            startOfTurnCleanup();
        }

        batch.begin();
        // rysul tlo
        batch.draw(textures.getTexture(Card.BACKGROUND1), 0, 0);
        batch.draw(textures.getTexture(Card.BACKGROUND2), 0, 0);

        //czja tura
        if (gameManager.state == GameState.EVOLUTION || gameManager.state == GameState.FEEDING) {

            card = textures.getTexture(Card.CHOICE);

            //ilosc jedzenia
            batch.draw(card, screenWidth - card.getWidth(), 100);
            if(gameManager.state==GameState.EVOLUTION){
                if(gameManager.otherPlayers.size()==1) {
                    font.draw(batch, "1x dice + 2", screenWidth - card.getWidth() + 12, 105 + card.getHeight()/2);
                }else if(gameManager.otherPlayers.size()==2) {
                    font.draw(batch, "2x dice", screenWidth - card.getWidth() + 20, 105 + card.getHeight()/2);
                }else if(gameManager.otherPlayers.size()==3) {
                    font.draw(batch, "2x dice + 2", screenWidth - card.getWidth() + 12, 105 +card.getHeight()/2);
                }
            }else if(gameManager.state==GameState.FEEDING){
                font.draw(batch, Integer.toString(gameManager.amountOfFood), screenWidth - card.getWidth()/2 -1, 105 + card.getHeight()/2);
            }

            //czyja tura
            batch.draw(card, screenWidth - 2 * card.getWidth(), screenHeight - card.getHeight());
            if (gameManager.turn == player.number) {
                font.draw(batch, "Your Turn", screenWidth - 2 * card.getWidth() + 15, 5 + screenHeight - 25);
            } else {
                for (Player player : gameManager.otherPlayers) {
                    if (player.number == gameManager.turn)
                        if (player.name != null)
                            font.draw(batch, player.name, screenWidth - 2 * card.getWidth() + 15, 5 + screenHeight - 25);
                }
            }

            //guziki pass i end turn
            batch.draw(pass.getGraphic(), pass.getPositionX(), pass.getPositionY());
            font.draw(batch, "Pass", 35, 5 + screenHeight - 25);
            batch.draw(endRound.getGraphic(), endRound.getPositionX(), endRound.getPositionY());
            font.draw(batch, "End Round", screenWidth - card.getWidth() + 15, 5 + screenHeight - 25);
            // rysuj wybor
            if (flagManager.printChoosenCard) {
                for (int i = 0; i < player.cardsNumber(); i++) {
                    if (cardButtons[i].isTouched(mouse)) {
                        card = textures.getTexture(player.getCards(i));
                        batch.draw(card, (screenWidth - card.getWidth()) / 2, card.getHeight() + (screenHeight - card.getHeight()) / 2);
                        //narysowanie ramki do tekstu
                        card = textures.getTexture(Card.RAMKA);
                        batch.draw(card, (screenWidth - card.getWidth()) / 2, (screenHeight - card.getHeight()) / 2);
                        //opis karty
                        font.draw(batch, infomanager.getDescription(player.getCards(i)), 600 - 3 * infomanager.getDescription(player.getCards(i)).length(), 5 + screenHeight / 2);

                        batch.draw(cardChoices[0].getGraphic(), cardChoices[0].getPositionX(), cardChoices[0].getPositionY());
                        batch.draw(cardChoices[1].getGraphic(), cardChoices[1].getPositionX(), cardChoices[1].getPositionY());
                        batch.draw(cardChoices[2].getGraphic(), cardChoices[2].getPositionX(), cardChoices[2].getPositionY());
                        card = textures.getTexture(Card.CHOICE);
                        font.draw(batch, "Add Animal", ((screenWidth - card.getWidth()) / 2) - card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
                        font.draw(batch, "Add Perk 1", ((screenWidth - card.getWidth()) / 2) + 10, ((screenHeight - card.getHeight()) / 2) - 20);
                        font.draw(batch, "Add Perk 2", ((screenWidth - card.getWidth()) / 2) + card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
                    }
                }
            }

            //rysuj guzik do anulowania
            if(flagManager.printCancelButton) {
                batch.draw(cancelButton.getGraphic(), cancelButton.getPositionX(),cancelButton.getPositionY());
                font.draw(batch, "Cancel", cancelButton.getPositionX() + 30, cancelButton.getPositionY() + 30);
            }

            //rysuj podswietlone zwierze
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

            //rysuj opcje FEEDing faze
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


            // rysowanie pozycji na zwierzeta
            if (flagManager.printAnimalsSlots) {
                card = textures.getTexture(Card.SPACE);
                //rysuje miejsca na zwierzaka
                for (int i = 0; i < 5; i++) {
                    batch.draw(animalPlaces[i].getGraphic(), animalPlaces[i].getPositionX(), animalPlaces[i].getPositionY());
                }
            }
            //karty gracza
            for (int i = 0; i < player.cardsNumber(); i++) {
                if(cardButtons[i]!=null) {
                    batch.draw(cardButtons[i].getGraphic(), cardButtons[i].getPositionX(), cardButtons[i].getPositionY());
                }
            }

            //zwierzęta innych graczy
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


            //zwierzeta
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
        batch.end();
    }*/
}
