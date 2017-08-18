package com.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.platformer.proto.GameplayScreen;

public class Player extends Sprite {
    private TiledMapTileLayer tileLayer;
    private Sprite sprite;

    private Vector2 velocity = new Vector2();
    private float gravity = 18f;
    private float maxSpeed = 450f;
    private float jumpSpeed = 450;

    private boolean jumping = true;

    public Player(Sprite sprite, TiledMapTileLayer tileLayer) {
        this.sprite = sprite;
        this.sprite.setX(200);
        this.sprite.setY(400);

        this.tileLayer = tileLayer;
    }

    public void draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
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

        if (velocity.x < 0) {
            if (isCollision(newX, prevY))
                sprite.setX(prevX);
        } else if (velocity.x > 0) {
            if (isCollision(newX, prevY))
                sprite.setX(prevX);
        }

        sprite.setY(newY);

        // Going downwards
        if (velocity.y < 0) {
            if (isCollision(prevX, newY)) {
                sprite.setY(prevY);
                jumping = false;
                velocity.y = 0;
            }
        // Going upwards
        } else if (velocity.y > 0) {
            if (isCollision(prevX, newY)) {
                velocity.y = 0;
                sprite.setY(prevY);
            }
        }
    }

    public void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x = -200;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x = 200;
        } else {
            velocity.x = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !jumping) {
            velocity.y = jumpSpeed;
            jumping = true;
        }
    }

    public boolean isCollision(float X, float Y) {
        Rectangle playerRect = new Rectangle(X, Y, sprite.getWidth(), sprite.getHeight());

        /* Naive approach for checking collision */
        for (int y=0; y<tileLayer.getHeight(); y++) {
            for (int x=0; x<tileLayer.getWidth(); x++) {
                Rectangle tileRect = new Rectangle(x*32, y*32, 32, 32);

                if (playerRect.overlaps(tileRect) && tileLayer.getCell(x, y).getTile().getProperties().containsKey("solid"))
                    return true;
            }
        }
        return false;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }
}
