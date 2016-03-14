package com.mygdx.game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MyGdxGame implements ApplicationListener {
	int screenWidth=1200;
	int screenHeight=800;
	String line;
	BufferedReader in;
	SpriteBatch batch;
	Texture background;
	Texture card;
	Sprite sprite;
	BitmapFont font;
	int mouseClick[]=new int[2];
	GameManager gameManager=new GameManager();
	Player player;


	//glowna petla
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player=gameManager.startClient();
		drawGame();
		getMouseInput();
		chooseCardFromHand();
	}

	public void chooseCardFromHand(){
		//wybor karty
		for(int i=0;i<player.cardsNumber();i++){
			if(mouseClick[1]<card.getHeight() && mouseClick[0]>i*card.getWidth() && mouseClick[0]<(i+1)*card.getWidth()){
				batch.begin();
				//narysowanie wybranej karty
				card=new Texture("core/assets/"+player.getCards(i)+".png");
				batch.draw(card, (screenWidth-card.getWidth())/2, card.getHeight()+(screenHeight-card.getHeight())/2);
				//narysowanie ramki do tekstu
				card=new Texture("core/assets/ramka.png");
				batch.draw(card,(screenWidth-card.getWidth())/2 , (screenHeight-card.getHeight())/2);
				//pobranie opisu musi byc w try-catchu
				try{
					in = new BufferedReader(new FileReader("core/assets/"+player.getCards(i)+".txt"));
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
				try{
					line = in.readLine();
				}catch(IOException e) {
					e.printStackTrace();
				}
				//tekst mniej wiecej na srodku
				font.draw(batch, line, 600-3*line.length(), 5+screenHeight/2);
				card=new Texture("core/assets/choice.png");
				//rysowanie wyborow
				for(int j=-1;j<2;j++){
					batch.draw(card, ((screenWidth-card.getWidth())/2)-j*card.getWidth(), (screenHeight-card.getHeight())/2-50);
				}
				font.draw(batch, "Add Animal", ((screenWidth-card.getWidth())/2)-card.getWidth()+10, ((screenHeight-card.getHeight())/2)-20);
				font.draw(batch, "Add Perk 1", ((screenWidth-card.getWidth())/2)+10, ((screenHeight-card.getHeight())/2)-20);
				font.draw(batch, "Add Perk 2", ((screenWidth-card.getWidth())/2)+card.getWidth()+10, ((screenHeight-card.getHeight())/2)-20);
				batch.end();
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

	public boolean drawGame(){
		//najpierw tlo
		batch.begin();
		background=new Texture("core/assets/bg.png");
		batch.draw(background, 0, 0);
		background=new Texture("core/assets/bg2.png");
		batch.draw(background, 0, 0);
		//karty gracza
		for(int i=0; i<player.cardsNumber();i++){
			card=new Texture("core/assets/"+player.getCards(i)+".png");
			batch.draw(card, i*card.getWidth(), 0);
		}
		batch.end();
		return true;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.GREEN);
		background=new Texture("core/assets/bg.png");
		card=new Texture("core/assets/animal.png");
	}



	@Override
	public void dispose() {
		batch.dispose();
		background.dispose();
		font.dispose();
		card.dispose();
	}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}

}