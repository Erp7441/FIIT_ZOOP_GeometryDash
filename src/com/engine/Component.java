package com.engine;

import java.awt.Graphics2D;

public abstract class Component<T> {
    public GameObject gameObject;
    public void update(double deltaTime){}
    public void draw(Graphics2D graphics2D){}
    public abstract Component copy();
    public void start(){}
}
