package sk.stuba.fiit.martinszabo.geometrydash.components;

import sk.stuba.fiit.martinszabo.geometrydash.engine.Component;
import sk.stuba.fiit.martinszabo.geometrydash.engine.GameObject;
import sk.stuba.fiit.martinszabo.geometrydash.file.Parser;
import sk.stuba.fiit.martinszabo.geometrydash.util.Constants;
import sk.stuba.fiit.martinszabo.geometrydash.util.Vector2D;

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

    private double halfWidth;
    private double halfHeight;
    private double enclosingRadius;
    private final Vector2D center = new Vector2D();
    private boolean isTrigger;
    private double xBuffer;
    private double yBuffer;

    public BoxBounds(double width, double height){
        init (width, height, 0.0, 0.0, false);
    }

    public BoxBounds(double width, double height, boolean isTrigger){
        init (width, height, 0.0, 0.0, isTrigger);
    }
    public BoxBounds(double width, double height, double xBuffer, double yBuffer, boolean isTrigger){
        init (width, height, xBuffer, yBuffer, isTrigger);
    }

    private void init(double width, double height, double xBuffer, double yBuffer, boolean isTrigger){
        this.width = width;
        this.height = height;
        this.halfWidth = width / 2.0;
        this.halfHeight = height / 2.0;
        this.enclosingRadius = Math.sqrt((this.halfWidth * halfWidth) + (this.halfHeight * halfHeight));
        this.setType(BoundsType.BOX);
        this.isTrigger = isTrigger;
        this.xBuffer = xBuffer;
        this.yBuffer = yBuffer;
    }

    @Override
    public void start() {
        calculateCenter();
    }

    public void calculateCenter(){
        this.center.setX(this.getGameObject().getX() + halfWidth + this.xBuffer);
        this.center.setY(this.getGameObject().getY() + halfHeight + this.yBuffer);
    }

    public static boolean checkCollision(BoxBounds a, BoxBounds b){
        a.calculateCenter();
        b.calculateCenter();

        double dx = b.center.getX() - a.center.getX();
        double dy = b.center.getY() - a.center.getY();
        double combinedWidth = a.halfWidth + b.halfWidth;
        double combinedHeight = a.halfHeight + b.halfHeight;

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
        double combinedWidth = playerBounds.halfWidth + this.halfWidth;
        double combinedHeight = playerBounds.halfHeight + this.halfHeight;

        double overlapX = combinedWidth - Math.abs(dx);
        double overlapY = combinedHeight - Math.abs(dy);

        if (overlapX >= overlapY){
            if (dy > 0){
                // Collision on top of the player
                player.setY(getGameObject().getY() - playerBounds.getHeight() + yBuffer);
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
                player.setY(getGameObject().getY() - playerBounds.getHeight() + yBuffer);
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
        builder.append(addDoubleProperty("xBuffer", xBuffer, tabSize + 1, true, true));
        builder.append(addDoubleProperty("yBuffer", yBuffer, tabSize + 1, true, true));
        builder.append(addBooleanProperty("isTrigger", isTrigger, tabSize + 1, true, false));
        builder.append(endObjectProperty(tabSize));

        return builder.toString();
    }

    public static BoxBounds deserialize(){
        double width = Parser.consumeDoubleProperty("Width");
        Parser.consume(',');
        double height = Parser.consumeDoubleProperty("Height");
        Parser.consume(',');
        double xBuffer = Parser.consumeDoubleProperty("xBuffer");
        Parser.consume(',');
        double yBuffer = Parser.consumeDoubleProperty("yBuffer");
        Parser.consume(',');
        boolean isTrigger = Parser.consumeBooleanProperty("isTrigger");
        Parser.consumeEndObjectProperty();
        return new BoxBounds(width, height, xBuffer, yBuffer, isTrigger);
    }
    @Override
    public void update(double deltaTime){
        // This class doesn't need update method
    }

    @Override
    public BoxBounds copy(){
        return new BoxBounds(this.width, this.height, this.xBuffer, this.yBuffer, this.isTrigger);
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

    public double getHalfWidth(){
        return halfWidth;
    }

    public double getHalfHeight(){
        return halfHeight;
    }

    public double getEnclosingRadius(){
        return enclosingRadius;
    }

    @Override
    public boolean raycast(Vector2D position){
        return (
            position.getX() > this.getGameObject().getX() + xBuffer &&
            position.getX() < this.getGameObject().getX() +xBuffer + this.width &&
            position.getY() > this.getGameObject().getY() + yBuffer &&
            position.getY() < this.getGameObject().getY() + yBuffer + this.height
        );
    }

    @Override
    public void draw(Graphics2D graphics2D){
        if (isSelected()){
            graphics2D.setColor(Color.GREEN);
            graphics2D.setStroke(Constants.THICK_LINE);
            graphics2D.draw(new Rectangle2D.Double(
                this.getGameObject().getX() + xBuffer,
                this.getGameObject().getY() + yBuffer,
                this.width, this.height
            ));
            graphics2D.setStroke(Constants.LINE);
        }
    }

    public void setYBuffer(double yBuffer){
        this.yBuffer = yBuffer;
    }
}
