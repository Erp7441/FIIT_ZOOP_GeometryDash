package com.util;

/**
 * Transform class containing one vector for postion. One vector for scale and
 * one double value for the amout of rotation. This is used on objects so they can
 * be placed in different places on the screen.
 */
public class Transform {
    public Vector2D position;
    public double rotation;
    public Vector2D scale;

    /**
     * Constructs a new Transform with the given position. Assumes that rotation is 0
     * at the creation of the transform and scale is 1 (no change in scale).
     *
     * @param position 2D vector containing postion coordinates.
     */
    public Transform (Vector2D position) {
        this.position = position;
        this.rotation = 0.0;
        this.scale = new Vector2D(1.0, 1.0);
    }

    /**
     * Creates new Transform and assigns the values of this Transform to the new one.
     * @return New Transform with same values as this Transform.
     */
    public Transform copy(){
        Transform transform = new Transform(this.position.copy());
        transform.scale = this.scale.copy();
        transform.rotation = this.rotation;
        return transform;
    }

    /**
     * @return Converts this Transform to a string representation suitable for displaying on the screen.
     */
    @Override
    public String toString() {
        return "Position (" + position.x + ", " + position.y + ") Rotation (" + rotation + ") Scale (" + scale.x + ", " + scale.y +")";
    }
}
