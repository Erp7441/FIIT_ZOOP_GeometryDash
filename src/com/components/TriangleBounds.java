package com.components;

import com.engine.Component;
import com.file.Parser;
import com.util.Constants;
import com.util.Vector2D;

import java.awt.Graphics2D;
import java.awt.geom.Line2D; // For visual debugging
import java.util.Vector;

public class TriangleBounds extends Bounds {
    private double enclosingRadius;
    private double base; // Width at the base of the triangle
    private double height;
    private double halfWidth;
    private double halfHeight;

    private Vector2D point1;
    private Vector2D point2;
    private Vector2D point3;

    // Binary region codes
    private final int INSIDE = 0;
    private final int LEFT = 1;
    private final int RIGHT = 2;
    private final int BOTTOM = 4;
    private final int TOP = 8;


    public TriangleBounds(double base, double height){
        this.type = BoundsType.TRIANGLE;
        this.base = base;
        this.height = height;
        this.halfWidth = base / 2.0;
        this.halfHeight = height / 2.0;
        this.enclosingRadius = Math.max(this.halfWidth, this.halfHeight);
    }

    @Override
    public void start() {
        calculateTransformation();
    }

    public static boolean checkCollision(BoxBounds a, TriangleBounds b){
        if(b.broadPhase(a)){
            System.out.println("Broad phase!");
            return b.narrowPhase(a);
        }
        return false;
    }

    private boolean broadPhase(BoxBounds a){
        double aRadius = a.getEnclosingRadius();
        double thisRadius = this.enclosingRadius;

        double centerX = this.point2.getX();
        double centerY = this.point2.getY() + halfHeight;

        double playerCenterX = a.getGameObject().getX() + a.getHalftWidth();
        double playerCenterY = a.getGameObject().getY() + a.getHalftHeight();

        Vector2D distance = new Vector2D(playerCenterX - centerX, playerCenterY - centerY);
        double magnitudeSquared = (distance.getX() * distance.getX()) + (distance.getY() * distance.getY());
        double radiusSquared = (aRadius + thisRadius) * (aRadius + thisRadius);

        return magnitudeSquared <= radiusSquared;
    }

    private boolean narrowPhase(BoxBounds a){
        Vector2D p1 = point1.copy();
        Vector2D p2 = point2.copy();
        Vector2D p3 = point3.copy();

        Vector2D origin = new Vector2D(a.getGameObject().getX() + (a.getWidth() / 2.0), a.getGameObject().getY() + (a.getHeight() / 2.0));
        double radiansAngle = Math.toRadians(a.getGameObject().getTransform().getRotation());

        p1 = rotatePoint(radiansAngle, p1, origin);
        p2 = rotatePoint(radiansAngle, p2, origin);
        p3 = rotatePoint(radiansAngle, p3, origin);

        return (
            boxIntersection(p1, p2, 0, a, a.getGameObject().getTransform().getPosition()) ||
            boxIntersection(p1, p3, 0, a, a.getGameObject().getTransform().getPosition()) ||
            boxIntersection(p2, p3, 0, a, a.getGameObject().getTransform().getPosition())
        );
    }

    // TODO refactor from recursion to loop
    private boolean boxIntersection(Vector2D p1, Vector2D p2, int depth, BoxBounds bounds, Vector2D position) {

        // Cohen Sutherland clipping algorithm
        if(depth > 5){
            System.out.println("DEBUG: Max depth");
            return true;
        }

        int code1 = regionCode(p1, bounds);
        int code2 = regionCode(p2, bounds);

        // Checking if line is fully inside or outsite of the bounds or halfway between
        if(code1 == 0 && code2 == 0){
            return true; // line is fully inside the bounds
        }
        else if ((code1 & code2) != 0){
            return false; // line is not intersecting
        }
        else if (code1 == 0 || code2 == 0){
            return true; // One code is inside the bounds one is outside the bounds
        }

        int xMax = (int)(position.getX() + bounds.getWidth());
        int xMin = (int)(position.getX());

        double m = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        double b = p2.getY() - (m * (p2.getX()));

        if ((code1 & LEFT) == LEFT){
            // Add 1 to ensure we are clipping
            p1.setX(xMin + 1);
        }
        else if ((code1 & RIGHT) == RIGHT){
            // Subtract 1 to ensure we are clipping
            // When we are on the right side we use xMax. When we are on the left side we use xMin.
            p1.setX(xMax - 1);
        }
        p1.setY((m * p1.getX()) + b); // y = mx + b

        // Again for p2 vector
        if ((code1 & LEFT) == LEFT){
            // Add 1 to ensure we are clipping
            p2.setX(xMin + 1);
        }
        else if ((code1 & RIGHT) == RIGHT){
            // Subtract 1 to ensure we are clipping
            // When we are on the right side we use xMax. When we are on the left side we use xMin.
            p2.setX(xMax - 1);
        }
        p2.setY((m * p2.getX()) + b); // y = mx + b

        return boxIntersection(p1, p2, depth + 1, bounds, position);
    }

