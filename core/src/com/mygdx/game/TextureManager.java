package com.mygdx.game;

import java.util.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
/**
 * Created by kopec on 2016-03-17.
 */
public class TextureManager {
    public Map<String,Texture> textures;

    public TextureManager(){
        textures = new HashMap<String,Texture>();
        textures.put("aquatic",new Texture("core/assets/aquatic.png"));
        textures.put("camouflage",new Texture("core/assets/camouflage.png"));
        textures.put("communication",new Texture("core/assets/communication.png"));
        textures.put("coopc",new Texture("core/assets/coopc.png"));
        textures.put("coopf",new Texture("core/assets/coopf.png"));
        textures.put("hibernation",new Texture("core/assets/hibernation.png"));
        textures.put("fat",new Texture("core/assets/fat.png"));
        textures.put("massivec",new Texture("core/assets/massivec.png"));
        textures.put("massivef",new Texture("core/assets/massivef.png"));
        textures.put(" mimicry",new Texture("core/assets/mimicry.png"));
        textures.put("parasitef",new Texture("core/assets/parasitef.png"));
        textures.put("parasitec",new Texture("core/assets/parasitec.png"));
        textures.put("pasturage",new Texture("core/assets/pasturage.png"));
        textures.put("piracy",new Texture("core/assets/piracy.png"));
        textures.put("roar",new Texture("core/assets/roar.png"));
        textures.put("scavenger",new Texture("core/assets/scavenger.png"));
        textures.put("sharp",new Texture("core/assets/sharp.png"));
        textures.put("speed",new Texture("core/assets/speed.png"));
        textures.put("symbiosis",new Texture("core/assets/symbiosis.png"));
        textures.put("tail",new Texture("core/assets/tail.png"));
        textures.put("toxic",new Texture("core/assets/toxic.png"));
    }

    public Texture getTexture(String textureName){
        return textures.get(textureName);
    }

    public Set entrySet(){
        return textures.entrySet();
    }
}
