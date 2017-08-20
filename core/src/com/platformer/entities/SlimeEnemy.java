package com.platformer.entities;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

public class SlimeEnemy extends Character {
    protected Animation<TextureRegion> slimeWalk;

    public SlimeEnemy(TiledMapTileLayer tileLayer, TextureAtlas atlas) {
        //super(new Sprite(new Texture("enemies/slime.png")), 400, 350, tileLayer);
        super(new Sprite(atlas.findRegion("slime/slimeWalk1")), 400, 350, tileLayer);

        velocity.x = -25;

        int CHARACTER_WIDTH = 50;
        int CHARACTER_HEIGHT = 28;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i=0; i<2; i++) {
            frames.add(new TextureRegion(sprite.getTexture(), i * CHARACTER_WIDTH, 0, CHARACTER_WIDTH, CHARACTER_HEIGHT));
        }
        slimeWalk = new Animation<TextureRegion>(0.1f, frames);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case RUNNING:
            case JUMPING:
            case FALLING:
            case STANDING:
            default: // Slimes are always walking
                region = slimeWalk.getKeyFrame(animationTimer, true);
        }

        // Face slime to the left or right depending on where it's facing
        if ((velocity.x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false);
            facingRight = false;
        } else if ((velocity.x > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }

        animationTimer = (currentState == previousState) ? animationTimer + delta : 0;
        previousState = currentState;
        return region;
    }
}
