package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Evolution;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config;
		config = new LwjglApplicationConfiguration();
		config.width=1200;
		config.height=800;
		new LwjglApplication(new Evolution(), config);
	}
}
