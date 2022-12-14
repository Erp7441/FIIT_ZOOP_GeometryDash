package sk.stuba.fiit.martinszabo.geometrydash.util;

import sk.stuba.fiit.martinszabo.geometrydash.file.Parser;
import sk.stuba.fiit.martinszabo.geometrydash.file.Serialization;

import java.util.Objects;

/**
 * Vector class representing 2 dimensional vector from physics with X and Y coordinates.
 */
public class Vector2D extends Serialization{
    private double x;
    private double y;

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

    public double getX(){
        return x;
    }

    public void setX(double x){
        this.x = x;
    }

    public double getY(){
        return y;
    }

    public void setY(double y){
        this.y = y;
    }

    @Override
    public String serialize(int tabSize){
        StringBuilder builder = new StringBuilder();

        builder.append(addDoubleProperty("x", this.x, tabSize, true, true));
        builder.append(addDoubleProperty("y", this.y, tabSize, true, false));
        return builder.toString();
    }

    public static Vector2D deserialize(){
        double x = Parser.consumeDoubleProperty("x");
        Parser.consume(',');
        double y = Parser.consumeDoubleProperty("y");
        return new Vector2D(x, y);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(o instanceof  Vector2D){
            Vector2D vector2D = (Vector2D) o;
            return Double.compare(vector2D.x, x) == 0 && Double.compare(vector2D.y, y) == 0;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(x, y);
    }
}
