package com.mygdx.game;



import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.logic.*;
import com.mygdx.game.managers.*;
import components.enums.Card;
import components.enums.GameState;
import components.objects.Player;

import java.io.BufferedReader;

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




	private Mouse mouse = new Mouse();
	private Keyboard keyboard = new Keyboard();

	FlagManager flagManager=new FlagManager();
	GameManager gameManager=new GameManager();
	TextureManager textures;
	AutoPlayer autoPlayer;
	LayoutManager layout = new LayoutManager(textures);
	InfoManager infomanager = new InfoManager();
	ButtonManager buttonManager;
	MyTextInputListener listener=new MyTextInputListener(flagManager);
	PlayerAction playerAction;

	Player player;
	Player otherPlayer;


	public Evolution(){
	}
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.GREEN);

		textures  = new TextureManager();
		player = gameManager.player;
		buttonManager=new ButtonManager(gameManager,textures,screenWidth,screenHeight);
		card = textures.getTexture(Card.CHOICE);
		buttonManager.endRound=new Button(card,screenWidth-card.getWidth(),screenHeight-card.getHeight(),mouse.x,mouse.y);
		buttonManager.pass=new Button(card,0,screenHeight-card.getHeight(),mouse.x,mouse.y);


		buttonManager.createButtons();
		playerAction=new PlayerAction(flagManager, gameManager, mouse, buttonManager);
		autoPlayer=new AutoPlayer(playerAction);

		Gdx.input.setInputProcessor(this);
	}

	//glowna petla
	@Override
	public void render () {
		try
		{
			Thread.sleep((long)(1000/10-Gdx.graphics.getDeltaTime()));
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		switch(gameManager.state){
			case LOGIN:
				drawLogin();
				break;
			case SERVER:
				drawServer();
				break;
			case GAMEOVER:
				drawGameOver();
				break;
			case BEGIN:
				drawMenu();
				break;
			default:
				autoPlayer.feedingPhasePass();
				drawGame();
				break;

		}
	}

	//ramka z napisem
	public void drawMessage(String text){
		card = textures.getTexture(Card.RAMKA);
		batch.draw(card, (screenWidth - card.getWidth()) / 2, (screenHeight - card.getHeight()) / 2);
		font.draw(batch, text ,(screenWidth - card.getWidth()) / 2 + 15, (screenHeight - card.getHeight()) / 2 + 30 );
	}

    public void drawYourTurnMessage(){
		if(flagManager.printTurnMessage) {
			String yourTurnMessage;
			if (gameManager.state == GameState.EVOLUTION) {
				yourTurnMessage = "Your turn, EVOLUTION PHASE";
			} else {
				yourTurnMessage = "Your turn, FEEDING PHASE";
			}
			drawMessage(yourTurnMessage);
		}
    }

	public void startOfTurnCleanup(){
		gameManager.turnStart=false;
		flagManager.actionDone=false;
		flagManager.printTurnMessage=true;

		autoPlayer.evolutionPhasePass();

        //odblokowanie szybkosci
        for(int i=0;i<gameManager.otherPlayers.size();i++) {
            otherPlayer = gameManager.otherPlayers.elementAt(i);
            for (int j = 0; j < 5; j++) {
                if (otherPlayer.animals[j] != null) {
                    otherPlayer.animals[j].resetSpeedData();
                }
            }
        }

		//odblokowanie cech
		for(int i=0;i<5;i++){
			if(player.animals[i]!=null) {
				player.animals[i].piracy = false;
				player.animals[i].scavenger = false;
				player.animals[i].pasturage = false;
				for(int j=0;j<2;j++) {
					player.animals[i].commUsed[j] = false;
					player.animals[i].coopUsed[j]=false;
				}
				player.animals[i].foodRecieved = false;
				player.animals[i].realFoodRecieved = false;
				if (player.animals[i].hibernationUsed = false) {
					player.animals[i].hibernation = false;
				}
				if (player.animals[i].hibernationUsed = true) {
					player.animals[i].hibernationUsed = false;
				}
			}
		}
	}

	public void drawGameOver(){
		//najpierw tlo
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		int mostPoints=0;
		Player winner=player;
		batch.begin();
		// rysul tlo
		batch.draw(textures.getTexture(Card.BACKGROUND1), 0, 0);
		batch.draw(textures.getTexture(Card.BACKGROUND2), 0, 0);

		batch.draw(buttonManager.cancelButton.getGraphic(), buttonManager.cancelButton.getPositionX(), buttonManager.cancelButton.getPositionY());
		font.draw(batch, "End Game", buttonManager.cancelButton.getPositionX() + 15, buttonManager.cancelButton.getPositionY() + 30);

		int playerPoints[]=new int[gameManager.otherPlayers.size()+1];

		//jesli pierwsze powtorzenie tej petli
		if(gameManager.turnStart){
			gameManager.hungerDeaths();
			gameManager.turnStart=false;
		}

		//liczenie punktow graczy
		mostPoints+=player.animalsNumber();
		for(int i=0;i<5;i++) {
			if (player.animals[i]!=null){
				mostPoints += player.animals[i].features.size();
				mostPoints += player.animals[i].foodNeeded;
				if(player.animals[i].commWith[1]!=null)mostPoints++;
				if(player.animals[i].coopWith[1]!=null)mostPoints++;
				if(player.animals[i].symbiosis[1]!=null)mostPoints++;
			}
		}
		playerPoints[0]=mostPoints;

		for(int j=0;j<gameManager.otherPlayers.size();j++) {
			otherPlayer = gameManager.otherPlayers.elementAt(j);
			playerPoints[j+1]+=otherPlayer.animalsNumber();
			for(int i=0;i<5;i++) {
				if (otherPlayer.animals[i]!=null){
					playerPoints[j+1] += otherPlayer.animals[i].features.size();
					playerPoints[j+1] += otherPlayer.animals[i].foodNeeded;
					if(otherPlayer.animals[i].commWith[1]!=null)mostPoints++;
					if(otherPlayer.animals[i].coopWith[1]!=null)mostPoints++;
					if(otherPlayer.animals[i].symbiosis[1]!=null)mostPoints++;
				}
			}
			if(playerPoints[j+1]>mostPoints){
				mostPoints=playerPoints[j+1];
				winner=otherPlayer;
			}
		}

		String message="Winner: "+winner.name+". Your points: "+Integer.toString(playerPoints[0]);
		for(int i=0;i<gameManager.otherPlayers.size();i++){
			message+=", "+gameManager.otherPlayers.elementAt(i).name+": "+Integer.toString(playerPoints[i+1])+" points";
		}
		drawMessage(message);
		flagManager.printCancelButton=true;


		batch.end();
	}

	public void drawMenu(){
		//najpierw tlo
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		// rysul tlo
		batch.draw(textures.getTexture(Card.BACKGROUND1), 0, 0);
		batch.draw(textures.getTexture(Card.BACKGROUND2), 0, 0);

		for (int i = 0; i < 6; i++) {
			batch.draw(buttonManager.menuButtons[i].getGraphic(), buttonManager.menuButtons[i].getPositionX(), buttonManager.menuButtons[i].getPositionY());
		}
		font.draw(batch, "Login", buttonManager.menuButtons[0].getPositionX() + 15, buttonManager.menuButtons[0].getPositionY() + 30);
		font.draw(batch, "Register", buttonManager.menuButtons[1].getPositionX() + 15, buttonManager.menuButtons[1].getPositionY() + 30);
		font.draw(batch, "Find Game", buttonManager.menuButtons[2].getPositionX() + 15, buttonManager.menuButtons[2].getPositionY() + 30);
		font.draw(batch, "Server", buttonManager.menuButtons[3].getPositionX() + 15, buttonManager.menuButtons[3].getPositionY() + 30);
		font.draw(batch, "Help", buttonManager.menuButtons[4].getPositionX() + 15, buttonManager.menuButtons[4].getPositionY() + 30);
		font.draw(batch, "Credits", buttonManager.menuButtons[5].getPositionX() + 15, buttonManager.menuButtons[5].getPositionY() + 30);

		//szukanie gry...
		if(flagManager.lookingForGames){
			drawMessage("Looking for a game...");
		}

		batch.end();
	}

	public void drawLogin(){
		if(flagManager.login) {
			flagManager.inputed=false;
			flagManager.login=false;
			Gdx.input.getTextInput(listener, "Username:", gameManager.configuration.playerName, "");
			flagManager.password=true;
		}
		if(flagManager.password && flagManager.inputed) {
			if(listener.getIntputedText()!=null) {
				gameManager.configuration.playerName = listener.getIntputedText();
			}
			flagManager.inputed=false;
			flagManager.password=false;
			Gdx.input.getTextInput(listener, "Password:", "Password", "");
		}
		if(flagManager.inputed && !flagManager.password && !flagManager.login){
			listener.setIntputedText(null);
			gameManager.state=GameState.BEGIN;
			flagManager.chooseMainMenuOption=false;

		}
	}

	//wpisz adres serwera
	public void drawServer(){
		if(flagManager.server) {
			flagManager.inputed=false;
			flagManager.server=false;
			Gdx.input.getTextInput(listener, "Server Adress:", gameManager.configuration.serverAdress, "");
		}
		if(flagManager.inputed && !flagManager.server){
			if(listener.getIntputedText()!=null) {
				gameManager.configuration.serverAdress = listener.getIntputedText();
			}
			listener.setIntputedText(null);
			gameManager.state=GameState.BEGIN;
			flagManager.chooseMainMenuOption=false;

		}
	}

	//czy uzyc cechy podwojnej w lewo czy w prawo
	public void askDouble(){
		if(flagManager.askComm || flagManager.askCoop){
			if(flagManager.askComm){
				drawMessage("Use communication with animal on the left or right?");
			}else{
				drawMessage("Use cooperation with animal on the left or right?");
			}
			if((!flagManager.askComm&&gameManager.player.animals[playerAction.selectedAnimal].coopWith[0]!=null)||(flagManager.askComm&&gameManager.player.animals[playerAction.selectedAnimal].commWith[0]!=null)) {
				batch.draw(buttonManager.yes.getGraphic(), buttonManager.yes.getPositionX(), buttonManager.yes.getPositionY());
				font.draw(batch, "Left", buttonManager.yes.getPositionX() + 45, buttonManager.yes.getPositionY() + 30);
			}
			if((!flagManager.askComm&&gameManager.player.animals[playerAction.selectedAnimal].coopWith[1]!=null)||(flagManager.askComm&&gameManager.player.animals[playerAction.selectedAnimal].commWith[1]!=null)) {
				batch.draw(buttonManager.no.getGraphic(), buttonManager.no.getPositionX(), buttonManager.no.getPositionY());
				font.draw(batch, "Right", buttonManager.no.getPositionX() + 45, buttonManager.no.getPositionY() + 30);
			}
		}
	}

	//ilosc jedzenia
	public void drawFoodAmount(){
		card=textures.getTexture(Card.CHOICE);
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
	}

	//czyja tura
	public void drawWhoseTurn(){
		card=textures.getTexture(Card.CHOICE);
		batch.draw(card, screenWidth - 2 * card.getWidth(), screenHeight - card.getHeight());
		if (gameManager.turn == player.number) {
			font.draw(batch, "Your Turn", screenWidth - 2 * card.getWidth() + 15, 5 + screenHeight - 25);
		} else {
			for (Player player : gameManager.otherPlayers) {
				if (player.number == gameManager.turn) {
					if (player.name != null) {
						font.draw(batch, player.name, screenWidth - 2 * card.getWidth() + 15, 5 + screenHeight - 25);
					}
				}
			}
		}
	}

	//guziki End Round i Pass
	public void drawEndRoundAndPass() {
		batch.draw(buttonManager.pass.getGraphic(), buttonManager.pass.getPositionX(), buttonManager.pass.getPositionY());
		font.draw(batch, "Pass", 35, 5 + screenHeight - 25);
		batch.draw(buttonManager.endRound.getGraphic(), buttonManager.endRound.getPositionX(), buttonManager.endRound.getPositionY());
		font.draw(batch, "End Round", screenWidth - card.getWidth() + 15, 5 + screenHeight - 25);
	}

	// rysuj wybor
	public void drawSelectedCard(){
		if (flagManager.printChoosenCard) {
			for (int i = 0; i < player.cardsNumber(); i++) {
				if (buttonManager.cardButtons[i]!=null && buttonManager.cardButtons[i].isTouched(mouse)) {
                    card = textures.getTexture(player.getCards(i));
                    batch.draw(card, (screenWidth - card.getWidth()) / 2, card.getHeight() + (screenHeight - card.getHeight()) / 2);
                    //opis karty
                    drawMessage(infomanager.getDescription(player.getCards(i)));

                    //opcje rysujemy tylko podczas kolejki gracza
                    if (player.number == gameManager.turn&&gameManager.state== GameState.EVOLUTION) {
                        batch.draw(buttonManager.cardChoices[0].getGraphic(), buttonManager.cardChoices[0].getPositionX(), buttonManager.cardChoices[0].getPositionY());
                        batch.draw(buttonManager.cardChoices[1].getGraphic(), buttonManager.cardChoices[1].getPositionX(), buttonManager.cardChoices[1].getPositionY());
                        batch.draw(buttonManager.cardChoices[2].getGraphic(), buttonManager.cardChoices[2].getPositionX(), buttonManager.cardChoices[2].getPositionY());
                        card = textures.getTexture(Card.CHOICE);
                        font.draw(batch, "Add Animal", ((screenWidth - card.getWidth()) / 2) - card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
                        font.draw(batch, "Add Perk 1", ((screenWidth - card.getWidth()) / 2) + 10, ((screenHeight - card.getHeight()) / 2) - 20);
                        font.draw(batch, "Add Perk 2", ((screenWidth - card.getWidth()) / 2) + card.getWidth() + 10, ((screenHeight - card.getHeight()) / 2) - 20);
                    }
                }
			}
		}
	}

	//rysuj guzik do anulowania
	public void drawCancelButton(){
		if(flagManager.printCancelButton) {
			batch.draw(buttonManager.cancelButton.getGraphic(), buttonManager.cancelButton.getPositionX(),buttonManager.cancelButton.getPositionY());
			font.draw(batch, "Cancel", buttonManager.cancelButton.getPositionX() + 30, buttonManager.cancelButton.getPositionY() + 30);
		}
	}

	//rysuj wybrane zwierze
	public void drawSelectedAnimal(){
		if (flagManager.printSelectedAnimal) {
			if(playerAction.otherPlayer.animals[playerAction.selectedAnimal]!=null) {
				for (int i = 0; i < playerAction.otherPlayer.animals[playerAction.selectedAnimal].features.size(); i++) {
					card = textures.getTexture(playerAction.otherPlayer.animals[playerAction.selectedAnimal].getFeature(i));
					if (playerAction.otherPlayer.animals[playerAction.selectedAnimal].features.size() % 2 == 0) {
						batch.draw(card, ((screenWidth - playerAction.otherPlayer.animals[playerAction.selectedAnimal].features.size()) / 2) + card.getWidth() * (i - (playerAction.otherPlayer.animals[playerAction.selectedAnimal].features.size()) / 2), card.getHeight() + (screenHeight - card.getHeight()) / 2);
					} else {
						batch.draw(card, ((screenWidth - playerAction.otherPlayer.animals[playerAction.selectedAnimal].features.size()) / 2) + card.getWidth() * (i - (playerAction.otherPlayer.animals[playerAction.selectedAnimal].features.size()) / 2) - card.getWidth() / 2, card.getHeight() + (screenHeight - card.getHeight()) / 2);
					}
				}
			}
		}
	}

	//rysuj opcje FEEDing faze
	public void drawAnimalOptions(){
		if (flagManager.printFeedingChoices) {
			for (int i = 0; i < 6; i++) {
				batch.draw(buttonManager.feedChoices[i].getGraphic(), buttonManager.feedChoices[i].getPositionX(), buttonManager.feedChoices[i].getPositionY());
			}
			card = textures.getTexture(Card.CHOICE);
			font.draw(batch, "Eat", buttonManager.feedChoices[0].getPositionX() + 40, buttonManager.feedChoices[0].getPositionY() + 30);
			font.draw(batch, "Carnivore", buttonManager.feedChoices[1].getPositionX() + 15, buttonManager.feedChoices[1].getPositionY() + 30);
			font.draw(batch, "Piracy", buttonManager.feedChoices[2].getPositionX() + 25, buttonManager.feedChoices[2].getPositionY() + 30);
			font.draw(batch, "Pasturage", buttonManager.feedChoices[3].getPositionX() + 15, buttonManager.feedChoices[3].getPositionY() + 30);
			font.draw(batch, "Hibernation", buttonManager.feedChoices[4].getPositionX() + 10, buttonManager.feedChoices[4].getPositionY() + 30);
			font.draw(batch, "Scavenger", buttonManager.feedChoices[5].getPositionX() + 15, buttonManager.feedChoices[5].getPositionY() + 30);
		}
	}

	// rysowanie pozycji na zwierzeta
	public void drawAnimalSlots(){
		if (flagManager.printAnimalsSlots) {
			card = textures.getTexture(Card.SPACE);
			//rysuje miejsca na zwierzaka
			for (int i = 0; i < 5; i++) {
				batch.draw(buttonManager.animalPlaces[i].getGraphic(), buttonManager.animalPlaces[i].getPositionX(), buttonManager.animalPlaces[i].getPositionY());
			}
		}
	}

	//karty gracza
	public void drawPlayerCards(){
		for (int i = 0; i < player.cardsNumber(); i++) {
			buttonManager.updateCardButtons();
			if(buttonManager.cardButtons[i]!=null) {
				batch.draw(buttonManager.cardButtons[i].getGraphic(), buttonManager.cardButtons[i].getPositionX(), buttonManager.cardButtons[i].getPositionY());
			}
		}
	}

	//zwierzęta innych graczy
	public void drawOtherAnimals(){
		card = textures.getTexture(Card.ANIMAL);
		for(int j=1;j<gameManager.otherPlayers.size()+1;j++){
			otherPlayer = gameManager.otherPlayers.elementAt(j-1);
			for (int i = 0; i < 5; i++) {
				if (buttonManager.animalButtons[j][i] != null && otherPlayer.animals[i]!=null) {
					batch.draw(card, buttonManager.animalButtons[j][i].getPositionX(), buttonManager.animalButtons[j][i].getPositionY());
					font.draw(batch, "Perks: "+Integer.toString(otherPlayer.animals[i].features.size()), buttonManager.animalButtons[j][i].getPositionX() + 20, buttonManager.animalButtons[j][i].getPositionY() + 70);
					font.draw(batch, "Food: "+Integer.toString(otherPlayer.animals[i].food)+"/"+Integer.toString(otherPlayer.animals[i].foodNeeded), buttonManager.animalButtons[j][i].getPositionX() + 15, buttonManager.animalButtons[j][i].getPositionY() + 50);
					font.draw(batch, "Fat: "+Integer.toString(otherPlayer.animals[i].fat)+"/"+Integer.toString(otherPlayer.animals[i].fatTotal), buttonManager.animalButtons[j][i].getPositionX() + 20, buttonManager.animalButtons[j][i].getPositionY() + 30);
					if(j==1) {
						if (otherPlayer.animals[i].coopWith[1] != null) {
							batch.draw(textures.getTexture(Card.COOP), buttonManager.animalButtons[j][i].getPositionX() + card.getWidth() - textures.getTexture(Card.COOP).getWidth() / 2, buttonManager.animalButtons[j][i].getPositionY() - textures.getTexture(Card.COOP).getHeight());
						}
						if (otherPlayer.animals[i].commWith[1] != null) {
							batch.draw(textures.getTexture(Card.COMM), buttonManager.animalButtons[j][i].getPositionX() + card.getWidth() - textures.getTexture(Card.COMM).getWidth() / 2, buttonManager.animalButtons[j][i].getPositionY() - textures.getTexture(Card.COMM).getHeight());
						}
						if (otherPlayer.animals[i].symbiosis[1] != null) {
							batch.draw(textures.getTexture(Card.SYMB), buttonManager.animalButtons[j][i].getPositionX() + card.getWidth() - textures.getTexture(Card.SYMB).getWidth() / 2, buttonManager.animalButtons[j][i].getPositionY() - textures.getTexture(Card.SYMB).getHeight());
						}
					}
					if(j==2) {
						if (otherPlayer.animals[i].coopWith[1] != null) {
							batch.draw(textures.getTexture(Card.COOP), buttonManager.animalButtons[j][i].getPositionX() + card.getWidth(), buttonManager.animalButtons[j][i].getPositionY() +card.getHeight() - textures.getTexture(Card.COOP).getHeight()/2);
						}
						if (otherPlayer.animals[i].commWith[1] != null) {
							batch.draw(textures.getTexture(Card.COMM), buttonManager.animalButtons[j][i].getPositionX() + card.getWidth(), buttonManager.animalButtons[j][i].getPositionY() +card.getHeight() - textures.getTexture(Card.COOP).getHeight()/2);
						}
						if (otherPlayer.animals[i].symbiosis[1] != null) {
							batch.draw(textures.getTexture(Card.SYMB), buttonManager.animalButtons[j][i].getPositionX() + card.getWidth(), buttonManager.animalButtons[j][i].getPositionY() +card.getHeight() - textures.getTexture(Card.COOP).getHeight()/2);
						}
					}
					if(j==3) {
						if (otherPlayer.animals[i].coopWith[1] != null) {
							batch.draw(textures.getTexture(Card.COOP), buttonManager.animalButtons[j][i].getPositionX() - textures.getTexture(Card.COOP).getWidth(), buttonManager.animalButtons[j][i].getPositionY() +card.getHeight() - textures.getTexture(Card.COOP).getHeight()/2);
						}
						if (otherPlayer.animals[i].commWith[1] != null) {
							batch.draw(textures.getTexture(Card.COMM), buttonManager.animalButtons[j][i].getPositionX() - textures.getTexture(Card.COOP).getWidth(), buttonManager.animalButtons[j][i].getPositionY() +card.getHeight() - textures.getTexture(Card.COOP).getHeight()/2);
						}
						if (otherPlayer.animals[i].symbiosis[1] != null) {
							batch.draw(textures.getTexture(Card.SYMB), buttonManager.animalButtons[j][i].getPositionX() - textures.getTexture(Card.COOP).getWidth(), buttonManager.animalButtons[j][i].getPositionY() +card.getHeight() - textures.getTexture(Card.COOP).getHeight()/2);
						}
					}
				}
			}
		}
	}

	//zwierzeta
	public void drawMyAnimals(){
		card = textures.getTexture(Card.ANIMAL);
		for (int i = 0; i < 5; i++) {
			if (player.animals[i] != null) {
				batch.draw(card, ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth(), 100);
				font.draw(batch, "Perks: "+Integer.toString(player.animals[i].features.size()), ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth() + 20, 130+card.getHeight()/2);
				font.draw(batch, "Food: "+Integer.toString(player.animals[i].food)+"/"+Integer.toString(player.animals[i].foodNeeded), ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth() + 15, 110+card.getHeight()/2);
				font.draw(batch, "Fat: "+Integer.toString(player.animals[i].fat)+"/"+Integer.toString(player.animals[i].fatTotal), ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth() + 20, 90+card.getHeight()/2);
				if(player.animals[i].coopWith[1]!=null){
					batch.draw(textures.getTexture(Card.COOP), ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth() + card.getWidth()-textures.getTexture(Card.COOP).getWidth()/2, 100 + card.getHeight());
				}
				if(player.animals[i].commWith[1]!=null){
					batch.draw(textures.getTexture(Card.COMM), ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth() + card.getWidth()-textures.getTexture(Card.COMM).getWidth()/2, 100 + card.getHeight());
				}
				if(player.animals[i].symbiosis[1]!=null){
					batch.draw(textures.getTexture(Card.SYMB), ((screenWidth - card.getWidth()) / 2) + (i - 2) * card.getWidth() + card.getWidth()-textures.getTexture(Card.SYMB).getWidth()/2, 100 + card.getHeight());
				}
			}
		}
	}

	//rysuj przycisk do pisania wiadomosci
	public void drawChatbox(){
		batch.draw(buttonManager.chat.getGraphic(), buttonManager.chat.getPositionX(), buttonManager.chat.getPositionY());
		font.draw(batch, "Chat", buttonManager.chat.getPositionX() + 20, buttonManager.chat.getPositionY() + 30);
		if(flagManager.chatboxPressed){
			flagManager.inputed=false;
			Gdx.input.getTextInput(listener, "Message:", "", "");
			flagManager.chatboxPressed=false;
		}
		if(flagManager.inputed&&!flagManager.password && !flagManager.login) {
			if(listener.getIntputedText()!=null) {
				gameManager.chatMessage(listener.getIntputedText());
				flagManager.inputed=false;
			}
		}
	}

	//wypisz otrzymana wiadomosc
	public void drawChatMessage(){
		if(gameManager.chatMessageDelivered){
			batch.draw(buttonManager.chatMessage.getGraphic(), buttonManager.chatMessage.getPositionX(), buttonManager.chatMessage.getPositionY());
			font.draw(batch, gameManager.messanger+": "+gameManager.chatMessageContent ,buttonManager.chatMessage.getPositionX() + 15, buttonManager.chatMessage.getPositionY() + 30 );
		}
	}

	public void drawGame(){
	//najpierw tlo
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        batch.begin();
        // rysul tlo
        batch.draw(textures.getTexture(Card.BACKGROUND1), 0, 0);
        batch.draw(textures.getTexture(Card.BACKGROUND2), 0, 0);

        if(gameManager.turnStart){
            startOfTurnCleanup();
        }
        if(gameManager.newTurn){
            gameManager.newTurn=false;
            buttonManager.updateAnimalButtons();
        }

			if (gameManager.state == GameState.EVOLUTION || gameManager.state == GameState.FEEDING) {
				card = textures.getTexture(Card.CHOICE);
				drawYourTurnMessage();
				drawFoodAmount();
				drawWhoseTurn();
				drawEndRoundAndPass();
				drawChatbox();
				drawChatMessage();
				drawSelectedCard();
				drawCancelButton();
				drawSelectedAnimal();
				drawAnimalOptions();
				drawAnimalSlots();
				drawPlayerCards();
				drawOtherAnimals();
				drawMyAnimals();
				askDouble();
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
		if(flagManager.getText){
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

		if(gameManager.state==GameState.BEGIN){
			playerAction.chooseMainMenuOption();
		}

		if(gameManager.state==GameState.GAMEOVER){
			playerAction.returnToMenu();
		}

		if(gameManager.state==GameState.FEEDING || gameManager.state==GameState.EVOLUTION){
			playerAction.enterMessage();
			flagManager.printTurnMessage=false;
		}

        if( gameManager.state==GameState.EVOLUTION){
            playerAction.chooseCardFromHand();
        }
		if(gameManager.state==GameState.FEEDING){
			playerAction.chooseAnimalForAction();
		}
		if(gameManager.turn==player.number && gameManager.state==GameState.EVOLUTION){
			playerAction.chooseAction();
			playerAction.chooseAnimalPlace();
			playerAction.chooseMyAnimal();
		}
		if(gameManager.turn==player.number && gameManager.state==GameState.FEEDING){
			playerAction.chooseAnimalAction();
			playerAction.chooseTarget();
			playerAction.choosePiracyTarget();
			playerAction.useCommunication();
			playerAction.useCooperation();
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