package sk.stuba.fiit.martinszabo.geometrydash.engine;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Responsible for handling player mouse movement.
 *
 * @see MouseAdapter MouseAdapter - Handler for mouse movement events.
 */
public class MouseListener extends MouseAdapter {
    private boolean mousePressed = false;
    private boolean mouseDragged = false;
    private float x = -1.0f;
    private float y = -1.0f;
    private float dx = -1.0f;
    private float dy = -1.0f;
    private int mouseButton = -1;

    /**
     * Sets mouse pressed state to true and sets button code pressed.
     *
     * @param mouseEvent the player's mouse event to be processed.
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent){
        this.mousePressed = true;
        this.mouseButton = mouseEvent.getButton();
    }

    /**
     * Sets mouse pressed state to false, dragged state to false and resets
     * differentials in position so the mouse won't move anymore.
     *
     * @param mouseEvent the player's mouse event to be processed.
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent){
        this.mousePressed = false;
        this.mouseDragged = false;
        this.dx = 0;
        this.dy = 0;
    }

    /**
     * Sets mouse position to new one.
     *
     * @param mouseEvent the player's mouse event to be processed.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent){
        this.x = mouseEvent.getX();
        this.y = mouseEvent.getY();
    }

    /**
     * Calculates the distance between where the mouse is pointing and where it was.
     *
     * @param mouseEvent the player's mouse event to be processed.
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent){
        this.mouseDragged = true;
        this.dx = mouseEvent.getX() - this.x;
        this.dy = mouseEvent.getY() - this.y;
    }

    public boolean isMousePressed(){
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed){
        this.mousePressed = mousePressed;
    }

    public boolean isMouseDragged(){
        return mouseDragged;
    }

    public void setMouseDragged(boolean mouseDragged){
        this.mouseDragged = mouseDragged;
    }

    public float getX(){
        return x;
    }

    public void setX(float x){
        this.x = x;
    }

    public float getY(){
        return y;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getDx(){
        return dx;
    }

    public void setDx(float dx){
        this.dx = dx;
    }

    public float getDy(){
        return dy;
    }

    public void setDy(float dy){
        this.dy = dy;
    }

    public int getMouseButton(){
        return mouseButton;
    }

    public void setMouseButton(int mouseButton){
        this.mouseButton = mouseButton;
    }
}
