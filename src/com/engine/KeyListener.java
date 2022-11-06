package com.engine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Responsible for handling key events from the player.
 */
public class KeyListener extends KeyAdapter implements java.awt.event.KeyListener {
    // Array of boolean values. The index maches key code and value maches if pressed state.
    private boolean keyPressed[] = new boolean[128]; // TODO change type to ArrayList here

    /**
     * Sets the corresponding key value to true if the key is pressed
     *
     * @param keyEvent the key event triggered by the player
     */
    @Override
    public void keyPressed(KeyEvent keyEvent){
        keyPressed[keyEvent.getKeyCode()] = true;
    }

    /**
     * Sets the corresponding key value to false if the key is not pressed
     *
     * @param keyEvent the key event triggered by the player
     */
    @Override
    public void keyReleased(KeyEvent keyEvent){
        keyPressed[keyEvent.getKeyCode()] = false;
    }

    /**
     * Gets the corresponding key pressed state value.
     *
     * @param keyCode the key code we are looking for in the array.
     * @return Key state corresponding to the key code
     */
    public boolean isKeyPressed(int keyCode){
        return keyPressed[keyCode];
    }
}
