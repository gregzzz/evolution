package com.mygdx.game;



import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.logic.Button;
import com.mygdx.game.logic.Keyboard;
import com.mygdx.game.logic.Mouse;
import com.mygdx.game.managers.GameManager;
import com.mygdx.game.managers.LayoutManager;
import com.mygdx.game.managers.TextureManager;
import components.objects.Player;
import components.enums.GameState;
import components.enums.Card;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;



public class Evolution implements ApplicationListener, InputProcessor {
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
	Button animalPlaces[]=new Button[5];
	Button animalButtons[][]=new Button[4][5];
	Button endRound;
	Button pass;




	private Mouse mouse = new Mouse();
	private Keyboard keyboard = new Keyboard();

	GameManager gameManager=new GameManager();
	TextureManager textures;
	LayoutManager layout = new LayoutManager(textures);

	Player player;
	Player otherPlayer;

	// zmienne do do funkcji z uzyciem kart i wybranego zwierzęcia
	int chosenCard=99;
	int selectedAnimal=99;

	boolean secondaryPerk;

	boolean getText = false;

	boolean chooseCardFromHand;
	boolean chooseAction;
	boolean chooseAnimalPlace;
	boolean chooseMyAnimal;

	boolean printAnimalsSlots = false;
	boolean printChoosenCard = false;
	boolean printSelectedAnimal=false;


