package com.components;

import com.engine.Component;
import com.file.Parser;
import com.util.Vector2D;

import javax.swing.Box;

/**
 * Boundaries game object component is for objects that
 * need to collide within the game.
 *
 * @see Component Component – An add-on to the game object.
 */
public class BoxBounds extends Bounds {

    private double width;
    private double height;

    private double halftWidth;
    private double halftHeight;
    private Vector2D center = new Vector2D();

    public BoxBounds(double width, double height){
        this.width = width;
        this.height = height;
        this.halftWidth = width / 2.0;
        this.halftHeight = height / 2.0;
        this.type = BoundsType.Box;
    }

    @Override
    public void start() {
        calculateCenter();
    }

    public void calculateCenter(){
        this.center.setX(this.getGameObject().getTransform().getPosition().getX() + halftWidth);
        this.center.setY(this.getGameObject().getTransform().getPosition().getY() + halftHeight);
    }

    public static boolean checkCollision(BoxBounds a, BoxBounds b){
        a.calculateCenter();
        b.calculateCenter();

        double dx = b.center.getX() - a.center.getX();
        double dy = b.center.getY() - a.center.getY();
        double combinedWidth = a.halftWidth + b.halftWidth;
        double combinedHeight = a.halftHeight + b.halftHeight;

        if(Math.abs(dx) <= combinedWidth){
            return Math.abs(dy) <= combinedHeight;
        }

        return false;
    }

    @Override
    public void update(double deltaTime){
        // This class doesn't need update method
    }

    @Override
    public BoxBounds copy(){
        return new BoxBounds(this.width, this.height);
    }

    public double getWidth(){
        return width;
    }

    public void setWidth(double width){
        this.width = width;
    }

    public double getHeight(){
        return height;
    }

    public void setHeight(double height){
        this.height = height;
    }

    @Override
    public String serialize(int tabSize){
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("BoxBounds", tabSize));
        builder.append(addDoubleProperty("Width", width, tabSize + 1, true, true));
        builder.append(addDoubleProperty("Height", height, tabSize + 1, true, false));
        builder.append(endObjectProperty(tabSize));

        return builder.toString();
    }

    public static BoxBounds deserialize(){
        double width = Parser.consumeDoubleProperty("Width");
        Parser.consume(',');
        double height = Parser.consumeDoubleProperty("Height");
        Parser.consumeEndObjectProperty();
        return new BoxBounds(width, height);
    }
}
