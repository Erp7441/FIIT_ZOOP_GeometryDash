package com.components;

import com.engine.Component;
import com.engine.GameObject;
import com.engine.LevelScene;
import com.engine.Window;

import com.util.Constants;

import java.awt.Color;
import java.awt.Graphics2D;

public class Ground extends Component {


    @Override
    public void update(double deltaTime) {
        if(!Window.getWindow().isInEditor){
            LevelScene scene = (LevelScene)  Window.getWindow().getCurrentScene();
            GameObject player = scene.player;

            if (player.transform.position.y + player.getComponent(BoxBounds.class).height > gameObject.transform.position.y) {
                player.transform.position.y = gameObject.transform.position.y - player.getComponent(BoxBounds.class).height;
            }

            gameObject.transform.position.x = scene.camera.position.x;
        }
        else{
            gameObject.transform.position.x = Window.getWindow().getCurrentScene().camera.position.x;
        }


    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect((int) gameObject.transform.position.x, (int) gameObject.transform.position.y, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    @Override
    public Component copy() {
        return null; // Copy not needed for this component
    }
}