	public Evolution(){
		gameManager.startClient();
		player = gameManager.player;

		chooseCardFromHand=false;
		chooseAction=true;
		chooseAnimalPlace=true;
		chooseMyAnimal=true;


	}
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.GREEN);

		textures  = new TextureManager();
		card = textures.getTexture(Card.CHOICE);
		endRound=new Button(card,screenWidth-card.getWidth(),screenHeight-card.getHeight(),mouse.x,mouse.y);
		pass=new Button(card,0,screenHeight-card.getHeight(),mouse.x,mouse.y);

		createButtons();

		Gdx.input.setInputProcessor(this);
	}

	//glowna petla
	@Override
	public void render () {
		// piekne stany prosze z nich korzystac
		if(gameManager.state != GameState.WAIT) {
			drawGame();
		}

	}

	public void createButtons(){
		card = textures.getTexture(Card.CHOICE);
		for (int i = 0; i < 3; i++) {
			cardChoices[i] = new Button(card, ((screenWidth - card.getWidth()) / 2) + (i-1) * card.getWidth(), (screenHeight - card.getHeight()) / 2 - card.getHeight(),mouse.x,mouse.y);

		}
		for (int i = 0; i < player.cardsNumber(); i++) {
			cardButtons[i] = new Button(textures.getTexture( player.getCards(i)), i * textures.getTexture(player.getCards(i)).getWidth(), 0, mouse.x, mouse.y);
		}
		card = textures.getTexture(Card.SPACE);
		for (int i = 0; i < 5; i++) {
			animalPlaces[i]=new Button(card,((screenWidth - card.getWidth()) / 2) + (i-2) * card.getWidth(), 100,mouse.x,mouse.y);
		}
	}

	public void chooseAnimalPlace(){
		if(!chooseAnimalPlace) {
			for (int i = 0; i < 5; i++) {
				if (animalPlaces[i].isTouched(mouse) && player.animals[i]==null) {
					//akcja guzika add animal
					player.addAnimal(i);
					player.removeCard(chosenCard);
					gameManager.addAnimal(i);
					animalButtons[0][i]=new Button(textures.getTexture(Card.ANIMAL),animalPlaces[i].getPositionX(),animalPlaces[i].getPositionY());

					chooseAction=true;
					chooseAnimalPlace=true;
					chooseCardFromHand=false;

					printAnimalsSlots = false;
				}
			}
		}
	}

	public void chooseMyAnimal(){
		if(!chooseMyAnimal){
			for(int i=0;i<5;i++) {
				if(animalButtons[0][i]!=null && animalButtons[0][i].isTouched(mouse)){
					player.animals[i].addFeature(player.getCards(chosenCard));
					gameManager.addFeature(i,player.getCards(chosenCard));
					player.removeCard(chosenCard);

					chooseCardFromHand=false;
					chooseMyAnimal=true;

				}
			}
		}
	}

	public void chooseAction(){
		// 99?
		if(chosenCard!=99 && !chooseAction) {
			if (cardChoices[0].isTouched(mouse) && player.animalsNumber()<5) {
				chooseCardFromHand=true;
				chooseAnimalPlace=false;
				chooseAction=true;
				printChoosenCard = false;
				printAnimalsSlots = true;
			}
			else if (cardChoices[1].isTouched(mouse) && player.animalsNumber()>0){
				chooseAction=true;
				chooseCardFromHand=true;
				printChoosenCard=false;
				chooseMyAnimal=false;
			}
			else if (cardChoices[2].isTouched(mouse) && player.animalsNumber()>0){
				chooseAction=true;
				chooseCardFromHand=true;
				printChoosenCard=false;
				chooseMyAnimal=false;


			}
		}

	}

	public void chooseCardFromHand(){
		//wybor karty
		if(!chooseCardFromHand) {
			for (int i = 0; i < player.cardsNumber(); i++) {
				if (cardButtons[i].isTouched(mouse)) {
					chosenCard = i;
					chooseAction = false;
					printChoosenCard = true;
					printSelectedAnimal=false;
				}
			}
			if(pass.isTouched(mouse)&&player.animalsNumber()>0){
				gameManager.pass();
				printSelectedAnimal=false;
				chosenCard=99;
			}
			for(int i=0;i<5;i++) {
				if(animalButtons[0][i]!=null && animalButtons[0][i].isTouched(mouse)){
					printSelectedAnimal=true;
					selectedAnimal=i;
				}
			}
		}
	}


	//uzupelnia tablice mouseInput wspolrzednymi myszki

	public void drawGame(){
	//najpierw tlo
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.begin();
			// rysul tlo
			batch.draw(textures.getTexture(Card.BACKGROUND1), 0, 0);
			batch.draw(textures.getTexture(Card.BACKGROUND2), 0, 0);

		//czja tura
			if (gameManager.state == GameState.EVOLUTION || gameManager.state == GameState.FEEDING) {

				card = textures.getTexture(Card.CHOICE);
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
				if (printChoosenCard) {
					for (int i = 0; i < player.cardsNumber(); i++) {
						if (cardButtons[i].isTouched(mouse)) {
							card = textures.getTexture(player.getCards(i));
							batch.draw(card, (screenWidth - card.getWidth()) / 2, card.getHeight() + (screenHeight - card.getHeight()) / 2);
							//narysowanie ramki do tekstu
							card = textures.getTexture(Card.RAMKA);
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
							card = textures.getTexture(Card.CHOICE);
							font.draw(batch, "Add Animal", ((screenWidth - card.getWidth()) / 2) - card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
							font.draw(batch, "Add Perk 1", ((screenWidth - card.getWidth()) / 2) + 10, ((screenHeight - card.getHeight()) / 2) - 20);
							font.draw(batch, "Add Perk 2", ((screenWidth - card.getWidth()) / 2) + card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
						}
					}
				}
				//rysuj podswietlone zwierze
				if (printSelectedAnimal) {
					for (int i = 0; i < player.animals[selectedAnimal].features.size(); i++) {
						card = textures.getTexture(player.animals[selectedAnimal].getFeature(i));
						if (player.animals[selectedAnimal].features.size() % 2 == 0) {
							batch.draw(card, ((screenWidth - player.animals[selectedAnimal].features.size()) / 2) + card.getWidth() * (i - (player.animals[selectedAnimal].features.size()) / 2), card.getHeight() + (screenHeight - card.getHeight()) / 2);
						} else {
							batch.draw(card, ((screenWidth - player.animals[selectedAnimal].features.size()) / 2) + card.getWidth() * (i - (player.animals[selectedAnimal].features.size()) / 2) - card.getWidth() / 2, card.getHeight() + (screenHeight - card.getHeight()) / 2);
						}
					}
				}

				// rysowanie pozycji na zwierzeta
				if (printAnimalsSlots) {
					card = textures.getTexture(Card.SPACE);
					//rysuje miejsca na zwierzaka
					for (int i = 0; i < 5; i++) {
						animalPlaces[i] = new Button(card, ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth(), 100, mouse.x, mouse.y);
						batch.draw(animalPlaces[i].getGraphic(), animalPlaces[i].getPositionX(), animalPlaces[i].getPositionY());
					}
				}
				//karty gracza
				for (int i = 0; i < player.cardsNumber(); i++) {
					cardButtons[i] = new Button(textures.getTexture(player.getCards(i)), i * textures.getTexture(player.getCards(i)).getWidth(), 0, mouse.x, mouse.y);
					batch.draw(cardButtons[i].getGraphic(), cardButtons[i].getPositionX(), cardButtons[i].getPositionY());
				}

				//zwierzęta innych graczy
				card = textures.getTexture(Card.ANIMAL);
				if (gameManager.otherPlayers.size() > 0) {
					otherPlayer = gameManager.otherPlayers.elementAt(0);
					for (int i = 0; i < 5; i++) {
						if (otherPlayer.animals[i] != null) {
							batch.draw(card, ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth(), screenHeight - card.getHeight());
						}
					}
				}
				if (gameManager.otherPlayers.size() > 1) {
					otherPlayer = gameManager.otherPlayers.elementAt(1);
					for (int i = 0; i < 5; i++) {
						if (otherPlayer.animals[i] != null) {
							batch.draw(card, 0, (screenHeight + 100 - card.getHeight()) / 2 + (i - 2) * card.getHeight());
						}
					}
				}
				if (gameManager.otherPlayers.size() > 2) {
					otherPlayer = gameManager.otherPlayers.elementAt(2);
					for (int i = 0; i < 5; i++) {
						if (otherPlayer.animals[i] != null) {
							batch.draw(card, screenWidth - card.getWidth(), (screenHeight + 100 - card.getHeight()) / 2 + (i - 2) * card.getHeight());
						}
					}
				}

				//zwierzeta
				card = textures.getTexture(Card.ANIMAL);
				for (int i = 0; i < 5; i++) {
					if (player.animals[i] != null) {
						batch.draw(card, ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth(), 100);
					}
				}
			}
			batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();

		// tutaj jeszcze poprawic to textures textures
		for (Map.Entry<Card, Texture> entry: textures.textures.entrySet() ){
			entry.getValue().dispose();
		}
	}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(getText){
			if((int) character == 10  || (int) character == 13){
				System.out.println(keyboard.getText());
				keyboard.clear();
				return true;
			}
			else{
				keyboard.addChar(character);
			}
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mouse.x = screenX;
		mouse.y = Gdx.graphics.getHeight() - screenY;
		if(gameManager.turn==player.number){
			chooseCardFromHand();
			chooseAction();
			chooseAnimalPlace();
			chooseMyAnimal();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}


}