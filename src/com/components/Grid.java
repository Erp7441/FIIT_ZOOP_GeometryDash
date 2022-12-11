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
    private int width;
    private int height;
    private final int numYLines = 31;
    private final int numXLines = 31;

    /**
     * Setting up tile dimensions and current window camera.
     *
     * @see Camera Camera - player viewport of the game window.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see Window Window – Window where the game is being rendered.
     */
    public Grid(){
        this.camera = Window.getCamera(); //! Aggregation
        this.width = Constants.TILE_WIDTH;
        this.height = Constants.TILE_HEIGHT;
    }

    @Override
    public void update(double deltaTime) {
        // This class doesn't need update method
    }

    /**
     * Draws the grid lines from camera position leftmost visible point to
     * camera position rightmost visible point horizontally. Vertically it
     * draws the grid lines from camera position the highest point to the ground,
     * so it won't overlap with the ground line.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     * @see Camera Camera - player viewport in the game window.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     */
    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(new Color(0.2f, 0.2f, 0.2f, 0.8f));

        // Ground line to not overlap with.
        double bottom = Math.min(Constants.GROUND_Y - camera.getPosition().getY(), Constants.SCREEN_HEIGHT);

        // Starting points for drawing the lines
        double startX = Math.floor(this.camera.getPosition().getX() / this.width) * this.width - this.camera.getPosition().getX();
        double startY = Math.floor(this.camera.getPosition().getY() / this.height) * this.height - this.camera.getPosition().getY();

        for (int column = 0; column <= numYLines; column++){
            graphics2D.draw(new Line2D.Double(startX, 0, startX, bottom));
            startX += this.width;
        }

        for (int row = 0; row <= numXLines; row++){
            if(camera.getPosition().getY() + startY < Constants.GROUND_Y){
                graphics2D.draw(new Line2D.Double(0, startY, Constants.SCREEN_WIDTH, startY));
                startY += this.height;
            }
        }
    }

    @Override
    public Component copy() {
        return null; // Copy not needed for this component
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    @Override
    public String serialize(int tabSize) {
        return ""; // Serialize not needed for this component
    }
}
