package com.mygdx.game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import components.Player;
import components.enums.GameState;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

public class MyGdxGame implements ApplicationListener {
	int screenWidth=1200;
	int screenHeight=800;

	String line;
	BufferedReader in;
	SpriteBatch batch;
	Texture background;
	Texture card;
	Button cardButtons[]=new Button[12];
	Button cardChoices[]=new Button[3];
	Button animalPlaces[]=new Button[5];



	Sprite sprite;
	BitmapFont font;
	int mouseClick[]=new int[2];
	GameManager gameManager=new GameManager();
	TextureManager textures;
	Player player;

	int chosenCard=99;

	boolean chooseCardFromHand;
	boolean chooseAction;
	boolean chooseAnimalPlace;

	public MyGdxGame(){
		gameManager.startClient();
		player = gameManager.player;
		chooseCardFromHand=false;
		chooseAction=true;
		chooseAnimalPlace=true;
	}

	//glowna petla
	@Override
	public void render () {
		getMouseInput();
		// piekne stany prosze z nich korzystac
		if(gameManager.state != GameState.WAIT){
			drawGame();
		}
		getMouseInput();
		chooseCardFromHand();
		getMouseInput();
		chooseAction();
		getMouseInput();
		chooseAnimalPlace();
	}

	public void chooseAnimalPlace(){
		if(chooseAnimalPlace==false) {
			getMouseInput();
			for (int i = 0; i < 5; i++) {
				if (animalPlaces[i].isTouched(mouseClick[0], mouseClick[1])) {
					//akcja guzika add animal


					player.addAnimal();
					player.removeCard(chosenCard);
					chooseAction=true;
					chooseAnimalPlace=true;
					chooseCardFromHand=false;
				}
			}
		}
	}

	public void chooseAction(){
		if(chosenCard!=99 && chooseAction==false) {
			card = new Texture("core/assets/choice.png");
			//klikniecie add animal
			if (cardChoices[0].isTouched(mouseClick[0], mouseClick[1])&&player.animals.size()<5) {
				chooseCardFromHand=true;

				batch.begin();
				card = textures.getTexture("space");
				//rysuje miejsca na zwierzaka
				for (int i = -2; i < 3; i++) {
					animalPlaces[i+2]=new Button(card,((screenWidth - card.getWidth()) / 2) + i * card.getWidth(), 100);
					batch.draw(animalPlaces[i+2].getGraphic(), animalPlaces[i+2].getPositionX(), animalPlaces[i+2].getPositionY());
				}
				batch.end();
				chooseAnimalPlace=false;

			}
		}

	}

	public void chooseCardFromHand(){
		//wybor karty
		if(chooseCardFromHand==false) {
			for (int i = 0; i < player.cardsNumber(); i++) {
				if (cardButtons[i].isTouched(mouseClick[0], mouseClick[1])) {
					batch.begin();
					//narysowanie wybranej karty
					card = textures.getTexture((String) player.getCards(i));
					batch.draw(card, (screenWidth - card.getWidth()) / 2, card.getHeight() + (screenHeight - card.getHeight()) / 2);
					//narysowanie ramki do tekstu
					card = new Texture("core/assets/ramka.png");
					batch.draw(card, (screenWidth - card.getWidth()) / 2, (screenHeight - card.getHeight()) / 2);
					//pobranie opisu musi byc w try-catchu
					try {
						in = new BufferedReader(new FileReader("core/assets/" + player.getCards(i) + ".txt"));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					try {
						line = in.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					//tekst mniej wiecej na srodku
					font.draw(batch, line, 600 - 3 * line.length(), 5 + screenHeight / 2);
					card = textures.getTexture("choice");
					//rysowanie wyborow
					for (int j = -1; j < 2; j++) {
						cardChoices[j + 1] = new Button(card, ((screenWidth - card.getWidth()) / 2) + j * card.getWidth(), (screenHeight - card.getHeight()) / 2 - card.getHeight());
						batch.draw(cardChoices[j + 1].getGraphic(), cardChoices[j + 1].getPositionX(), cardChoices[j + 1].getPositionY());
					}
					font.draw(batch, "Add Animal", ((screenWidth - card.getWidth()) / 2) - card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
					font.draw(batch, "Add Perk 1", ((screenWidth - card.getWidth()) / 2) + 10, ((screenHeight - card.getHeight()) / 2) - 20);
					font.draw(batch, "Add Perk 2", ((screenWidth - card.getWidth()) / 2) + card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
					batch.end();
					chosenCard = i;
					chooseAction = false;

				}
			}
		}
	}


	//uzupelnia tablice mouseInput wspolrzednymi myszki
	private boolean getMouseInput(){
		if(Gdx.input.isTouched()){
			mouseClick[0]=(int)(((double)Gdx.input.getX())/((double)Gdx.graphics.getWidth()/screenWidth));
			mouseClick[1]=(int)(((double)Gdx.graphics.getHeight()-(double)Gdx.input.getY())/((double)Gdx.graphics.getHeight()/screenHeight));
		}

		return true;
	}

	public void drawGame(){
	//najpierw tlo
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background = new Texture("core/assets/bg.png");
		batch.draw(background, 0, 0);
		background = new Texture("core/assets/bg2.png");
		batch.draw(background, 0, 0);
		//guzik pass
		card = textures.getTexture("choice");
		Button pass=new Button(card,0,screenHeight-card.getHeight());
		batch.draw(pass.getGraphic(), pass.getPositionX(), pass.getPositionY());
		font.draw(batch, "Pass", 35, 5 + screenHeight-25);
		if(pass.isTouched(mouseClick[0],mouseClick[1])){
			//akcja guzika pass



		}

		//karty gracza
		for (int i = 0; i < player.cardsNumber(); i++) {
			cardButtons[i] = new Button(textures.getTexture((String) player.getCards(i)), i * textures.getTexture((String) player.getCards(i)).getWidth(), 0);
			batch.draw(cardButtons[i].getGraphic(), cardButtons[i].getPositionX(), cardButtons[i].getPositionY());
		}
			//zwierzeta
		card = textures.getTexture("animal");
		for (int i = -2; i < player.animals.size()-2; i++) {
			batch.draw(card,((screenWidth - card.getWidth()) / 2) + i * card.getWidth(), 100);
		}
		batch.end();


	}



	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.GREEN);
		background=new Texture("core/assets/bg.png");
		card=new Texture("core/assets/animal.png");
		textures  = new TextureManager();


	}



	@Override
	public void dispose() {
		batch.dispose();
		background.dispose();
		font.dispose();
		card.dispose();

		// tutaj jeszcze poprawic to textures textures
		for (Map.Entry<String, Texture> entry: textures.textures.entrySet() ){
			entry.getValue().dispose();
		}
	}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}

}