package com.components;

import com.engine.Component;
import com.engine.GameObject;
import com.engine.Window;
import com.util.Constants;
import com.util.Vector2D;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class SnapToGrid  extends Component {
    public Sprite sprite = null;

    private double debounceTime = 0.2;
    private double debounceLeft = 0.0;
    private int width = 0;
    private int height = 0;

    public SnapToGrid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(double deltaTime){
        debounceLeft -= deltaTime;

        if(this.gameObject.getComponent(Sprite.class) != null){
            double x = Math.floor((Window.getWindow().mouseListener.x + Window.getWindow().getCurrentScene().camera.position.x + Window.getWindow().mouseListener.dx) / this.width);
            double y = Math.floor((Window.getWindow().mouseListener.y + Window.getWindow().getCurrentScene().camera.position.y + Window.getWindow().mouseListener.dy) / this.height);
            this.gameObject.transform.position.x = x * this.width - Window.getWindow().getCurrentScene().camera.position.x;
            this.gameObject.transform.position.y = y * this.height - Window.getWindow().getCurrentScene().camera.position.y;

            if(Window.getWindow().mouseListener.y < Constants.BUTTON_OFFSET_Y && Window.getWindow().mouseListener.mousePressed && Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1 && debounceLeft < 0) {
                debounceLeft = debounceTime;
                GameObject object = gameObject.copy();
                object.transform.position = new Vector2D(x * this.width, y * this.height);
                Window.getWindow().getCurrentScene().addGameObject(object);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D){
        Sprite sprite = gameObject.getComponent(Sprite.class);
        if(sprite!= null){
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            graphics2D.setComposite(alphaComposite);
            graphics2D.drawImage(sprite.image, (int) gameObject.transform.position.x, (int) gameObject.transform.position.y, sprite.width, sprite.height, null);
            alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f);
            graphics2D.setComposite(alphaComposite);

        }
    }

    @Override
    public Component copy() {
        return null; // Copy not needed for this component
    }
}
