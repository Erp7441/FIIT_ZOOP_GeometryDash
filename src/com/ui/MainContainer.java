package com.ui;

import com.components.Sprite;
import com.components.Spritesheet;
import com.engine.Component;
import com.engine.GameObject;
import com.util.Constants;
import com.util.Transform;
import com.util.Vector2D;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Container that holds all menu items in editor mode of the game.
 *
 * @see Component Component – An add-on to the game object.
 */
public class MainContainer extends Component {

    public List<GameObject> menuItems = null;

    /**
     * Constructor that inicializes empty ArrayList of GameObject
     * to be displayed in the container.
     */
    public MainContainer() {
        this.menuItems = new ArrayList<GameObject>();
        init();
    }

    /**
     * Initializes the container items with textures and formates them in a table layout.
     *
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see MenuItem MenuItem – Object placed within the bounds of the container.
     * @see Spritesheet Spritesheet - multiple pieces of 2D textures
     * @see Sprite Sprite – a piece of 2D texture.
     */
    public void init() {
        Spritesheet groundSprites = new Spritesheet("assets/groundSprites.png", 42, 42, 2, 6, 12);
        Spritesheet buttonSprites = new Spritesheet("assets/buttonSprites.png", 60, 60, 2, 2, 2);

        for (int i = 0; i < groundSprites.sprites.size(); i++){
            Sprite currentSprite = groundSprites.sprites.get(i);
            int x = Constants.BUTTON_OFFSET_X + (currentSprite.column * Constants.BUTTON_WIDTH) + (currentSprite.column * Constants.BUTTON_SPACING_HZ);
            int y = Constants.BUTTON_OFFSET_Y + (currentSprite.row * Constants.BUTTON_HEIGHT) + (currentSprite.row * Constants.BUTTON_SPACING_VT);

            GameObject obj = new GameObject("Generated", new Transform(new Vector2D(x, y)));
            obj.addComponent(currentSprite.copy());
            MenuItem menuItem = new MenuItem(x, y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, buttonSprites.sprites.get(0), buttonSprites.sprites.get(1));
            obj.addComponent(menuItem);
            menuItems.add(obj);
        }
    }

    /**
     * Creates all player components.
     *
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see Component Component – An add-on to the game object.
     */
    @Override
    public void start() {
        for(GameObject gameObject : this.menuItems){
            for(Component component : gameObject.getComponents()){
                component.start();
            }
        }
    }

    /**
     * Updates game objects values.
     *
     * @param deltaTime Diffrence between last mouse update time and current mouse update time.
     */
    @Override
    public void update(double deltaTime){
        for(GameObject gameObject : this.menuItems){
            gameObject.update(deltaTime);
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
     * Draws all items in this container on the scrren.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     */
    @Override
    public void draw(Graphics2D graphics2D) {
        for(GameObject gameObject : this.menuItems){
            gameObject.draw(graphics2D);
        }
    }
}
