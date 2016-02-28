package com.mygdx.game;

import com.mygdx.game.GameManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame implements ApplicationListener {
	SpriteBatch batch;
	Texture img;
	Sprite sprite;
	int xa,ya;

	private void update(){
		if(Gdx.input.justTouched()){
			float x=Gdx.input.getX()-16;
			float y=Gdx.graphics.getHeight()-Gdx.input.getY()-16;
			xa=(int)(x/((double)Gdx.graphics.getWidth()/640.0));
			ya=(int)(y/((double)Gdx.graphics.getHeight()/480.0));
		}
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		sprite = new Sprite(img, 0, 0, 32, 32);
	}

	@Override
	public void render () {
		update();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		sprite.setPosition(xa,ya);
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}

}
