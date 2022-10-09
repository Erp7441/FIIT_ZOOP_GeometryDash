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

public class MainContainer extends Component {

    public List<GameObject> menuItems = null;

    public MainContainer() {
        this.menuItems = new ArrayList<GameObject>();
        init();
    }

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

    @Override
    public void start() {
        for(GameObject gameObject : this.menuItems){
            for(Component component : gameObject.getComponents()){
                component.start();
            }
        }
    }

    @Override
    public void update(double deltaTime){
        for(GameObject gameObject : this.menuItems){
            gameObject.update(deltaTime);
        }
    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        for(GameObject gameObject : this.menuItems){
            gameObject.draw(graphics2D);
        }
    }
}
