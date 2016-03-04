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
	Sprite sprite;
	BitmapFont font;
	int mouseClick[]=new int[2];
	GameManager gameManager=new GameManager();


	//uzupelnia tablice mouseInput wspolrzednymi myszki
	private boolean getMouseInput(){
		if(Gdx.input.isTouched()){
			mouseClick[0]=(int)(((double)Gdx.input.getX())/((double)Gdx.graphics.getWidth()/screenWidth));
			mouseClick[1]=(int)(((double)Gdx.graphics.getHeight()-(double)Gdx.input.getY())/((double)Gdx.graphics.getHeight()/screenHeight));
		}

		return true;
	}

	//wybieranie connect lub host
	public boolean menu(){
		batch.begin();
		font.draw(batch, "Host", 10, screenHeight-10);
		font.draw(batch, "Connect", 10, screenHeight-30);
		getMouseInput();
		if (mouseClick[0] < 40 && mouseClick[1] > (screenHeight - 25)) {
			font.draw(batch, "Waiting for clients", 10, screenHeight - 50);
			gameManager.startServer();
			batch.end();
			return true;
		} else if (mouseClick[0] < 80 && mouseClick[1] > (screenHeight - 50) && mouseClick[1] < (screenHeight - 25)) {
			font.draw(batch, "Connecting", 10, screenHeight - 50);
			gameManager.startClient();
			batch.end();
			return true;
		}
		batch.end();
		return false;
	}

	public boolean drawGame(){
		batch.begin();

		batch.draw(background, 0, 0);
		background=new Texture("core/assets/bg2.png");
		batch.draw(background, 0, 0);
		batch.end();
		return true;
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
		if(menu()) {
			if(drawGame()){
				
			}
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
