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
    private double prevMx;
    private double prevMy;

    public CameraControls() {
       prevMx = 0.f;
       prevMy = 0.f;
    }

    /**
     * Updates the camera position based on the player mouse position.
     * This method checks if the player is holding middle mouse button and
     * if so, updates the camera position based on the mouse position. It calculates
     * the difference in x and y coordinates and updates the camera dynamically,so it won't
     * snap to a current player position on the screen right when the player is holding middle
     * mouse button.
     *
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     * @see Window Window – Window where the game is being rendered.
     */
    @Override
    public void update(double deltaTime) {
        if(Window.getWindow().getMouseListener().isMousePressed() && Window.getWindow().getMouseListener().getMouseButton() == MouseEvent.BUTTON2){
            double dx = (Window.getWindow().getMouseListener().getX() + Window.getWindow().getMouseListener().getDx() - prevMx);
            double dy = (Window.getWindow().getMouseListener().getY() + Window.getWindow().getMouseListener().getDy() - prevMy);

            Window.getWindow().getCurrentScene().getCamera().getPosition().setX(Window.getWindow().getCurrentScene().getCamera().getPosition().getX() - dx);
            Window.getWindow().getCurrentScene().getCamera().getPosition().setY(Window.getWindow().getCurrentScene().getCamera().getPosition().getY() - dy);
        }

        prevMx = Window.getWindow().getMouseListener().getX() + Window.getWindow().getMouseListener().getDx();
        prevMy = Window.getWindow().getMouseListener().getY() + Window.getWindow().getMouseListener().getDy();
    }

    @Override
    public CameraControls copy(){
        return null; // Copy not needed for this component
    }
}
