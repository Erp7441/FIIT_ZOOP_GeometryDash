package com.components;

import com.engine.Component;

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
