package com.engine;

import com.file.Serialization;

import java.awt.Graphics2D;

/**
 * Abstract class or a "template" for every component. This class contains all basic methods that
 * every component needs to implement in order to be usable by the engine.
 *
 * @param <T> Indicates generic type of component. This means type can be closer specified at compile time.
 */
public abstract class Component<T> extends Serialization{
    private GameObject gameObject;

    /**
     * Update function that will be called when the component
     * values need to be updated in the game.
     *
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     */
    public void update(double deltaTime){}

    /**
     * Draws the component on the screen.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     */
    public void draw(Graphics2D graphics2D){}
    public abstract Component copy();
    public void start(){}

    public GameObject getGameObject(){
        return gameObject;
    }

    public void setGameObject(GameObject gameObject){
        this.gameObject = gameObject;
    }

}
