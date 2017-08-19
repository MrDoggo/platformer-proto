package com.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 velocity = new Vector2();
    private float startx;
    private float starty;

    // TODO: Might be more logical to use some timer or similiar instead of distance, to determine when to remove the bullet, since distances are not necessarily linear
    private int maxDistanceX = 500; // Max distance the bullet should travel
    private int maxDistanceY = 500;

    private Sprite sprite;
    ParticleEffect particleEffect;

    // Variable to keep track of if the bullet should still exist or not
    boolean done = false;

    public Bullet(float startx, float starty, Vector2 velocity) {
        this.startx = startx;
        this.starty = starty;
        this.velocity = velocity;

        sprite = new Sprite(new Texture("weapons/bullet.png"));
        sprite.setX(startx);
        sprite.setY(starty);

        // If the bullet goes left, it should subtract its start position with the length of the bullet image
        if (velocity.x < 0) {
            startx -= sprite.getWidth();
        }

        float particleX, particleY;
        if (velocity.x < 0)
            particleX = sprite.getX() - 10;
        else
            particleX = sprite.getX() + 10;
        particleY = starty;

        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particles/bullet"), Gdx.files.internal(""));
        particleEffect.getEmitters().first().setPosition(particleX, particleY);
        particleEffect.start();
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);

        particleEffect.draw(batch);
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

        // Update particle effect
        particleEffect.update(delta);
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public boolean isDone() {
        return done;
    }
}
