package com.components;

import com.engine.Component;

import com.util.Constants;
import com.util.Vector2D;

public class RigidBody extends Component{
    public Vector2D velocity;

    public RigidBody(Vector2D velocity) {
        this.velocity = velocity;
    }

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
