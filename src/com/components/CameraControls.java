package com.components;

import com.engine.Component;
import com.engine.Window;

import java.awt.event.MouseEvent;

public class CameraControls extends Component {

    private double prevMx, prevMy;

    public CameraControls() {
       prevMx = 0.f;
       prevMy = 0.f;
    }

    @Override
    public void update(double deltaTime) {
        if(Window.getWindow().mouseListener.mousePressed && Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON2){
            double dx = (Window.getWindow().mouseListener.x + Window.getWindow().mouseListener.dx - prevMx);
            double dy = (Window.getWindow().mouseListener.y + Window.getWindow().mouseListener.dy - prevMy);

            Window.getWindow().getCurrentScene().camera.position.x -= dx;
            Window.getWindow().getCurrentScene().camera.position.y -= dy;
        }

        prevMx = Window.getWindow().mouseListener.x + Window.getWindow().mouseListener.dx;
        prevMy = Window.getWindow().mouseListener.y + Window.getWindow().mouseListener.dy;
    }
}
