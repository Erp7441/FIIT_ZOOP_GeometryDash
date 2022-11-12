package com.components;

import com.engine.Component;

import com.util.Constants;
import com.util.Vector2D;

/**
 * Similar to Unreal Engine, The rigid body game object component makes the game object
 * rigid, so we can apply velocity and acceleration to it. This is useful for physics
 * of the game.
 *
 * @see Component Component – An add-on to the game object.
 */
public class RigidBody extends Component{
    public Vector2D velocity;

    /**
     * Set's the velocity of the rigid body's parent game object on component construction.
     * @param velocity 2D vector that represents the velocity of the rigid body.
     * @see Vector2D Vector2D – Vector that has lenght and a direction within the 2D space.
     */
    public RigidBody(Vector2D velocity) {
        this.velocity = velocity; //! Agregation
    }

    /**
     * Applies the velocity to parent game object over time.
     * @param deltaTime Diffrence between last mouse update time and current mouse update time.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     */
    @Override
    public void update(double deltaTime){
        gameObject.transform.position.x += velocity.x * deltaTime;
        gameObject.transform.position.y += velocity.y * deltaTime;

        velocity.y += Constants.GRAVITY * deltaTime;

        if (Math.abs(velocity.y) > Constants.TERMINAL_VELOCITY){
            velocity.y = Math.signum(velocity.y) * Constants.TERMINAL_VELOCITY;
        }
    }

    @Override
    public Component copy() {
        return null; // Copy not needed for this component
    }
}
