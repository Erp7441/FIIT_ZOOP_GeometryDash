package com.engine;

import java.awt.Graphics2D;

public abstract class Component<T> {

    public GameObject gameObject;

    public void update(double deltaTime){ return;}

    public void draw(Graphics2D graphics2D){ return;}
}
