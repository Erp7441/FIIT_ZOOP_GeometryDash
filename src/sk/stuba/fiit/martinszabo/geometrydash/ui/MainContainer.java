package sk.stuba.fiit.martinszabo.geometrydash.ui;

import sk.stuba.fiit.martinszabo.geometrydash.components.*;
import sk.stuba.fiit.martinszabo.geometrydash.engine.Component;
import sk.stuba.fiit.martinszabo.geometrydash.engine.GameObject;
import sk.stuba.fiit.martinszabo.geometrydash.engine.Window;
import sk.stuba.fiit.martinszabo.geometrydash.util.AssetPool;
import sk.stuba.fiit.martinszabo.geometrydash.util.Constants;
import sk.stuba.fiit.martinszabo.geometrydash.util.Transform;
import sk.stuba.fiit.martinszabo.geometrydash.util.Vector2D;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Container that holds all menu items in editor mode of the game.
 *
 * @see Component Component – An add-on to the game object.
 */
public class MainContainer extends Component<MainContainer> {

    private Sprite containerBackground;
    private ArrayList<GameObject> tabs;
    private HashMap<GameObject, List<GameObject>> tabMaps;
    private GameObject currentTab = null;
    private GameObject currentButton = null;

    /**
     * Constructor that initializes empty ArrayList of GameObject
     * to be displayed in the container.
     */
    public MainContainer() {
        this.tabs = new ArrayList<>(); //! Composition
        this.tabMaps = new HashMap<>(); //! Composition
        this.containerBackground = AssetPool.getSprite("assets/ui/menuContainerBackground.png");
        init();
    }

    /**
     * Initializes the container items with textures and formats them in a table layout.
     *
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see MenuItem MenuItem – Object placed within the bounds of the container.
     * @see Spritesheet Spritesheet - multiple pieces of 2D textures
     * @see Sprite Sprite – a piece of 2D texture.
     */
    public void init() {
        addTabs();
        addTabObjects();
    }

    private void addTabs(){
        Spritesheet tabSprites = AssetPool.getSpritesheet("assets/ui/tabs.png");

        for (int i = 0; i < tabSprites.getSprites().size(); i++){
            Sprite currentTabSprite = tabSprites.getSprites().get(i);

            int x = Constants.TAB_OFFSET_X + (currentTabSprite.getColumn() * Constants.TAB_WIDTH) + (currentTabSprite.getColumn() * Constants.TAB_HORIZONTAL_SPACING);
            int y = Constants.TAB_OFFSET_Y;

            GameObject tab = new GameObject("Tab", new Transform(new Vector2D(x,y)), 10, true, false);
            TabItem tabItem = new TabItem(x, y, Constants.TAB_WIDTH, Constants.TAB_HEIGHT,  currentTabSprite, this);
            tab.addComponent(tabItem);

            this.tabs.add(tab);
            this.tabMaps.put(tab, new ArrayList<>());
            Window.getScene().addGameObject(tab);
        }
        this.currentTab = this.tabs.get(0);
        this.currentTab.getComponent(TabItem.class).setSelected(true);
    }

    private void addTabObjects() {

        Spritesheet groundSprites = AssetPool.getSpritesheet("assets/groundSprites.png");
        Spritesheet smallBlocks = AssetPool.getSpritesheet("assets/smallBlocks.png");
        Spritesheet spikeSprites = AssetPool.getSpritesheet("assets/spikes.png");
        Spritesheet bigSprites = AssetPool.getSpritesheet("assets/bigSprites.png");
        Spritesheet portalSprites = AssetPool.getSpritesheet("assets/portal.png");

        addButtons(groundSprites, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, 0, BoundsType.BOX);
        addButtons(groundSprites, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, 2, BoundsType.BOX);
        addButtons(spikeSprites, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, 3, BoundsType.TRIANGLE);
        addButtons(bigSprites, Constants.TILE_WIDTH, 56, 4, BoundsType.BOX);

        ArrayList<Component<?>> portalComponents = new ArrayList<>();
        portalComponents.add(new Portal(PlayerState.FLYING));
        addButtons(portalSprites, 44, 85, 5, BoundsType.BOX, true, portalComponents);

        addButtons(smallBlocks, Constants.TILE_WIDTH, 16, 1, BoundsType.BOX);
        for (GameObject smallBlock : this.tabMaps.get(this.tabs.get(1))){
            smallBlock.getComponent(BoxBounds.class).setYBuffer(42 - 16);
        }

    }


