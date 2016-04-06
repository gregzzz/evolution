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
import com.mygdx.game.managers.InfoManager;
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
	Button feedChoices[]=new Button[6];
	Button animalPlaces[]=new Button[5];
	Button animalButtons[][]=new Button[4][5];
	Button endRound;
	Button pass;




	private Mouse mouse = new Mouse();
	private Keyboard keyboard = new Keyboard();

	GameManager gameManager=new GameManager();
	TextureManager textures;
	LayoutManager layout = new LayoutManager(textures);
	InfoManager infomanager = new InfoManager();

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
	boolean chooseAnimalForAction;
	boolean chooseAnimalAction;
	boolean chooseTarget;

	boolean printAnimalsSlots = false;
	boolean printChoosenCard = false;
	boolean printSelectedAnimal=false;
	boolean printFeedingChoices=false;


	public Evolution(){
		gameManager.startClient();
		player = gameManager.player;

		chooseCardFromHand=false;
		chooseAction=true;
		chooseAnimalPlace=true;
		chooseMyAnimal=true;
		chooseAnimalForAction=false;
		chooseAnimalAction=true;
		chooseTarget=true;


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
		if(gameManager.state != GameState.WAIT) {
			drawGame();
		}

	}

	public void createButtons(){
		card = textures.getTexture(Card.CHOICE);
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

	//aktualizuje wszystkie przyciski zwierzat, wywolywane jesli kogos zabiles i na poczatku kazdej tury
	public void updateAnimalButtons(){
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

	//wybierz zwierze podczas fazy zywienia
	public void chooseAnimalForAction(){
		if(!chooseAnimalForAction){
			for(int i=0;i<5;i++) {
				if(animalButtons[0][i]!=null && animalButtons[0][i].isTouched(mouse)){
					printSelectedAnimal=true;
					selectedAnimal=i;
					printFeedingChoices=true;
					chooseAnimalAction=false;
				}
			}
			if(pass.isTouched(mouse)&&player.animalsNumber()>0){
				gameManager.pass();
				printSelectedAnimal=false;
				printFeedingChoices=false;
				chosenCard=99;
			}
		}
	}

	//wybierz co robisz zwirzeciem podczas fazy zywienia
	public void chooseAnimalAction(){
		if(!chooseAnimalAction){
			if(feedChoices[0].isTouched(mouse)&&!player.animals[selectedAnimal].isFeeded()){
				player.animals[selectedAnimal].feed(1);
				gameManager.feed(selectedAnimal,1);
			}else if(feedChoices[1].isTouched(mouse) && player.animals[selectedAnimal].carnivore&&!player.animals[selectedAnimal].isFeeded()){
				chooseTarget=false;
				chooseAnimalForAction=true;
				chooseAnimalAction=true;
				printFeedingChoices=false;
			}
		}
	}

	//wybierz zwierze do ataku i zaatakuj
	public void chooseTarget(){
		if(!chooseTarget){
			for(int i=0;i<4;i++) {
				for (int j = 0; j < 5; j++) {
					if (animalButtons[i][j] != null && animalButtons[i][j].isTouched(mouse)) {
						if(i>0) {
							otherPlayer = gameManager.otherPlayers.elementAt(i - 1);
						}else{
							otherPlayer=player;
						}
						if(otherPlayer.animals[j]!=player.animals[selectedAnimal] && otherPlayer.animals[j].canBeAttacked(player.animals[selectedAnimal])){
							if(player.animals[selectedAnimal].attack(otherPlayer.animals[j])!=0){
								player.animals[selectedAnimal].feed(2);
								gameManager.feed(selectedAnimal,2);
								otherPlayer.killAnimal(j);
								gameManager.kill(i,j);
								chooseTarget=true;
								printSelectedAnimal=false;
								chooseAnimalForAction=false;
								updateAnimalButtons();
							}else{
								chooseTarget=true;
								printSelectedAnimal=false;
								chooseAnimalForAction=false;
							}
						}
					}
				}
			}
		}
	}

	//wybierz gdzie chcesz postawic nowe zwierze
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

	//wybierz zwierze ktoremu chcesz dodac ceche
	public void chooseMyAnimal(){
		if(!chooseMyAnimal){
			for(int i=0;i<5;i++) {
				if(animalButtons[0][i]!=null && animalButtons[0][i].isTouched(mouse)){
					if(!secondaryPerk) {
						player.animals[i].addFeature(player.getCards(chosenCard));
						gameManager.addFeature(i, player.getCards(chosenCard));
						player.removeCard(chosenCard);

						chooseCardFromHand = false;
						chooseMyAnimal = true;

					}else{
						if(player.getCards(chosenCard)==Card.MASSIVEC || player.getCards(chosenCard)==Card.PARASITEC || player.getCards(chosenCard)==Card.COOPERATIONC || player.getCards(chosenCard)==Card.COMMUNICATION || player.getCards(chosenCard)==Card.TOXIC || player.getCards(chosenCard)==Card.HIBERNATION){
							if(!player.animals[i].carnivore) {
								player.animals[i].addFeature(Card.CARNIVORE);
								gameManager.addFeature(i, Card.CARNIVORE);
								player.removeCard(chosenCard);
								chooseCardFromHand = false;
								chooseMyAnimal = true;
							}
						}else if(player.getCards(chosenCard)==Card.MASSIVEF || player.getCards(chosenCard)==Card.PARASITEF ||  player.getCards(chosenCard)==Card.COOPERATIONF ||  player.getCards(chosenCard)==Card.CAMOUFLAGE || player.getCards(chosenCard)==Card.ROAR){
							player.animals[i].addFeature(Card.FAT);
							gameManager.addFeature(i, Card.FAT);
							chooseCardFromHand = false;
							chooseMyAnimal = true;
						}else{
							chooseCardFromHand = false;
							chooseMyAnimal = true;
						}

					}

				}
			}
		}
	}

	//wybierz co chcesz zrobic z kartą
	public void chooseAction(){
		if(chosenCard!=99 && !chooseAction) {
			if (cardChoices[0].isTouched(mouse) && player.animalsNumber()<5) {
				chooseCardFromHand=true;
				chooseAnimalPlace=false;
				chooseAction=true;
				printChoosenCard = false;
				printAnimalsSlots = true;
			}
			else if (cardChoices[1].isTouched(mouse) && player.animalsNumber()>0){
				secondaryPerk=false;
				chooseAction=true;
				chooseCardFromHand=true;
				printChoosenCard=false;
				chooseMyAnimal=false;
			}
			else if (cardChoices[2].isTouched(mouse) && player.animalsNumber()>0){
				secondaryPerk=true;
				chooseAction=true;
				chooseCardFromHand=true;
				printChoosenCard=false;
				chooseMyAnimal=false;
			}
		}

	}

	//wybierz karte z łapy albo spasuj
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



	public void drawGame(){
	//najpierw tlo
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			if(gameManager.turnStart){
				gameManager.turnStart=false;
				updateAnimalButtons();
			}

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

				//rysuj opcje FEEDing faze
				if (printFeedingChoices) {
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
		mouse.x = (int)(((double)screenX)/((double)Gdx.graphics.getWidth()/screenWidth));
		mouse.y = (int)(((double)Gdx.graphics.getHeight()-(double)screenY)/((double)Gdx.graphics.getHeight()/screenHeight));
		if(gameManager.turn==player.number && gameManager.state==GameState.EVOLUTION){
			chooseCardFromHand();
			chooseAction();
			chooseAnimalPlace();
			chooseMyAnimal();
		}
		if(gameManager.turn==player.number && gameManager.state==GameState.FEEDING){
			chooseAnimalForAction();
			chooseAnimalAction();
			chooseTarget();
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