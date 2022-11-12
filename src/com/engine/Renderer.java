package com.engine;

import com.util.Transform;
import com.util.Vector2D;

import java.awt.Graphics2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for rendering game objects on screen.
 *
 * @see List List - Ordered collection of objects.
 * @see Camera Camera - Player's point of view.
 * @see GameObject GameObject – Base object from which everything is derived from.
 */
public class Renderer {
    List<GameObject> gameObjects = null;
    Camera camera = null;

    /**
     * Constructor for the Renderer. Sets the camera used for rendering.
     *
     * @param camera Player's camera.
     * @see Camera Camera - Player's point of view.
     * @see GameObject GameObject – Base object from which everything is derived from.
     */
    public Renderer(Camera camera) {
        this.camera = camera;
        this.gameObjects = new ArrayList<GameObject>(); //! Kompozcicia
    }

    /**
     * Adding a new game object to be rendered.
     *
     * @param gameObject Game object to be added to rendering list
     * @see GameObject GameObject – Base object from which everything is derived from.
     */
    public void submit(GameObject gameObject){
        this.gameObjects.add(gameObject);
    }

    /**
     * Moves game objects relative to the current camera position by subtracting
     * camera position from the current game object position. Camera is basically our
     * window and if we want to draw something that is for example at 0,0 coordinates
     * in the world. We need to move it within the window, then draw it and return it back
     * to where it was before.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     * @see Transform Transform - Class containing one vector for position.
     * @see GameObject GameObject – Base object from which everything is derived from.
     */
    public void render(Graphics2D graphics2D){
        for (GameObject gameObject : gameObjects){
            // Copies old transform of game object
            Transform oldTransform = new Transform(gameObject.transform.position);
            oldTransform.rotation = gameObject.transform.rotation;
            oldTransform.scale = new Vector2D(gameObject.transform.scale.x, gameObject.transform.scale.y);

            // Moves the object to a position relative to the camera position.
            gameObject.transform.position = new Vector2D(gameObject.transform.position.x - camera.position.x, gameObject.transform.position.y - camera.position.y);
            // Draws the object
            gameObject.draw(graphics2D);
            // Moves the game object back where it was before
            gameObject.transform = oldTransform;
        }
    }
}
