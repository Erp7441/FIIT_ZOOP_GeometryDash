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

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Sprite buttonSprite;
    private final Sprite hoverSprite;
    private Sprite imageAttached;
    private boolean isSelected;
    private int bufferX;
    private int bufferY;
    private MainContainer parent;

    /**
     * Initializes variables needed for positioning and drawing this object.
     *
     * @param x X coordinate of the item
     * @param y Y coordinate of the item
     * @param width width of the item texture
     * @param height height of the item texture
     * @param buttonSprite sprite used for the item
     * @param hoverSprite sprite used for the item while hovering over the item
     */
    public MenuItem(int x, int y, int width, int height, Sprite buttonSprite, Sprite hoverSprite, MainContainer parent){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonSprite = buttonSprite; //! Aggregation
        this.hoverSprite = hoverSprite; //! Aggregation
        this.isSelected = false;
        this.parent = parent;
    }

    /**
     * Initializes texture of the object and place it in middle
     * within the bounds of the object.
     */
    @Override
    public void start(){
        imageAttached = getGameObject().getComponent(Sprite.class);
        this.bufferX = (int)((this.width / 2.0) - (imageAttached.getWidth()/ 2.0));
        this.bufferY = (int)((this.height / 2.0) - (imageAttached.getHeight() / 2.0));
    }

    /**
     * Attaches the object to player's mouse cursor.
     *
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     */
    @Override
    public void update(double deltaTime){
        if(
            !isSelected
            && Window.getWindow().getMouseListener().isMousePressed() && Window.getWindow().getMouseListener().getMouseButton() == MouseEvent.BUTTON1
            && Window.getWindow().getMouseListener().getX() > this.x && Window.getWindow().getMouseListener().getX() <= this.x + this.width
            && Window.getWindow().getMouseListener().getY() > this.y && Window.getWindow().getMouseListener().getY() <= this.y + this.height
        ){
            // Clicked inside the button
            GameObject obj = getGameObject().copy();
            obj.removeComponent(MenuItem.class);
            LevelEditorScene scene = (LevelEditorScene) Window.getWindow().getCurrentScene();
            SnapToGrid snapToGrid = scene.getCursor().getComponent(SnapToGrid.class);
            obj.addComponent(snapToGrid);
            scene.setCursor(obj);
            this.isSelected = true;
            this.parent.setCurrentButton(getGameObject());
        }
    }

    /**
     * Function to copy this object. We do not want to copy this object.
     * So this method wil return null.
     *
     * @return reference to null object
     */
    @Override
    public MenuItem copy() {
        return new MenuItem(this.x, this.y, this.width, this.height, (Sprite)this.buttonSprite.copy(), (Sprite)this.hoverSprite.copy(), this.parent);
    }

    /**
     * Draws the object on the screen.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     */
    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(this.buttonSprite.getImage(), this.x, this.y, this.width, this.height, null);
        graphics2D.drawImage(this.imageAttached.getImage(), this.x + bufferX, this.y + bufferY, imageAttached.getWidth(), imageAttached.getHeight(), null);
        if(isSelected){
            graphics2D.drawImage(hoverSprite.getImage(), this.x, this.y, this.width, this.height, null);
        }
    }

    @Override
    public String serialize(int tabSize) {
        return ""; // Serialize not needed for this component
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Sprite getButtonSprite(){
        return buttonSprite;
    }

    public Sprite getHoverSprite(){
        return hoverSprite;
    }

    public Sprite getImageAttached(){
        return imageAttached;
    }

    public void setImageAttached(Sprite imageAttached){
        this.imageAttached = imageAttached;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected){
        isSelected = selected;
    }

    public int getBufferX(){
        return bufferX;
    }

    public void setBufferX(int bufferX){
        this.bufferX = bufferX;
    }

    public int getBufferY(){
        return bufferY;
    }

    public void setBufferY(int bufferY){
        this.bufferY = bufferY;
    }

    public MainContainer getParent(){
        return parent;
    }

    public void setParent(MainContainer parent){
        this.parent = parent;
    }
}
