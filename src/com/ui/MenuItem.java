package com.ui;

import com.components.SnapToGrid;
import com.components.Sprite;
import com.engine.Component;
import com.engine.GameObject;
import com.engine.LevelEditorScene;
import com.engine.Window;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class MenuItem extends Component {

    private int x, y, width, height;
    private Sprite buttonSprite, hoverSprite, imageAttached;
    public boolean isSelected;

    private int bufferX, bufferY;

    public MenuItem(int x, int y, int width, int height, Sprite buttonSprite, Sprite hoverSprite){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonSprite = buttonSprite;
        this.hoverSprite = hoverSprite;
        this.isSelected = false;
    }

    @Override
    public void start(){
        imageAttached = gameObject.getComponent(Sprite.class);
        this.bufferX = (int)((this.width / 2.0) - (imageAttached.width/ 2.0));
        this.bufferY = (int)((this.height / 2.0) - (imageAttached.height / 2.0));
    }

    @Override
    public void update(double deltaTime){
        if(
            !isSelected
            && Window.getWindow().mouseListener.x > this.x && Window.getWindow().mouseListener.x <= this.x + this.width
            && Window.getWindow().mouseListener.y > this.y && Window.getWindow().mouseListener.y <= this.y + this.height
        ){
            if(Window.getWindow().mouseListener.mousePressed && Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1){
                // Clicked inside the button
                GameObject obj = gameObject.copy();
                obj.removeComponent(MenuItem.class);
                LevelEditorScene scene = (LevelEditorScene) Window.getWindow().getCurrentScene();
                SnapToGrid snapToGrid = scene.cursor.getComponent(SnapToGrid.class);
                obj.addComponent(snapToGrid);
                scene.cursor = obj;
                this.isSelected = true;
            }
        }
    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(this.buttonSprite.image, this.x, this.y, this.width, this.height, null);
        graphics2D.drawImage(this.imageAttached.image, this.x + bufferX, this.y + bufferY, imageAttached.width, imageAttached.height, null);
        if(isSelected){
            graphics2D.drawImage(hoverSprite.image, this.x, this.y, this.width, this.height, null);
        }
    }
}
