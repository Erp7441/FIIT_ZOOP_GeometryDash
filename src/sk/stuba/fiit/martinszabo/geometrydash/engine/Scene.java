package sk.stuba.fiit.martinszabo.geometrydash.engine;

import sk.stuba.fiit.martinszabo.geometrydash.components.Ground;
import sk.stuba.fiit.martinszabo.geometrydash.components.ParallaxBackground;
import sk.stuba.fiit.martinszabo.geometrydash.file.Parser;
import sk.stuba.fiit.martinszabo.geometrydash.util.AssetPool;
import sk.stuba.fiit.martinszabo.geometrydash.util.Constants;
import sk.stuba.fiit.martinszabo.geometrydash.util.Transform;
import sk.stuba.fiit.martinszabo.geometrydash.util.Vector2D;

import java.awt.Graphics2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for creating scene. Contains all the necessary parts for creating
 * a scene. It manages adding game objects to the game.
 *
 * @see List List - Ordered collection of objects.
 * @see Camera Camera - Player's point of view.
 * @see GameObject GameObject – Base object from which everything is derived from.
 */
public abstract class Scene{
    private String name;
    private Camera camera;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> gameObjectsToRemove;
    private Renderer renderer;
    private GameObject ground;

    /**
     * Initializes a new scene. Creates a new camera and renderer.
     *
     * @param name Name of the scene for debugging purposes
     * @see List List - Ordered collection of objects.
     * @see Camera Camera - Player's point of view.
     * @see GameObject GameObject – Base object from which everything is derived from.
     */
    protected Scene(String name) {
        this.name = name;
        this.camera = new Camera(new Vector2D()); //! Composition
        this.gameObjects = new ArrayList<>(); //! Composition
        this.gameObjectsToRemove = new ArrayList<>(); //! Composition
        this.renderer = new Renderer(this.camera); //! Composition
    }

    /**
     * Adds game object to the game object list of this scene.
     *
     * @param gameObject - Game object to be added to game object list
     * @see GameObject GameObject – Base object from which everything is derived from.
     */
    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        renderer.submit(gameObject);
        for (Component<?> component : gameObject.getComponents()){
            component.start();
        }
    }

    public abstract void init();
    public abstract void update(double deltaTime);
    public abstract void draw(Graphics2D graphics2D);

    public void initAssetPool(){
        AssetPool.addSpritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/groundSprites.png", 42, 42, 2, 6, 12);
    }
    protected void importLevel(String fileName){

        // TODO:: Clear level before importing

        Parser.openFile(fileName);

        Parser.consume('{');
        GameObject gameObject = Parser.parseGameObject();
        while(gameObject != null){
            addGameObject(gameObject);
            gameObject = Parser.parseGameObject();
        }
        Parser.consume('}');
    }

    protected void initBackgrounds(int numberOfBackgrounds, boolean staticBackground){
        GameObject ground = new GameObject("Ground", new Transform(new Vector2D(0, Constants.GROUND_Y)), 1, true, false);
        ground.addComponent(new Ground());
        addGameObject(ground);

        ArrayList<GameObject> backgrounds = new ArrayList<>(numberOfBackgrounds);
        ArrayList<GameObject> groundBackgrounds = new ArrayList<>(numberOfBackgrounds);

        for (int i = 0; i < numberOfBackgrounds; i++){

            ParallaxBackground background = new ParallaxBackground("assets/backgrounds/bg01.png", backgrounds, ground.getComponent(Ground.class), false, staticBackground);
            int x = i * background.getSprite().getWidth();
            int y = 0;

            GameObject backgroundGameObject = new GameObject("Background", new Transform(new Vector2D(x, y)), -10, true, false);
            backgroundGameObject.addComponent(background);
            backgrounds.add(backgroundGameObject);

            ParallaxBackground groundBackground = new ParallaxBackground("assets/grounds/ground01.png", groundBackgrounds, ground.getComponent(Ground.class), true, staticBackground);
            x = i * groundBackground.getSprite().getWidth();
            y = (int)ground.getY();

            GameObject groundGameObject = new GameObject("GroundBackground", new Transform(new Vector2D(x, y)), -9, true, false);
            groundGameObject.addComponent(groundBackground);
            groundBackgrounds.add(groundGameObject);

            addGameObject(backgroundGameObject);
            addGameObject(groundGameObject);
        }
        this.ground = ground; // Keeping reference to the ground because of LevelEditorScene
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Camera getCamera(){
        return camera;
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    public ArrayList<GameObject> getGameObjects(){
        return gameObjects;
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects){
        this.gameObjects = gameObjects;
    }

    public Renderer getRenderer(){
        return renderer;
    }

    public void setRenderer(Renderer renderer){
        this.renderer = renderer;
    }

    public GameObject getGround(){
        return ground;
    }

    public void removeGameObject(GameObject gameObject){
        gameObjectsToRemove.add(gameObject);
    }

    public ArrayList<GameObject> getGameObjectsToRemove(){
        return gameObjectsToRemove;
    }
}
