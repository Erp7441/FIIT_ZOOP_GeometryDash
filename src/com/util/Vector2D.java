package com.util;

/**
 * Vector class representing 2 dimensional vector from physics with X and Y coordinates.
 */
public class Vector2D {
    public double x, y;

    /**
     * Constructs a new Vector2D that sets the X and Y values
     * @param x Horizontal coordinate of the vector
     * @param y Vertical coordinate of the vector
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor for empty vector class arguments. Automatically assumes that X and Y are 0
     */
    public Vector2D(){
        this.x = 0;
        this.y = 0;
    }

    /**
     * @return New Vector with same X and Y coordinates as this vector.
     */
    public Vector2D copy(){
        return new Vector2D(this.x, this.y);
    }
}
