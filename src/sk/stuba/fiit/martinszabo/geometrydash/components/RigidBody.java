package sk.stuba.fiit.martinszabo.geometrydash.components;

import sk.stuba.fiit.martinszabo.geometrydash.engine.Component;

import sk.stuba.fiit.martinszabo.geometrydash.util.Constants;
import sk.stuba.fiit.martinszabo.geometrydash.util.Vector2D;

/**
 * Similar to Unreal Engine, The rigid body game object component makes the game object
 * rigid, so we can apply velocity and acceleration to it. This is useful for physics
 * of the game.
 *
 * @see Component Component – An add-on to the game object.
 */
public class RigidBody extends Component<RigidBody> {
    private Vector2D velocity;

    /**
     * Set's the velocity of the rigid body's parent game object on component construction.
     * @param velocity 2D vector that represents the velocity of the rigid body.
     * @see Vector2D Vector2D – Vector that has lenght and a direction within the 2D space.
     */
    public RigidBody(Vector2D velocity) {
        this.velocity = velocity; //! Aggregation
    }

    /**
     * Applies the velocity to parent game object over time.
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     */
    @Override
    public void update(double deltaTime){

        //? Rounding up then casting to int partially fixes bug with player clipping into the side of box when he is on top of the box.
        getGameObject().setX((int)Math.nextUp(getGameObject().getX() + velocity.getX() *  deltaTime));
        getGameObject().setY((int)Math.nextUp(getGameObject().getY() + velocity.getY() * deltaTime));

        velocity.setY(velocity.getY() + Constants.GRAVITY * deltaTime);

        if (Math.abs(velocity.getY()) > Constants.TERMINAL_VELOCITY){
            velocity.setY(Math.signum(velocity.getY()) * Constants.TERMINAL_VELOCITY);
        }
    }

    @Override
    public Component<RigidBody> copy() {
        return null; // Copy not needed for this component
    }

    public Vector2D getVelocity(){
        return velocity;
    }

    public void setVelocity(Vector2D velocity){
        this.velocity = velocity;
    }

    @Override
    public String serialize(int tabSize) {
        return ""; // Serialize not needed for this component
    }
}
