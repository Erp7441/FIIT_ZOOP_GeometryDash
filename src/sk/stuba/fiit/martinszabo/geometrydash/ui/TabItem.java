package sk.stuba.fiit.martinszabo.geometrydash.ui;

import sk.stuba.fiit.martinszabo.geometrydash.components.Sprite;
import sk.stuba.fiit.martinszabo.geometrydash.engine.Component;
import sk.stuba.fiit.martinszabo.geometrydash.engine.Window;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class TabItem extends Component<TabItem>{

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Sprite sprite;

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
     * @param sprite sprite used for the item
     */
    public TabItem(int x, int y, int width, int height, Sprite sprite, MainContainer parent){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.isSelected = false;
        this.parent = parent;
    }

    @Override
    public void update(double deltaTime){
        if(
                !isSelected
                        && Window.getMouseListener().isMousePressed() && Window.getMouseListener().getMouseButton() == MouseEvent.BUTTON1
                        && Window.getMouseListener().getX() > this.x && Window.getMouseListener().getX() <= this.x + this.width
                        && Window.getMouseListener().getY() > this.y && Window.getMouseListener().getY() <= this.y + this.height
        ){
            // Clicked inside the button
            this.isSelected = true;
            this.parent.setCurrentTab(getGameObject());
        }
    }

    @Override
    public void draw(Graphics2D graphics2D){
        if(isSelected){
            graphics2D.drawImage(sprite.getImage(), x, y, width, height, null);
        }
        else{
            float alpha = 0.5f;
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            graphics2D.setComposite(alphaComposite);

            graphics2D.drawImage(sprite.getImage(), x, y, width, height, null);

            alpha = 1.f;
            alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            graphics2D.setComposite(alphaComposite);
        }
    }

    @Override
    public Component<TabItem> copy(){
        return null; // Copy not needed for this class
    }

    @Override
    public String serialize(int tabSize){
        return null; // Serialize not needed for this class
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

    public Sprite getSprite(){
        return sprite;
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
