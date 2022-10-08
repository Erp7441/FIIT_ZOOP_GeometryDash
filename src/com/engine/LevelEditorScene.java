package com.engine;

import com.components.*;

import com.util.Constants;
import com.util.Transform;
import com.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;

public class LevelEditorScene extends Scene{

    public GameObject player = null;
    GameObject ground = null;
    Grid grid = null;
    CameraControls cameraControls = null;

    public LevelEditorScene(String name) {
        super.Scene(name);

    }

    @Override
    public void init() {

        grid = new Grid();
        cameraControls = new CameraControls();

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

    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        renderer.render(graphics2D);
        grid.draw(graphics2D);
    }
}
