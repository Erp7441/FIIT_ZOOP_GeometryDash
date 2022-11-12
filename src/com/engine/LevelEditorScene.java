package com.engine;

import com.components.*;

import com.ui.MainContainer;
import com.util.Constants;
import com.util.Transform;
import com.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Scene that displays the level editor. This scene can edit the LevelScene objects
 * which means that player can model their own level.
 */
public class LevelEditorScene extends Scene{

    public GameObject player = null;
    private GameObject ground = null;
    private Grid grid = null;
    private CameraControls cameraControls = null;
    public GameObject cursor = null;
    private MainContainer editingButtons = new MainContainer(); //! Composition

    /**
     * Initializes the scene with name.
     * @param name The name of the object. Only used for debugging purposes.
     */
    public LevelEditorScene(String name) {
        super.Scene(name);

    }

    /**
     * Initializes the scene with the same way as LevelScene. Then it freezes the game and adds
     * grid, cursor and editor controls. So the player can place and remove objects from the level.
     * Grid so the game objects added can snap into place.
     *
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see Spritesheet Spritesheet - multiple pieces of 2D textures
     * @see Player Player - object that is controlled by the player.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see Color Color - object that represents the color of the player's textures.'
     */
    @Override
    public void init() {

        grid = new Grid(); //! Composition
        cameraControls = new CameraControls(); //! Composition
        editingButtons.start();

        cursor = new GameObject("Mouse Cursor", new Transform(new Vector2D())); //! Composition
        cursor.addComponent(new SnapToGrid(Constants.TILE_WIDTH, Constants.TILE_HEIGHT));

        Spritesheet layerOne = new Spritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13 * 5); //! Composition
        Spritesheet layerTwo = new Spritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13 * 5); //! Composition
        Spritesheet layerThree = new Spritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13 * 5); //! Composition

        player = new GameObject("Some game object", new Transform(new Vector2D(300.0,300.0))); //! Composition
        Player playerComp = new Player( //! Composition
                layerOne.sprites.get(0),
                layerTwo.sprites.get(0),
                layerThree.sprites.get(0),
                Color.RED,
                Color.GREEN
        );
        player.addComponent(playerComp);
        addGameObject(player);

        ground = new GameObject("Ground", new Transform(new Vector2D(0, Constants.GROUND_Y))); //! Composition
        ground.addComponent(new Ground());
        addGameObject(ground);
    }

    /**
     * Updates the all game objects in the scene, after that it updates camera,
     * grid, editing buttons and cursor.
     *
     * @param deltaTime Diffrence between last mouse update time and current mouse update time.
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     */
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

    /**
     * Draws the background of the game. Afterwards it renders all game objects.
     * Then it draws the editing buttons and the cursor.
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
        grid.draw(graphics2D);
        editingButtons.draw(graphics2D);
        cursor.draw(graphics2D);
    }
}
