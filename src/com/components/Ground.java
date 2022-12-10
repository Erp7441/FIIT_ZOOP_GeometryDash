package com.components;

import com.engine.Component;
import com.engine.GameObject;
import com.engine.LevelScene;
import com.engine.Window;

import com.util.Constants;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Ground game object component that is the ground plane on which the player is moving.
 * It handles the creation and drawing of the ground plane within the camera view.
 *
 * @see GameObject GameObject – Base object from which everything is derived from.
 * @see Component Component – An add-on to the game object.
 */
public class Ground extends Component {

    /**
     * Updates the collision of a game object with the ground. If we are not inside the editor then
     * we will want to apply gravity to the game object by subtracting the position the parent game
     * object that this component is attached to from player height. Additionally, we want the camera
     * to follow the player position. If we are inside the editor then we just want to have the player
     * at the camera position sitting stationary in the editor.
     *
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see LevelScene LevelScene - Scene from which the player is playing the game.
     * @see Window Window – Window where the game is being rendered.
     */
    @Override
    public void update(double deltaTime) {
        if(!Window.getWindow().isInEditor()){
            LevelScene scene = (LevelScene) Window.getWindow().getCurrentScene();
            GameObject player = scene.getPlayer();

            if (player.getTransform().getPosition().getY() + player.getComponent(BoxBounds.class).getHeight() > getGameObject().getTransform().getPosition().getY()) {
                player.getTransform().getPosition().setY(getGameObject().getTransform().getPosition().getY() - player.getComponent(BoxBounds.class).getHeight());

                player.getComponent(Player.class).setOnGround(true);
            }

            getGameObject().getTransform().getPosition().setX(scene.getCamera().getPosition().getX());
        }
        else{
            getGameObject().getTransform().getPosition().setX(Window.getWindow().getCurrentScene().getCamera().getPosition().getX());
        }


    }

    /**
     * Renders the ground as a line on the screen. The line is precisely
     * the same size as the screen width. That means when the player is
     * moving, the ground will be rendered in front of him giving the player
     * a sense of infinite ground.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     */
    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect((int) getGameObject().getTransform().getPosition().getX(), (int) getGameObject().getTransform().getPosition().getY(), Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    @Override
    public Component copy() {
        return null; // Copy not needed for this component
    }

    @Override
    public String serialize(int tabSize) {
        return ""; // Serialize not needed for this component
    }
}
