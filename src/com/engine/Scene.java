package com.engine;

import com.util.Vector2D;

import java.awt.Graphics2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for creating scene. Contains all the necessary parts for creating
 * a scene. It manages adding game objects to the game.
 *
 * @see List List - Ordered collection of objects.
 * @see Camera Camera - Player's point of view.
 * @see GameObject GameObject – Base object from which everything is derived from.
 */
public abstract class Scene {
    String name;
    public Camera camera;
    List<GameObject> gameObjects = null;
    Renderer renderer = null;

    /**
     * Initializes a new scene. Creates a new camera and renderer.
     *
     * @param name Name of the scene for debugging purposes
     * @see List List - Ordered collection of objects.
     * @see Camera Camera - Player's point of view.
     * @see GameObject GameObject – Base object from which everything is derived from.
     */
    public void Scene(String name) {
        this.name = name;
        this.camera = new Camera(new Vector2D()); //! Composition
        this.gameObjects = new ArrayList<GameObject>(); //! Composition
        this.renderer = new Renderer(this.camera); //! Composition
    }

    /**
     * Adds game object to the game object list of this scene.
     *
     * @param gameObject - Game object to be added to game object list
     * @see GameObject GameObject – Base object from which everything is derived from.
     */
    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        renderer.submit(gameObject);
        for (Component component : gameObject.getComponents()){
            component.start();
        }
    }
    public abstract void init();
    public abstract void update(double deltaTime);
    public abstract void draw(Graphics2D graphics2D);
}
