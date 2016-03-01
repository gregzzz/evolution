package com.mygdx.game;

import com.mygdx.game.GameManager;
import com.mygdx.game.EvoServer;
import com.mygdx.game.Client;
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
	int screenWidth=640;
	int screenHeight=480;
	SpriteBatch batch;
	Texture img;
	Sprite sprite;
	BitmapFont font;
	int mouseClick[]=new int[2];
	boolean chmuraToKutas=false;



	//dopasowywuje pixelowy input do rozdzielczosci okna
	private int normalizeW(double x){
		return (int)(x*((double)Gdx.graphics.getWidth()/screenWidth));

	}
	private int normalizeH(double y){
		int ya;
		ya =(int)(y*((double)Gdx.graphics.getHeight()/screenHeight));
		return ya;
	}

	private boolean getMouseInput(){
		if(Gdx.input.isTouched()){
			mouseClick[0]=(int)(((double)Gdx.input.getX())/((double)Gdx.graphics.getWidth()/screenWidth));
			mouseClick[1]=(int)(((double)Gdx.graphics.getHeight()-(double)Gdx.input.getY())/((double)Gdx.graphics.getHeight()/screenHeight));
		}

		return true;
	}

	public void game(){

	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		sprite = new Sprite(img, 0, 0, 32, 32);
		font = new BitmapFont();
		font.setColor(Color.WHITE);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		font.draw(batch, "Host", 10, screenHeight-10);
		font.draw(batch, "Connect", 10, screenHeight-30);
		sprite.setPosition(mouseClick[0]-16,mouseClick[1]-16);
		sprite.draw(batch);

		while(true) {
			if(getMouseInput())break;
		}
		if(mouseClick[0]<50&&mouseClick[1]>(screenHeight-25)) {
			font.draw(batch, "Ht", 10, screenHeight - 50);
			if(!chmuraToKutas) {
				try {
					Thread t = new EvoServer(5000, 1);
					t.start();
					chmuraToKutas=true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(mouseClick[0]<50&&mouseClick[1]>(screenHeight-50)&&mouseClick[1]<(screenHeight-25)) {
			font.draw(batch, "Chmura to kutas", 10, screenHeight - 50);
			if(!chmuraToKutas) {
				Client client = new Client("localhost",5000);
				chmuraToKutas=true;
			}
		}
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
		font.dispose();
	}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}

}
