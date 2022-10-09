package com.components;

import com.engine.Component;
import com.engine.Window;

import java.awt.event.MouseEvent;

/**
 * Camera controls game object component for managing and moving the camera around the screen.
 *
 * @see Component Component – An add-on to the game object.
 */
public class CameraControls extends Component {

    // Previous mouse position
    private double prevMx, prevMy;

    public CameraControls() {
       prevMx = 0.f;
       prevMy = 0.f;
    }

    /**
     * Updates the camera position based on the player mouse position.
     * This method checks if the player is holding middle mouse button and
     * if so, updates the camera position based on the mouse position. It calculates
     * the diffrence in x and y coordinates and updates the camera dynamically,so it won't
     * snap to a current player position on the screen right when the player is holding middle
     * mouse button.
     *
     * @param deltaTime Diffrence between last mouse update time and current mouse update time.
     * @see Window Window – Window where the game is being rendered.
     */
    @Override
    public void update(double deltaTime) {
        if(Window.getWindow().mouseListener.mousePressed && Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON2){
            double dx = (Window.getWindow().mouseListener.x + Window.getWindow().mouseListener.dx - prevMx);
            double dy = (Window.getWindow().mouseListener.y + Window.getWindow().mouseListener.dy - prevMy);

            Window.getWindow().getCurrentScene().camera.position.x -= dx;
            Window.getWindow().getCurrentScene().camera.position.y -= dy;
        }

        prevMx = Window.getWindow().mouseListener.x + Window.getWindow().mouseListener.dx;
        prevMy = Window.getWindow().mouseListener.y + Window.getWindow().mouseListener.dy;
    }

    @Override
    public CameraControls copy(){
        return null; // Copy not needed for this component
    }
}