    private void addButtons(Spritesheet sprites, int width, int height, int index, BoundsType type) {
        Spritesheet buttonSprites = AssetPool.getSpritesheet("assets/ui/buttonSprites.png");

        for (int i = 0; i < sprites.getSprites().size(); i++){
            Sprite currentSprite = sprites.getSprites().get(i);
            int x = Constants.BUTTON_OFFSET_X + (currentSprite.getColumn() * Constants.BUTTON_WIDTH) + (currentSprite.getColumn() * Constants.BUTTON_SPACING_HZ);
            int y = Constants.BUTTON_OFFSET_Y + (currentSprite.getRow() * Constants.BUTTON_HEIGHT) + (currentSprite.getRow() * Constants.BUTTON_SPACING_VT);

            // Adding game objects for first tab section in the editor container
            GameObject gameObject = new GameObject((index+1)+" TabObject", new Transform(new Vector2D(x, y)), 10, true, false);
            gameObject.addComponent(currentSprite.copy()); // TODO:: adding this to the game object
            MenuItem menuItem = new MenuItem(x, y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, buttonSprites.getSprites().get(0), buttonSprites.getSprites().get(1), this); //! Composition
            gameObject.addComponent(menuItem);
            if(type == BoundsType.BOX){
                gameObject.addComponent(new BoxBounds(width, height));
            }
            else if(type == BoundsType.TRIANGLE){
                gameObject.addComponent(new TriangleBounds(42, 42));
            }
            this.tabMaps.get(this.tabs.get(index)).add(gameObject); // Get tab and add gameObject to it
        }
    }

    private void addButtons(Spritesheet sprites, int width, int height, int index, BoundsType type, boolean isTrigger, ArrayList<Component<?>> additionalComponents) {
        Spritesheet buttonSprites = AssetPool.getSpritesheet("assets/ui/buttonSprites.png");

        for (int i = 0; i < sprites.getSprites().size(); i++){
            Sprite currentSprite = sprites.getSprites().get(i);
            int x = Constants.BUTTON_OFFSET_X + (currentSprite.getColumn() * Constants.BUTTON_WIDTH) + (currentSprite.getColumn() * Constants.BUTTON_SPACING_HZ);
            int y = Constants.BUTTON_OFFSET_Y + (currentSprite.getRow() * Constants.BUTTON_HEIGHT) + (currentSprite.getRow() * Constants.BUTTON_SPACING_VT);

            // Adding game objects for tab section in the editor container
            GameObject gameObject = new GameObject((index + 1)+" TabObject", new Transform(new Vector2D(x, y)), 10, true, false);
            gameObject.addComponent(currentSprite.copy());
            MenuItem menuItem = new MenuItem(x, y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, buttonSprites.getSprites().get(0), buttonSprites.getSprites().get(1), this); //! Composition
            gameObject.addComponent(menuItem);
            if(type == BoundsType.BOX){
                gameObject.addComponent(new BoxBounds(width, height, isTrigger));
            }
            else if(type == BoundsType.TRIANGLE){
                gameObject.addComponent(new TriangleBounds(42, 42));
            }

            if(additionalComponents != null){
                for (Component<?> component : additionalComponents){
                    if(component instanceof Portal){
                        if(i == 0){
                            gameObject.addComponent(new Portal(PlayerState.FLYING));
                        }
                        else{
                            gameObject.addComponent(new Portal(PlayerState.NORMAL));
                        }
                        continue;
                    }
                    gameObject.addComponent(component);
                }
            }

            this.tabMaps.get(this.tabs.get(index)).add(gameObject); // Get tab and add gameObject to it
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
        for(GameObject gameObject : tabs){
            for(GameObject currentTabObject : tabMaps.get(gameObject)){
                for(Component<?> component : currentTabObject.getComponents()){
                    component.start();
                }
            }
        }
    }

    /**
     * Updates game objects values.
     *
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     */
    @Override
    public void update(double deltaTime){
        for(GameObject gameObject : this.tabMaps.get(currentTab)){
            gameObject.update(deltaTime);

            MenuItem menuItem = gameObject.getComponent(MenuItem.class);
            if (gameObject != currentButton && menuItem.isSelected()){
                menuItem.setSelected(false);
            }
        }

        for (GameObject gameObject : this.tabs){
            TabItem tabItem = gameObject.getComponent(TabItem.class);
            if (gameObject != currentTab && tabItem.isSelected()){
                tabItem.setSelected(false);
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
    public Component<MainContainer> copy() {
        return null;
    }

    /**
     * Draws all items in this container on the screen.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     */
    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(this.containerBackground.getImage(), 0, Constants.CONTAINER_OFFSET_Y, this.containerBackground.getWidth(), this.containerBackground.getHeight(), null);

        for(GameObject gameObject : this.tabMaps.get(currentTab)){
            gameObject.draw(graphics2D, Sprite.class);
        }
    }

    @Override
    public String serialize(int tabSize) {
        return ""; // Serialize not needed for this component
    }

    public GameObject getCurrentTab(){
        return currentTab;
    }

    public void setCurrentTab(GameObject currentTab){
        this.currentTab = currentTab;
    }

    public GameObject getCurrentButton(){
        return currentButton;
    }

    public void setCurrentButton(GameObject currentButton){
        this.currentButton = currentButton;
    }
}
