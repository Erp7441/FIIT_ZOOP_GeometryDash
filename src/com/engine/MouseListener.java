package com.engine;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Responsible for handling player mouse movement.
 *
 * @see MouseAdapter MouseAdapter - Handler for mouse movement events.
 */
public class MouseListener extends MouseAdapter {
    public boolean mousePressed = false;
    public boolean mouseDragged = false;
    public float x = -1.0f, y = -1.0f;
    public float dx = -1.0f, dy = -1.0f;
    public int mouseButton = -1;

    /**
     * Sets mouse pressed state to true and sets button code pressed.
     *
     * @param mouseEvent the player's mouse event to be processed
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent){
        this.mousePressed = true;
        this.mouseButton = mouseEvent.getButton();
    }

    /**
     * Sets mouse pressed state to false, dragged state to false and resets
     * differencials in position so the mouse won't move anymore.
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
     * @param mouseEvent the player's mouse event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent){
        this.x = mouseEvent.getX();
        this.y = mouseEvent.getY();
    }

    /**
     *
     * @param mouseEvent the player's mouse event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent){
        this.mouseDragged = true;
        this.dx = mouseEvent.getX() - this.x;
        this.dy = mouseEvent.getY() - this.y;
    }

}
