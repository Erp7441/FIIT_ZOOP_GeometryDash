package com.engine;

import com.components.BoxBounds;
import com.components.Ground;
import com.components.Player;
import com.components.RigidBody;
import com.components.Spritesheet;

import com.util.Constants;
import com.util.Transform;
import com.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;


public class LevelScene extends Scene {

    public GameObject player = null;
    public LevelScene(String name) {
        super.Scene(name);
    }
    static LevelScene currentScene = null;

    @Override
    public void init() {

        // Sprites
        Spritesheet layerOne = new Spritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13 * 5);
        Spritesheet layerTwo = new Spritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13 * 5);
        Spritesheet layerThree = new Spritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13 * 5);

        // Creating player
        player = new GameObject("Some game object", new Transform(new Vector2D(300.0,300.0)));
        Player playerComp = new Player(
                layerOne.sprites.get(0),
                layerTwo.sprites.get(0),
                layerThree.sprites.get(0),
                Color.RED,
                Color.GREEN
        );
        player.addComponent(playerComp);
        player.addComponent(new RigidBody(new Vector2D(395, 0.0)));
        player.addComponent(new BoxBounds(Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT));
        addGameObject(player);

        // Creating ground
        GameObject ground = new GameObject("Ground", new Transform(new Vector2D(0, Constants.GROUND_Y)));
        ground.addComponent(new Ground());
        addGameObject(ground);
    }

    @Override
    public void update(double deltaTime) {

        if (player.transform.position.x - camera.position.x > Constants.CAMERA_OFFSET_X){
            camera.position.x = player.transform.position.x - Constants.CAMERA_OFFSET_X;
        }

        if (player.transform.position.y - camera.position.y > Constants.CAMERA_OFFSET_Y){
            camera.position.y = player.transform.position.y - Constants.CAMERA_OFFSET_Y;
        }

        if (camera.position.y > Constants.CAMERA_OFFSET_GROUND_Y){
            camera.position.y = Constants.CAMERA_OFFSET_GROUND_Y;
        }

        for (GameObject gameObject : gameObjects){
            gameObject.update(deltaTime);
        }
        //player.transform.rotation += deltaTime * 1f; // TODO remove this
        player.transform.position.y += deltaTime * 30f;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        renderer.render(graphics2D);
    }
}
