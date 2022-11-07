package com.ui;

import com.components.SnapToGrid;
import com.components.Sprite;
import com.engine.Component;
import com.engine.GameObject;
import com.engine.LevelEditorScene;
import com.engine.Window;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Object in editor menu. Represents placeable object in the game scene.
 * It can be placed in from editor scene.
 *
 * @see Component Component – An add-on to the game object.
 */
public class MenuItem extends Component {

    private int x, y, width, height;
    private Sprite buttonSprite, hoverSprite, imageAttached;
    public boolean isSelected;

    private int bufferX, bufferY;

    /**
     * Inicializes variables needed for positioning and drawing this object.
     *
     * @param x X coordinate of the item
     * @param y Y coordinate of the item
     * @param width width of the item texture
     * @param height height of the item texture
     * @param buttonSprite sprite used for the item
     * @param hoverSprite sprite used for the item while hovering over the item
     */
    public MenuItem(int x, int y, int width, int height, Sprite buttonSprite, Sprite hoverSprite){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonSprite = buttonSprite;
        this.hoverSprite = hoverSprite;
        this.isSelected = false;
    }

    /**
     * Inicializes texture of the object and place it in middle
     * within the bounds of the object.
     */
    @Override
    public void start(){
        imageAttached = gameObject.getComponent(Sprite.class);
        this.bufferX = (int)((this.width / 2.0) - (imageAttached.width/ 2.0));
        this.bufferY = (int)((this.height / 2.0) - (imageAttached.height / 2.0));
    }

    /**
     * Attaches the object to player's mouse cursor.
     *
     * @param deltaTime Diffrence between last mouse update time and current mouse update time.
     */
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

    /**
     * Function to copy this object. We do not want to copy this object.
     * So this method wil return null.
     *
     * @return reference to null object
     */
    @Override
    public Component copy() {
        return null;
    }

    /**
     * Draws the object on the screen.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     */
    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(this.buttonSprite.image, this.x, this.y, this.width, this.height, null);
        graphics2D.drawImage(this.imageAttached.image, this.x + bufferX, this.y + bufferY, imageAttached.width, imageAttached.height, null);
        if(isSelected){
            graphics2D.drawImage(hoverSprite.image, this.x, this.y, this.width, this.height, null);
        }
    }
}
