package com.util;

public class Transform {
    public Vector2D position;
    public double rotation;
    public Vector2D scale;

    public Transform (Vector2D position) {
        this.position = position;
        this.rotation = 0.0;
        this.scale = new Vector2D(1.0, 1.0);
    }

    public Transform copy(){
        Transform transform = new Transform(this.position.copy());
        transform.scale = this.scale.copy();
        transform.rotation = this.rotation;
        return transform;
    }

    @Override
    public String toString() {
        return "Position (" + position.x + ", " + position.y + ") Rotation (" + rotation + ") Scale (" + scale.x + ", " + scale.y +")";
    }
}
