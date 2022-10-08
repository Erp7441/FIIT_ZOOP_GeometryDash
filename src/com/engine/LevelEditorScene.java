package com.engine;

import com.components.Player;
import com.components.Spritesheet;
import com.util.Constants;
import com.util.Transform;
import com.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;

public class LevelEditorScene extends Scene{

    GameObject player = null;
    public LevelEditorScene(String name) {
        super.Scene(name);
    }

    @Override
    public void init() {
        player = new GameObject("Some game object", new Transform(new Vector2D(300.0,300.0)));
        Spritesheet layerOne = new Spritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13 * 5);
        Spritesheet layerTwo = new Spritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13 * 5);
        Spritesheet layerThree = new Spritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13 * 5);
        Player playerComp = new Player(
                layerOne.sprites.get(0),
                layerTwo.sprites.get(0),
                layerThree.sprites.get(0),
                Color.RED,
                Color.GREEN
        );
        player.addComponent(playerComp);
        renderer.submit(player);
    }

    @Override
    public void update(double deltaTime) {
        player.update(deltaTime);
        player.transform.rotation += deltaTime * 1f;
        camera.position.x += deltaTime * 50f;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        renderer.render(graphics2D);
    }
}
