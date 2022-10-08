package com.engine;

import com.util.Transform;
import com.util.Vector2D;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    List<GameObject> gameObjects = null;
    Camera camera = null;

    public Renderer(Camera camera) {
        this.camera = camera;
        this.gameObjects = new ArrayList<GameObject>();
    }

    public void submit(GameObject gameObject){
        this.gameObjects.add(gameObject);
    }

    public void render(Graphics2D graphics2D){
        for (GameObject gameObject : gameObjects){
            Transform oldTransform = new Transform(gameObject.transform.position);
            oldTransform.rotation = gameObject.transform.rotation;
            oldTransform.scale = new Vector2D(gameObject.transform.scale.x, gameObject.transform.scale.y);

            gameObject.transform.position = new Vector2D(gameObject.transform.position.x - camera.position.x, gameObject.transform.position.y - camera.position.y);
            gameObject.draw(graphics2D);
            gameObject.transform = oldTransform;
        }
    }
}
