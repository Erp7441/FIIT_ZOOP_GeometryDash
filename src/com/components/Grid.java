package com.components;

import com.engine.Camera;
import com.engine.Component;
import com.engine.Window;

import com.util.Constants;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;

/**
 * Grid game object component that is displayed when the game is in editor mode.
 * It is responsible for positioning and drawing the grid within the screen without
 * overlapping the ground.
 *
 * @see Component Component – An add-on to the game object.
 * @see Window Window – Window where the game is being rendered.
 */
public class Grid extends Component {

    Camera camera; // Reference to the camera in current scene
    public int width, height;
    private int numYLines = 31;
    private int numXLines = 31;

    /**
     * Setting up tile dimensions and current window camera.
     *
     * @see Camera Camera - player viewport of the game window.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see Window Window – Window where the game is being rendered.
     */
    public Grid(){
        this.camera = Window.getWindow().getCurrentScene().camera;
        this.width = Constants.TILE_WIDTH;
        this.height = Constants.TILE_HEIGHT;
    }

    @Override
    public void update(double deltaTime) {}

    /**
     * Draws the grid lines from camera position leftmost visible point to
     * camera position rightmost visible point horizontally. Vertically it
     * draws the grid lines from camera position the highest point to the ground,
     * so it won't overlap with the ground line.
     *
     * @param graphics2D 2D graphics handler.
     * @see Camera Camera - player viewport in the game window.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     */
    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(new Color(0.2f, 0.2f, 0.2f, 0.8f));

        // Ground line to not overlap with.
        double bottom = Math.min(Constants.GROUND_Y - camera.position.y, Constants.SCREEN_HEIGHT);

        // Starting points for drawing the lines
        double startX = Math.floor(this.camera.position.x / this.width) * this.width - this.camera.position.x;
        double startY = Math.floor(this.camera.position.y / this.height) * this.height - this.camera.position.y;

        for (int column = 0; column <= numYLines; column++){
            graphics2D.draw(new Line2D.Double(startX, 0, startX, bottom));
            startX += this.width;
        }

        for (int row = 0; row <= numXLines; row++){
            if(camera.position.y + startY < Constants.GROUND_Y){
                graphics2D.draw(new Line2D.Double(0, startY, Constants.SCREEN_WIDTH, startY));
                startY += this.height;
            }
        }
    }

    @Override
    public Component copy() {
        return null; // Copy not needed for this component
    }
}
