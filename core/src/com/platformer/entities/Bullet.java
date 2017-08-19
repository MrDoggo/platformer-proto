package com.platformer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 velocity = new Vector2();
    private float startx;
    private float starty;

    // TODO: Might be more logical to use some timer or similiar instead of distance, to determine when to remove the bullet, since distances are not necessarily linear
    private int maxDistanceX = 500; // Max distance the bullet should travel
    private int maxDistanceY = 500;

    private Sprite sprite;

    // Variable to keep track of if the bullet should still exist or not
    boolean done = false;

    public Bullet(float startx, float starty, Vector2 velocity) {
        this.startx = startx;
        this.starty = starty;
        this.velocity = velocity;

        sprite = new Sprite(new Texture("bullet.png"));
        sprite.setX(startx);
        sprite.setY(starty);

    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void update(float delta) {
        sprite.setX(sprite.getX() + velocity.x * delta);
        sprite.setY(sprite.getY() + velocity.y * delta);

        if (velocity.x < 0) {
            if (sprite.getX() < startx - maxDistanceX)
                done = true;
        } else if (velocity.x > 0) {
            if (sprite.getX() > startx + maxDistanceX)
                done = true;
        }
    }

    public void setX(float x) {
        sprite.setX(x);
    }

    public void setY(float y) {
        sprite.setY(y);
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public boolean isDone() {
        return done;
    }
}
