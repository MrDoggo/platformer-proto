package com.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

public class Player extends Sprite {
    private TiledMapTileLayer tileLayer;
    private Sprite sprite;

    private Vector2 velocity = new Vector2();
    private float gravity = 18f;
    private float maxSpeed = 450f;
    private float jumpSpeed = 450;

    private boolean jumping = true;
    private boolean facingRight = true;

    ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    public Player(Sprite sprite, TiledMapTileLayer tileLayer) {
        this.sprite = sprite;
        this.sprite.setX(200);
        this.sprite.setY(400);

        this.tileLayer = tileLayer;
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
                jumping = false;
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
    }

    public void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x = -200;
            facingRight = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x = 200;
            facingRight = true;
        } else {
            velocity.x = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !jumping) {
            velocity.y = jumpSpeed;
            jumping = true;
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

    public Rectangle isCollision(float X, float Y) {
        Rectangle playerRect = new Rectangle(X, Y, sprite.getWidth(), sprite.getHeight());

        /* Naive approach for checking collision */
        for (int y=0; y<tileLayer.getHeight(); y++) {
            for (int x=0; x<tileLayer.getWidth(); x++) {
                Rectangle tileRect = new Rectangle(x*32, y*32, 32, 32);

                if (playerRect.overlaps(tileRect) && tileLayer.getCell(x, y).getTile().getProperties().containsKey("solid"))
                    return tileRect;
            }
        }
        return null;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }
}
