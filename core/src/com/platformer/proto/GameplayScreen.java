package com.platformer.proto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameplayScreen implements Screen {
    final PlatformerProto game;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera cam;

    public GameplayScreen(final PlatformerProto game) {
        this.game = game;

        // The camera will represent the in-game position
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 10, 10);
        cam.update();

        map = new TmxMapLoader().load("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void show() {

    }

    public void handleInput(float delta) {
        // Added for debug purposes, in order to check if the camera works by moving it
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.position.x -= 200 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.position.x += 200 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.position.y += 200 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.position.y -= 200 * delta;
        }
    }

    @Override
    public void render(float delta) {
        // Handle input
        handleInput(delta);

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        // Set
        mapRenderer.setView(cam);
        mapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = width;
        cam.viewportHeight = height;
        cam.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
