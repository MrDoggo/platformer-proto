package com.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;

public class Player extends Character {
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    private Animation<TextureRegion> playerWalk;
    private TextureRegion playerStand;

    public Player(TiledMapTileLayer tileLayer, TextureAtlas atlas) {
        super(new Sprite(atlas.findRegion("player/walk1")), 200, 400, tileLayer);

        this.tileLayer = tileLayer;

        playerStand = new TextureRegion(sprite.getTexture(), 0, 0, 14, 31); // Standing should not be animated, just a still picture

        // TODO: Fix walk animation etc...
        int CHARACTER_WIDTH = 14;
        int CHARACTER_HEIGHT = 31;
        /*Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i=0; i<1; i++) {
            frames.add(new TextureRegion(sprite.getTexture(), i * CHARACTER_WIDTH, 0, CHARACTER_WIDTH, CHARACTER_HEIGHT));
        }
        playerWalk = new Animation<TextureRegion>(0.1f, frames);*/
    }

    public void draw(SpriteBatch spriteBatch) {
        // Draw player sprite
        sprite.draw(spriteBatch);

        // Draw all bullets
        for (Bullet bullet : bullets) {
            bullet.render(spriteBatch);
        }
    }

    public void update(float delta) {
        velocity.y -= gravity;

        // Set a limit on the y-velocity both upwards and downwards
        if (velocity.y > maxSpeed)
            velocity.y = maxSpeed;
        else if (velocity.y < -maxSpeed)
            velocity.y = -maxSpeed;

        // Save old X,Y values and the new X,Y values
        float prevX = sprite.getX();
        float prevY = sprite.getY();
        float newX = sprite.getX() + velocity.x * delta;
        float newY = sprite.getY() + velocity.y * delta;

        sprite.setX(newX);

        Rectangle collidingTileX = isCollision(newX, prevY);
        // Going left
        if (velocity.x < 0) {
            if (collidingTileX != null)
                sprite.setX(collidingTileX.x + collidingTileX.width + 1);
        // Going right
        } else if (velocity.x > 0) {
            if (collidingTileX != null)
                sprite.setX(collidingTileX.x - 1 - sprite.getWidth());
        }

        sprite.setY(newY);

        Rectangle collidingTileY = isCollision(prevX, newY);
        // Going downwards
        if (velocity.y < 0) {
            if (collidingTileY != null) {
                sprite.setY(collidingTileY.y + collidingTileY.height);
                velocity.y = 0;
            }
        // Going upwards
        } else if (velocity.y > 0) {
            if (collidingTileY != null) {
                velocity.y = 0;
                sprite.setY(collidingTileY.y - sprite.getHeight());
            }
        }

        // Update logic for all bullets
        // Need to use iterator so it's possible to remove a bullet while iterating the list of bullets, else ConcurrentModificationException might occur
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            bullet.update(delta);

            if (bullet.isDone()) {
                iter.remove();
            }
        }

        // Makes sure that the character in question changes appearance depending on how getFrame() is defined, so animation occurs
        sprite.setRegion(getFrame(delta));
    }

    public void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x = -200;
            //facingRight = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x = 200;
            //facingRight = true;
        } else {
            velocity.x = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && getState() != State.JUMPING && getState() != State.FALLING) {
            velocity.y = jumpSpeed;
        }

        // Player should be able to shoot something
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            Vector2 bulletVelocity = new Vector2(500, 0);
            Bullet bulletToCreate;

            // Change start position and bullet velocity to inverse, if the player is facing left instead of right
            if (!facingRight) {
                bulletVelocity.x = -bulletVelocity.x;
                bulletToCreate = new Bullet(sprite.getX(), sprite.getY() + sprite.getHeight() / 2, bulletVelocity);
            } else {
                bulletToCreate = new Bullet(sprite.getX() + sprite.getWidth(), sprite.getY() + sprite.getHeight() / 2, bulletVelocity);
            }

            bullets.add(bulletToCreate);
        }
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case RUNNING:
            case JUMPING:
            case FALLING:
            case STANDING:
            default:
                region = playerStand;
        }

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

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }
}
