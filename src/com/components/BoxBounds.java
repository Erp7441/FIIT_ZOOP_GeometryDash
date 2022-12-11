package com.components;

import com.engine.Component;
import com.engine.GameObject;
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
    private double enclosingRadius;
    private Vector2D center = new Vector2D();

    public BoxBounds(double width, double height){
        this.width = width;
        this.height = height;
        this.halftWidth = width / 2.0;
        this.halftHeight = height / 2.0;
        this.enclosingRadius = Math.sqrt((this.halftWidth * halftWidth) + (this.halftHeight * halftHeight));
        this.type = BoundsType.BOX;
    }

    @Override
    public void start() {
        calculateCenter();
    }

    public void calculateCenter(){
        this.center.setX(this.getGameObject().getX() + halftWidth);
        this.center.setY(this.getGameObject().getY() + halftHeight);
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
    public void resolveCollision(GameObject player){
        BoxBounds playerBounds = player.getComponent(BoxBounds.class);
        playerBounds.calculateCenter();
        this.calculateCenter();

        double dx = this.center.getX() - playerBounds.center.getX();
        double dy = this.center.getY() - playerBounds.center.getY();
        double combinedWidth = playerBounds.halftWidth + this.halftWidth;
        double combinedHeight = playerBounds.halftHeight + this.halftHeight;

        double overlapX = combinedWidth - Math.abs(dx);
        double overlapY = combinedHeight - Math.abs(dy);

        // TODO randomly dying (due to rotation issue)
        if (overlapX >= overlapY){
            if (dy > 0){
                // Collision on top of the player
                player.setY(getGameObject().getY() - playerBounds.getHeight());
                player.getComponent(RigidBody.class).getVelocity().setY(0);
                player.getComponent(Player.class).setOnGround(true);
            }
            else{
                // Collision on bottom of the player
                player.getComponent(Player.class).die();
            }
        }
        else{
            // Collision on left or right of the player
            if (dx < 0 && dy <= 0.3){
                player.setY(getGameObject().getY() - playerBounds.getHeight());
                player.getComponent(RigidBody.class).getVelocity().setY(0);
                player.getComponent(Player.class).setOnGround(true);
            }
            else{
                player.getComponent(Player.class).die();
            }
        }
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
    @Override
    public void update(double deltaTime){
        // This class doesn't need update method
    }

    @Override
    public BoxBounds copy(){
        return new BoxBounds(this.width, this.height);
    }

    @Override
    public double getWidth(){
        return width;
    }

    public void setWidth(double width){
        this.width = width;
    }

    @Override
    public double getHeight(){
        return height;
    }

    public void setHeight(double height){
        this.height = height;
    }

    public double getHalftWidth(){
        return halftWidth;
    }

    public double getHalftHeight(){
        return halftHeight;
    }

    public double getEnclosingRadius(){
        return enclosingRadius;
    }
}
