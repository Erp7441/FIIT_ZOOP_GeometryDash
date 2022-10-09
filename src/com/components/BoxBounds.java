package com.components;

import com.engine.Component;

/**
 * Bounderies game object component is for objects that
 * need to colide within the game.
 *
 * @see Component Component – An add-on to the game object.
 */
public class BoxBounds extends Component {

    public double width, height;

    public BoxBounds(double width, double height) {
        this.width = width;
        this.height = height;
    }
    @Override
    public void update(double deltaTime){

    }

    @Override
    public BoxBounds copy(){
        return new BoxBounds(this.width, this.height);
    }
}
