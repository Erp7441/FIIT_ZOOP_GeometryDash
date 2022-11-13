package com.engine;

import com.util.Vector2D;

/**
 * Represents the point of view for the player. It is player camera through which
 * he looks at the game.
 */
public class Camera {

    private Vector2D position;

    /**
     * Initializes position of the player's camera.
     *
     * @param position - vector that holds the position of the players camera
     * @see Vector2D Vector2D – Vector that has lenght and a direction within the 2D space.
     */
    public Camera(Vector2D position){
        this.position = position; //! Aggregation
    }

    public Vector2D getPosition(){
        return position;
    }

    public void setPosition(Vector2D position){
        this.position = position;
    }
}
