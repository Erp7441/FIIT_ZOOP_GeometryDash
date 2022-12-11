package com.components;

import com.engine.Component;
import com.engine.GameObject;
import com.engine.Window;
import com.file.Parser;
import com.util.Constants;
import com.util.Vector2D;

import javax.swing.Box;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

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
    private boolean isTrigger;

    public BoxBounds(double width, double height){
        init (width, height, false);
    }

    public BoxBounds(double width, double height, boolean isTrigger){
        init (width, height, isTrigger);
    }

    private void init(double width, double height, boolean isTrigger){
        this.width = width;
        this.height = height;
        this.halftWidth = width / 2.0;
        this.halftHeight = height / 2.0;
        this.enclosingRadius = Math.sqrt((this.halftWidth * halftWidth) + (this.halftHeight * halftHeight));
        this.setType(BoundsType.BOX);
        this.isTrigger = isTrigger;
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
        if (isTrigger) { return; }

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
        builder.append(addDoubleProperty("Height", height, tabSize + 1, true, true));
        builder.append(addBooleanProperty("isTrigger", this.isTrigger, tabSize + 1, true, false));
        builder.append(endObjectProperty(tabSize));

        return builder.toString();
    }

    public static BoxBounds deserialize(){
        double width = Parser.consumeDoubleProperty("Width");
        Parser.consume(',');
        double height = Parser.consumeDoubleProperty("Height");
        Parser.consume(',');
        boolean isTrigger = Parser.consumeBooleanProperty("isTrigger");
        Parser.consumeEndObjectProperty();
        return new BoxBounds(width, height, isTrigger);
    }
    @Override
    public void update(double deltaTime){
        // This class doesn't need update method
    }

    @Override
    public BoxBounds copy(){
        return new BoxBounds(this.width, this.height, this.isTrigger);
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

    @Override
    public boolean raycast(Vector2D position){
        return (
            position.getX() > this.getGameObject().getX() &&
            position.getX() < this.getGameObject().getX() + this.width &&
            position.getY() > this.getGameObject().getY() &&
            position.getY() < this.getGameObject().getY() + this.height
        );
    }

    @Override
    public void draw(Graphics2D graphics2D){
        if (isSelected()){
            graphics2D.setColor(Color.GREEN);
            graphics2D.setStroke(Constants.THICK_LINE);
            graphics2D.draw(new Rectangle2D.Double(
                this.getGameObject().getX(),
                this.getGameObject().getY(),
                this.width, this.height
            ));
            graphics2D.setStroke(Constants.LINE);
        }
    }
}
