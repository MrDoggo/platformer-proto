package com.platformer.proto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.platformer.entities.Player;
import com.platformer.entities.SlimeEnemy;

public class GameplayScreen implements Screen {
    final PlatformerProto game;

    private TiledMap map;
    private TiledMapTileLayer mapLayer;

    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera cam;

    private final Player player;
    private SlimeEnemy slimeEnemy;

    private final TextureAtlas atlas;

    public GameplayScreen(final PlatformerProto game) {
        this.game = game;

        // The camera will represent the in-game position
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        // Load map tile from Tiled, that contains all the tile maps
        map = new TmxMapLoader().load("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Load texture atlas containing all the character images used for animation
        atlas = new TextureAtlas("characters.pack");

        // TODO: Make this an arraylist or something later with all the tile map layers, instead of only one hardcoded
        mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

        // Create player character
        player = new Player(mapLayer, atlas);

        // Create test enemy character
        slimeEnemy = new SlimeEnemy(mapLayer, atlas);
    }

    @Override
    public void show() {

    }

    public void handleInput(float delta) {
        player.input();
    }

    @Override
    public void render(float delta) {
        // Handle input
        handleInput(delta);

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set camera to the player's position
        cam.position.set((int) player.getX(), (int) player.getY(), 0);

        // Make sure the camera doesn't go out of boundary (the tilemap size)
        if (cam.position.x < cam.viewportWidth / 2) {
            cam.position.x = cam.viewportWidth / 2;
        }
        if (cam.position.y < cam.viewportHeight / 2) {
            cam.position.y = cam.viewportHeight / 2;
        }
        if (cam.position.x + cam.viewportWidth / 2 > mapLayer.getWidth() * mapLayer.getTileWidth()) {
            cam.position.x = mapLayer.getWidth() * mapLayer.getTileWidth() - cam.viewportWidth / 2;
        }
        if (cam.position.y + cam.viewportHeight / 2 > mapLayer.getHeight() * mapLayer.getTileHeight()) {
            cam.position.y = mapLayer.getHeight() * mapLayer.getTileHeight() - cam.viewportHeight / 2;
        }

        // Update camera
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        // Render tile map
        mapRenderer.setView(cam);
        mapRenderer.render();

        player.update(delta);
        slimeEnemy.update(delta);

        game.batch.begin();
        slimeEnemy.draw(game.batch);
        player.draw(game.batch);
        game.batch.end();
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
