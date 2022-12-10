package com.engine;

import com.components.BoxBounds;
import com.components.Ground;
import com.components.Player;
import com.components.RigidBody;
import com.components.Spritesheet;

import com.util.AssetPool;
import com.util.Constants;
import com.util.Transform;
import com.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The scene that displays level. This scene is where the game happens. It is
 * responsible for rendering all objects, calling appropriate methods on them.
 *
 * @see Scene Scene - abstract class for creating scene objects.
 */
public class LevelScene extends Scene {

    private GameObject player = null;

    /**
     * Initializes the scene with name.
     * @param name The name of the object. Only used for debugging purposes.
     */
    public LevelScene(String name) {
        super(name);
    }

    /**
     * Initializes the scene with textures, renders ground, creates player object, and
     * adds appropriate components on it.
     */
    @Override
    public void init() {

        // Sprites
        initAssetPool();
        Spritesheet layerOne = AssetPool.getSpritesheet("assets/player/layerOne.png");
        Spritesheet layerTwo = AssetPool.getSpritesheet("assets/player/layerTwo.png");
        Spritesheet layerThree = AssetPool.getSpritesheet("assets/player/layerThree.png");

        // Creating player
        player = new GameObject("Some game object", new Transform(new Vector2D(300.0,300.0))); //! Composition
        Player playerComp = new Player(
                layerOne.getSprites().get(0),
                layerTwo.getSprites().get(0),
                layerThree.getSprites().get(0),
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

    public void initAssetPool(){
        AssetPool.addSpritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/groundSprites.png", 42, 42, 2, 6, 12);
    }

    /**
     * Updates the all game objects in the scene, after that it updates player position.
     *
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     */
    @Override
    public void update(double deltaTime) {

        if (player.getTransform().getPosition().getX() - getCamera().getPosition().getX() > Constants.CAMERA_OFFSET_X){
            getCamera().getPosition().setX(player.getTransform().getPosition().getX() - Constants.CAMERA_OFFSET_X);
        }

        if (player.getTransform().getPosition().getY() - getCamera().getPosition().getY() > Constants.CAMERA_OFFSET_Y){
            getCamera().getPosition().setY(player.getTransform().getPosition().getY() - Constants.CAMERA_OFFSET_Y);
        }

        if (getCamera().getPosition().getY() > Constants.CAMERA_OFFSET_GROUND_Y){
            getCamera().getPosition().setY(Constants.CAMERA_OFFSET_GROUND_Y);
        }

        for (GameObject gameObject : getGameObjects()){
            gameObject.update(deltaTime);
        }

        player.getTransform().getPosition().setY(player.getTransform().getPosition().getY() + deltaTime * 30f);
    }

    /**
     * Draws the background of the game. Afterwards it renders all game objects.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see Color Color - object that represents the color of the player's textures.
     */
    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        getRenderer().render(graphics2D);
    }

    public GameObject getPlayer(){
        return player;
    }

    public void setPlayer(GameObject player){
        this.player = player;
    }
}
