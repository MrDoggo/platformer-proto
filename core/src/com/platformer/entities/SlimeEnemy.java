package com.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SlimeEnemy extends Character {

    public SlimeEnemy(TiledMapTileLayer tileLayer) {
        super(new Sprite(new Texture("enemies/slime.png")), 400, 350, tileLayer);

        velocity.x = -25;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
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
            if (collidingTileX != null) {
                sprite.setX(collidingTileX.x + collidingTileX.width + 1);
                velocity.x = -velocity.x;
            }
            // Going right
        } else if (velocity.x > 0) {
            if (collidingTileX != null) {
                sprite.setX(collidingTileX.x - 1 - sprite.getWidth());
                velocity.x = -velocity.x;
            }
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
}
