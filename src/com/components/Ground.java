package com.components;

import com.engine.Component;
import com.engine.GameObject;
import com.engine.LevelEditorScene;
import com.util.Constants;
import java.awt.Color;
import java.awt.Graphics2D;

public class Ground extends Component {


    @Override
    public void update(double deltaTime) {
        GameObject player = LevelEditorScene.getScene().player;

        if (player.transform.position.y + player.getComponent(BoxBounds.class).height > gameObject.transform.position.y) {
            player.transform.position.y = gameObject.transform.position.y - player.getComponent(BoxBounds.class).height;
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect((int) gameObject.transform.position.x, (int) gameObject.transform.position.y, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }
}
