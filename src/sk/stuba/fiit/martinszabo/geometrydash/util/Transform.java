package sk.stuba.fiit.martinszabo.geometrydash.util;

import sk.stuba.fiit.martinszabo.geometrydash.file.Parser;
import sk.stuba.fiit.martinszabo.geometrydash.file.Serialization;

/**
 * Transform class containing one vector for position. One vector for scale and
 * one double value for the amount of rotation. This is used on objects, so they can
 * be placed in different places on the screen.
 */
public class Transform extends Serialization{
    private Vector2D position;
    private double rotation;
    private Vector2D scale;

    /**
     * Constructs a new Transform with the given position. Assumes that rotation is 0
     * at the creation of the transform and scale is 1 (no change in scale).
     *
     * @param position 2D vector containing position coordinates.
     */
    public Transform (Vector2D position) {
        this.position = position; //! Aggregation
        this.rotation = 0.0;
        this.scale = new Vector2D(1.0, 1.0); //! Composition
    }

    public Transform (Vector2D position, double rotation, Vector2D scale) {
        this.position = position; //! Aggregation
        this.rotation = rotation;
        this.scale = scale; //! Aggregation
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
        return "Position (" + position.getX() + ", " + position.getY() + ") Rotation (" + rotation + ") Scale (" + scale.getX() + ", " + scale.getY() +")";
    }

    public Vector2D getPosition(){
        return position;
    }

    public void setPosition(Vector2D position){
        this.position = position;
    }

    public double getRotation(){
        return rotation;
    }

    public void setRotation(double rotation){
        this.rotation = rotation;
    }

    public Vector2D getScale(){
        return scale;
    }

    public void setScale(Vector2D scale){
        this.scale = scale;
    }

    @Override
    public String serialize(int tabSize){
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("Transform", tabSize));

        builder.append(beginObjectProperty("Position", tabSize + 1));
        builder.append(this.position.serialize(tabSize + 2));
        builder.append(endObjectProperty(tabSize + 1));
        builder.append(addEnding(true, true));

        builder.append(beginObjectProperty("Scale", tabSize + 1));
        builder.append(this.scale.serialize(tabSize + 2));
        builder.append(endObjectProperty(tabSize + 1));
        builder.append(addEnding(true, true));

        builder.append(addDoubleProperty("Rotation", this.rotation, tabSize + 1, true, false));
        builder.append(endObjectProperty(tabSize));

        return builder.toString();
    }

    public static Transform deserialize(){
        Parser.consumeBeginObjectProperty("Transform");
        Parser.consumeBeginObjectProperty("Position");
        Vector2D position = Vector2D.deserialize();
        Parser.consumeEndObjectProperty();

        Parser.consume(',');
        Parser.consumeBeginObjectProperty("Scale");
        Vector2D scale = Vector2D.deserialize();
        Parser.consumeEndObjectProperty();

        Parser.consume(',');
        double rotation = Parser.consumeDoubleProperty("Rotation");
        Parser.consumeEndObjectProperty();

        return new Transform(position, rotation, scale);
    }
}
