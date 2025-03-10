package sk.stuba.fiit.martinszabo.geometrydash.components;

import sk.stuba.fiit.martinszabo.geometrydash.engine.Component;
import sk.stuba.fiit.martinszabo.geometrydash.engine.Window;

import java.awt.event.MouseEvent;

/**
 * Camera controls game object component for managing and moving the camera around the screen.
 *
 * @see Component Component – An add-on to the game object.
 */
public class CameraControls extends Component<CameraControls> {

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
        if(Window.getMouseListener().isMousePressed() && Window.getMouseListener().getMouseButton() == MouseEvent.BUTTON2){
            double dx = (Window.getMouseListener().getX() + Window.getMouseListener().getDx() - prevMx);
            double dy = (Window.getMouseListener().getY() + Window.getMouseListener().getDy() - prevMy);

            Window.getCamera().setX(Window.getCamera().getX() - dx);
            Window.getCamera().setY(Window.getCamera().getY() - dy);
        }

        prevMx = Window.getMouseListener().getX() + Window.getMouseListener().getDx();
        prevMy = Window.getMouseListener().getY() + Window.getMouseListener().getDy();
    }

    @Override
    public CameraControls copy(){
        return null; // Copy not needed for this component
    }

    @Override
    public String serialize(int tabSize) {
        return ""; // Serialize not needed for this component
    }
}
