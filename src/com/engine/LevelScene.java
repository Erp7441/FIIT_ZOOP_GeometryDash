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

/**
 * The scene that displays level. This scene is where the game happends. It is
 * responsible for rendering all objects, calling apropriate methods on them.
 *
 * @see Scene Scene - abstract class for creating scene objects.
 */
public class LevelScene extends Scene {

    public GameObject player = null;
    public LevelScene(String name) {
        super.Scene(name);
    }
    static LevelScene currentScene = null;

    /**
     * Initializes the scene with textures, renders ground, creates player object, and
     * adds apropriate components on it.
     */
    @Override
    public void init() {

        // Sprites
        Spritesheet layerOne = new Spritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13 * 5); //! Composition
        Spritesheet layerTwo = new Spritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13 * 5); //! Composition
        Spritesheet layerThree = new Spritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13 * 5); //! Composition

        // Creating player
        player = new GameObject("Some game object", new Transform(new Vector2D(300.0,300.0))); //! Composition
        Player playerComp = new Player( //! Composition
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
        GameObject ground = new GameObject("Ground", new Transform(new Vector2D(0, Constants.GROUND_Y))); //! Composition
        ground.addComponent(new Ground());
        addGameObject(ground);
    }

    /**
     * Updates the all game objects in the scene, after that it updates player position.
     *
     * @param deltaTime Diffrence between last mouse update time and current mouse update time.
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     */
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

    /**
     * Draws the background of the game. Afterwards it renders all game objects.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see Color Color - object that represents the color of the player's textures.'
     */
    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        renderer.render(graphics2D);
    }
}
