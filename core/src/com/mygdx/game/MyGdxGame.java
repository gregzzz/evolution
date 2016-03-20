package com.mygdx.game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.managers.GameManager;
import com.mygdx.game.managers.LayoutManager;
import com.mygdx.game.managers.TextureManager;
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
	Texture card;
	Button cardButtons[]=new Button[12];
	Button cardChoices[]=new Button[3];
	Button animalPlaces[]=new Button[5];



	Sprite sprite;
	BitmapFont font;
	int mouseClick[]=new int[2];
	GameManager gameManager=new GameManager();
	TextureManager textures;
	LayoutManager layout = new LayoutManager(textures);

	Player player;

	// ??
	int chosenCard=99;

	boolean chooseCardFromHand;
	boolean chooseAction;
	boolean chooseAnimalPlace;

	boolean printAnimalsSlots = false;
	boolean printChoosenCard = false;


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
		inputHandler();
		// piekne stany prosze z nich korzystac
		if(gameManager.state != GameState.WAIT) {
			drawGame();
		}

	}

	// zeby porzadeczek byl
	public void inputHandler(){
		chooseCardFromHand();
		chooseAction();
		chooseAnimalPlace();
	}
	public void createButtons(){
		card = textures.getTexture("choice");
		for (int j = -1; j < 2; j++) {
			cardChoices[j + 1] = new Button(card, ((screenWidth - card.getWidth()) / 2) + j * card.getWidth(), (screenHeight - card.getHeight()) / 2 - card.getHeight(),mouseClick[0],mouseClick[1]);

		}
		for (int i = 0; i < player.cardsNumber(); i++) {
			cardButtons[i] = new Button(textures.getTexture((String) player.getCards(i)), i * textures.getTexture((String) player.getCards(i)).getWidth(), 0, mouseClick[0], mouseClick[1]);
		}
		card = textures.getTexture("space");
		for (int i = -2; i < 3; i++) {
			animalPlaces[i+2]=new Button(card,((screenWidth - card.getWidth()) / 2) + i * card.getWidth(), 100,mouseClick[0],mouseClick[1]);
		}
	}

	public void chooseAnimalPlace(){
		if(!chooseAnimalPlace) {
			for (int i = 0; i < 5; i++) {
				if (animalPlaces[i].isTouched()) {
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
		// 99?
		//if(chosenCard!=99 && !chooseAction) {

			//klikniecie add animal
			getMouseInput();
			if (cardChoices[0].isTouched() && player.animals.size()<5) {
				chooseCardFromHand=true;
				chooseAnimalPlace=false;

				printChoosenCard = false;
				printAnimalsSlots = true;
		//	}
		}

	}

	public void chooseCardFromHand(){
		//wybor karty
		if(!chooseCardFromHand) {
			for (int i = 0; i < player.cardsNumber(); i++) {
				if (cardButtons[i].isTouched()) {
					chosenCard = i;
					chooseAction = false;
					printChoosenCard = true;
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
		// rysul tlo
		batch.draw(textures.getTexture("background-1"), 0, 0);
		batch.draw(textures.getTexture("background-2"), 0, 0);
		// guzik pass
		card = textures.getTexture("choice");
		Button pass=new Button(card,0,screenHeight-card.getHeight(),mouseClick[0],mouseClick[1]);
		batch.draw(pass.getGraphic(), pass.getPositionX(), pass.getPositionY());
		font.draw(batch, "Pass", 35, 5 + screenHeight-25);
		// rysuj wybor
		cardChoices[0] = new Button(card, ((screenWidth - card.getWidth()) / 2) + -1 * card.getWidth(), (screenHeight - card.getHeight()) / 2 - card.getHeight(),mouseClick[0],mouseClick[1]);

		if(printChoosenCard) {
			for (int i = 0; i < player.cardsNumber(); i++) {
				if (cardButtons[i].isTouched()) {
					card = textures.getTexture((String) player.getCards(i));
					batch.draw(card, (screenWidth - card.getWidth()) / 2, card.getHeight() + (screenHeight - card.getHeight()) / 2);
					//narysowanie ramki do tekstu
					card = textures.getTexture("ramka");
					batch.draw(card, (screenWidth - card.getWidth()) / 2, (screenHeight - card.getHeight()) / 2);
					//pobranie opisu musi byc w try-catchu
					// no to nie moze sie wykonywac co petle
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
					font.draw(batch, line, 600 - 3 * line.length(), 5 + screenHeight / 2);


					batch.draw(cardChoices[0].getGraphic(), cardChoices[0].getPositionX(), cardChoices[0].getPositionY());
					batch.draw(cardChoices[1].getGraphic(), cardChoices[1].getPositionX(), cardChoices[1].getPositionY());
					batch.draw(cardChoices[2].getGraphic(), cardChoices[2].getPositionX(), cardChoices[2].getPositionY());
					card = textures.getTexture("choice");
					font.draw(batch, "Add Animal", ((screenWidth - card.getWidth()) / 2) - card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
					font.draw(batch, "Add Perk 1", ((screenWidth - card.getWidth()) / 2) + 10, ((screenHeight - card.getHeight()) / 2) - 20);
					font.draw(batch, "Add Perk 2", ((screenWidth - card.getWidth()) / 2) + card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
				}
			}
		}
		// rysowanie pozycji na zwierzeta
		if (printAnimalsSlots) {
			card = textures.getTexture("space");
			//rysuje miejsca na zwierzaka

			for (int i = 0; i < 5; i++) {
				animalPlaces[i]=new Button(card,((screenWidth - card.getWidth()) / 2) + (i-2) * card.getWidth(), 100,mouseClick[0],mouseClick[1]);
				batch.draw(animalPlaces[i].getGraphic(), animalPlaces[i].getPositionX(), animalPlaces[i].getPositionY());
			}
		}
		//karty gracza
		for (int i = 0; i < player.cardsNumber(); i++) {
			cardButtons[i] = new Button(textures.getTexture((String) player.getCards(i)), i * textures.getTexture((String) player.getCards(i)).getWidth(), 0,mouseClick[0],mouseClick[1]);
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
		textures  = new TextureManager();

		createButtons();

	}



	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();

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