    private int regionCode(Vector2D point, BoxBounds bounds){
        int code = INSIDE;
        Vector2D topLeft = bounds.getGameObject().getTransform().getPosition();

        // Point is to the left or right of the bounds box.
        if (point.getX() < topLeft.getX()){
            code |= LEFT;
        }
        else if (point.getX() > topLeft.getX() + bounds.getWidth()){
            code |= RIGHT;
        }

        // Point is to the top or bottom of the bounds box
        if (point.getY() < topLeft.getY()){
            code |= TOP;
        }
        else if (point.getY() > topLeft.getY() + bounds.getHeight()){
            code |= BOTTOM;
        }

        return code;
    }

    /**
     *
     * @param angle - angle of rotation
     * @param point - point we want to rotate
     * @param origin - origin we want to rotate around
     * @return
     */
    private Vector2D rotatePoint(double angle, Vector2D point, Vector2D origin){
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        Vector2D newVector = new Vector2D(point.getX(), point.getY());
        newVector.setX(newVector.getX() - origin.getX());
        newVector.setY(newVector.getY() - origin.getY());

        double newX = (newVector.getX() * cos) - (newVector.getY() * sin);
        double newY = (newVector.getX() * sin) + (newVector.getY() * cos);

        return new Vector2D(newX + origin.getX(), newY + origin.getY());
    }

    private void calculateTransformation(){
        double radiansAngle = Math.toRadians(getGameObject().getTransform().getRotation());
        point1 = new Vector2D(getGameObject().getX(), getGameObject().getY() + height);
        point2 = new Vector2D(getGameObject().getX() + halfWidth, getGameObject().getY());
        point3 = new Vector2D(getGameObject().getX() + base, getGameObject().getY() + height);
        Vector2D origin = new Vector2D(getGameObject().getX() + (Constants.TILE_WIDTH / 2.0), getGameObject().getY() + (Constants.TILE_HEIGHT / 2.0));

        point1 = rotatePoint(radiansAngle, point1, origin);
        point2 = rotatePoint(radiansAngle, point2, origin);
        point3 = rotatePoint(radiansAngle, point3, origin);
    }

    @Override
    public double getWidth(){
        return this.base;
    }

    @Override
    public double getHeight(){
        return this.height;
    }

    // Visual debugging
    @Override
    public void draw(Graphics2D graphics2D){
        //graphics2D.draw(new Line2D.Double(point1.getX(), point1.getY(), point2.getX(), point2.getY()));
        //graphics2D.draw(new Line2D.Double(point1.getX(), point1.getY(), point3.getX(), point3.getY()));
        //graphics2D.draw(new Line2D.Double(point3.getX(), point3.getY(), point2.getX(), point2.getY()));
    }

    @Override
    public Component copy(){
        return new TriangleBounds(this.base, this.height);
    }

    @Override
    public String serialize(int tabSize){
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("TriangleBounds", tabSize));
        builder.append(addDoubleProperty("Base", base, tabSize + 1, true, true));
        builder.append(addDoubleProperty("Height", height, tabSize + 1, true, false));
        builder.append(endObjectProperty(tabSize));

        return builder.toString();
    }

    public static TriangleBounds deserialize(){
        double base = Parser.consumeDoubleProperty("Base");
        Parser.consume(',');
        double height = Parser.consumeDoubleProperty("Height");
        Parser.consumeEndObjectProperty();
        return new TriangleBounds(base, height);
    }

}
