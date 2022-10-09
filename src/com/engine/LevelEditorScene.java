package com.engine;

import com.components.*;

import com.ui.MainContainer;
import com.util.Constants;
import com.util.Transform;
import com.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;

public class LevelEditorScene extends Scene{

    public GameObject player = null;
    private GameObject ground = null;
    private Grid grid = null;
    private CameraControls cameraControls = null;
    public GameObject cursor = null;
    private MainContainer editingButtons = new MainContainer();

    public LevelEditorScene(String name) {
        super.Scene(name);

    }

    @Override
    public void init() {

        grid = new Grid();
        cameraControls = new CameraControls();
        editingButtons.start();

        cursor = new GameObject("Mouse Cursor", new Transform(new Vector2D()));
        cursor.addComponent(new SnapToGrid(Constants.TILE_WIDTH, Constants.TILE_HEIGHT));

        Spritesheet layerOne = new Spritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13 * 5);
        Spritesheet layerTwo = new Spritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13 * 5);
        Spritesheet layerThree = new Spritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13 * 5);

        player = new GameObject("Some game object", new Transform(new Vector2D(300.0,300.0)));
        Player playerComp = new Player(
                layerOne.sprites.get(0),
                layerTwo.sprites.get(0),
                layerThree.sprites.get(0),
                Color.RED,
                Color.GREEN
        );
        player.addComponent(playerComp);
        addGameObject(player);

        ground = new GameObject("Ground", new Transform(new Vector2D(0, Constants.GROUND_Y)));
        ground.addComponent(new Ground());
        addGameObject(ground);
    }

    @Override
    public void update(double deltaTime) {

        if (camera.position.y - ground.transform.position.y > Constants.CAMERA_OFFSET_GROUND_Y){
            camera.position.y = Constants.CAMERA_OFFSET_GROUND_Y;
        }

        for (GameObject gameObject : gameObjects){
            gameObject.update(deltaTime);
        }

        cameraControls.update(deltaTime);
        grid.update(deltaTime);
        editingButtons.update(deltaTime);
        cursor.update(deltaTime);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        renderer.render(graphics2D);
        grid.draw(graphics2D);
        editingButtons.draw(graphics2D);
        cursor.draw(graphics2D);
    }
}
