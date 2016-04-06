package com.mygdx.game.managers;

import java.util.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import components.enums.Card;

public class TextureManager {
    public Map<Card,Texture> textures;

    public TextureManager(){
        textures = new HashMap<Card,Texture>();
        // tla
        textures.put(Card.BACKGROUND1,new Texture("core/assets/bg.png"));
        textures.put(Card.BACKGROUND2,new Texture("core/assets/bg2.png"));
        // cechy
        textures.put(Card.AQUATIC,new Texture("core/assets/aquatic.png"));
        textures.put(Card.CAMOUFLAGE,new Texture("core/assets/camouflage.png"));
        textures.put(Card.COMMUNICATION,new Texture("core/assets/communication.png"));
        textures.put(Card.COOPERATIONC,new Texture("core/assets/coopc.png"));
        textures.put(Card.COOPERATIONF,new Texture("core/assets/coopf.png"));
        textures.put(Card.HIBERNATION,new Texture("core/assets/hibernation.png"));
        textures.put(Card.FAT,new Texture("core/assets/fat.png"));
        textures.put(Card.MASSIVEC,new Texture("core/assets/massivec.png"));
        textures.put(Card.MASSIVEF,new Texture("core/assets/massivef.png"));
        textures.put(Card.MIMICRY,new Texture("core/assets/mimicry.png"));
        textures.put(Card.PARASITEF,new Texture("core/assets/parasitef.png"));
        textures.put(Card.PARASITEC,new Texture("core/assets/parasitec.png"));
        textures.put(Card.PASTURAGE,new Texture("core/assets/pasturage.png"));
        textures.put(Card.PIRACY,new Texture("core/assets/piracy.png"));
        textures.put(Card.ROAR,new Texture("core/assets/roar.png"));
        textures.put(Card.SCAVENGER,new Texture("core/assets/scavenger.png"));
        textures.put(Card.SHARPSIGHT,new Texture("core/assets/sharp.png"));
        textures.put(Card.SPEED,new Texture("core/assets/speed.png"));
        textures.put(Card.SYMBIOSIS,new Texture("core/assets/symbiosis.png"));
        textures.put(Card.TAILTOSS,new Texture("core/assets/tail.png"));
        textures.put(Card.TOXIC,new Texture("core/assets/toxic.png"));
        textures.put(Card.CARNIVORE,new Texture("core/assets/carnivore.png"));
        // cos tam
        textures.put(Card.SPACE,new Texture("core/assets/space.png"));
        textures.put(Card.ANIMAL,new Texture("core/assets/animal.png"));
        textures.put(Card.CHOICE,new Texture("core/assets/choice.png"));
        textures.put(Card.RAMKA ,new Texture("core/assets/ramka.png"));
    }

    public Texture getTexture(Card textureName){
        return textures.get(textureName);
    }

    public Set entrySet(){
        return textures.entrySet();
    }
}
