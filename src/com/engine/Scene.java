package com.engine;

import com.util.Vector2D;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    String name;
    Camera camera;
    List<GameObject> gameObjects = null;
    Renderer renderer = null;
    public void Scene(String name) {
        this.name = name;
        this.camera = new Camera(new Vector2D());
        this.gameObjects = new ArrayList<GameObject>();
        this.renderer = new Renderer(this.camera);
        this.init();
    }
    public abstract void init();
    public abstract void update(double deltaTime);
    public abstract void draw(Graphics2D graphics2D);
}
