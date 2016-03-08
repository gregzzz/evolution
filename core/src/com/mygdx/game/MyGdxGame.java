package com.mygdx.game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;

import java.io.IOException;


public class MyGdxGame implements ApplicationListener {
	int screenWidth=1200;
	int screenHeight=800;
	SpriteBatch batch;
	Texture background;
	Texture card;
	Sprite sprite;
	BitmapFont font;
	int mouseClick[]=new int[2];


	GameManager game =new GameManager();
	//jak dla mnie player powinien  byc w gameManagerze
	Player player;


	//uzupelnia tablice mouseInput wspolrzednymi myszki
	private boolean getMouseInput(){
		if(Gdx.input.isTouched()){
			mouseClick[0]=(int)(((double)Gdx.input.getX())/((double)Gdx.graphics.getWidth()/screenWidth));
			mouseClick[1]=(int)(((double)Gdx.graphics.getHeight()-(double)Gdx.input.getY())/((double)Gdx.graphics.getHeight()/screenHeight));
		}

		return true;
	}

	//wybieranie connect lub host
	public void menu(){
		batch.begin();
		font.draw(batch, "Host", 10, screenHeight-10);
		font.draw(batch, "Connect", 10, screenHeight-30);
		getMouseInput();
		// wyjebalem stawianie serwera bo inne byly zalozenia projektowe
		if (mouseClick[0] < 80 && mouseClick[1] > (screenHeight - 50) && mouseClick[1] < (screenHeight - 25)) {
			font.draw(batch, "Connecting", 10, screenHeight - 50);
			game.startClient();
			game.screen = "game";
			batch.end();
		}
		else {
			batch.end();
		}
	}
	// zrob tak zbey od razy sie resizowala wzgledem wielkosci ekranu i ilosci graczy nie sztywno
	// i rozmiar kart
	public void drawGame(){
		batch.begin();
		// jestem za zmiana tesktur bo tlo jest paskudne xd
		background=new Texture("core/assets/bg.png");
		batch.draw(background, 0, 0);
		background=new Texture("core/assets/bg2.png");
		batch.draw(background, 0, 0);
		// wypisuje imiona graczy
		int spaceForName = 300;
		for (Object obj : game.otherPlayers){
			Player otherPlayer =(Player) obj;
			font.draw(batch, otherPlayer.name , screenWidth/2-game.otherPlayers.size()*spaceForName+spaceForName*game.otherPlayers.indexOf(obj), screenHeight-50);
		}
		// rysowanie kart
		// ladna zmienna reprezentujaca rozmiar karty
		int cardSize = 100;
		for(int i = 0; i < game.player.numberOfCards; i++){
				card=new Texture("core/assets/"+game.player.getCards(i)+".bmp");
				// jebnalem tak zeby je drukowalo mniej wiecej na srodku
				batch.draw(card, screenWidth/2-game.player.numberOfCards*cardSize/2+i*cardSize, 0);
		}
		batch.end();
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.GREEN);
		background=new Texture("core/assets/bg.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// zmienilem koncept bo byl paskudny i wykonywal menu przed kazdym wykonaniem drawGame
		if(game.screen.equals("menu")){
			menu();
		}
		else if(game.screen.equals("game")){
			drawGame();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		background.dispose();
		font.dispose();
	}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}

}
