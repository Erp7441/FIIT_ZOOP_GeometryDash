package sk.stuba.fiit.martinszabo.geometrydash.engine;

import sk.stuba.fiit.martinszabo.geometrydash.components.*;

import sk.stuba.fiit.martinszabo.geometrydash.file.Parser;
import sk.stuba.fiit.martinszabo.geometrydash.ui.MainContainer;
import sk.stuba.fiit.martinszabo.geometrydash.util.AssetPool;
import sk.stuba.fiit.martinszabo.geometrydash.util.Constants;
import sk.stuba.fiit.martinszabo.geometrydash.util.Transform;
import sk.stuba.fiit.martinszabo.geometrydash.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Scene that displays the level editor. This scene can edit the LevelScene objects
 * which means that player can model their own level.
 */
public class LevelEditorScene extends Scene {

    private GameObject player = null;
    private Grid grid = null;
    private CameraControls cameraControls = null;
    private GameObject cursor = null;
    private MainContainer editingButtons = null;

    /**
     * Initializes the scene with name.
     * @param name The name of the object. Only used for debugging purposes.
     */
    public LevelEditorScene(String name) {
        super(name);

    }

    /**
     * Initializes the scene with the same way as LevelScene. Then it freezes the game and adds
     * grid, cursor and editor controls. So the player can place and remove objects from the level.
     * Grid so the game objects added can snap into place.
     *
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see Spritesheet Spritesheet - multiple pieces of 2D textures
     * @see Player Player - object that is controlled by the player.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see Color Color - object that represents the color of the player's textures.
     */
    @Override
    public void init() {
        initAssetPool();

        editingButtons = new MainContainer(); //! Composition
        grid = new Grid(); //! Composition
        cameraControls = new CameraControls(); //! Composition
        editingButtons.start();

        cursor = new GameObject("Mouse Cursor", new Transform(new Vector2D()), 10); //! Composition
        cursor.addComponent(new LevelEditorControls(Constants.TILE_WIDTH, Constants.TILE_HEIGHT));

        Spritesheet layerOne = AssetPool.getSpritesheet("assets/player/layerOne.png");
        Spritesheet layerTwo = AssetPool.getSpritesheet("assets/player/layerTwo.png");
        Spritesheet layerThree = AssetPool.getSpritesheet("assets/player/layerThree.png");

        player = new GameObject("Player", new Transform(new Vector2D(300.0,300.0)), 0); //! Composition
        Player playerComp = new Player(
                layerOne.getSprites().get(0),
                layerTwo.getSprites().get(0),
                layerThree.getSprites().get(0),
                Color.RED,
                Color.GREEN
        );
        player.addComponent(playerComp);
        addGameObject(player);
        player.setSerializable(false);

        initBackgrounds(5, true);

    }

    @Override
    public void initAssetPool(){
        super.initAssetPool();
        AssetPool.addSpritesheet("assets/ui/buttonSprites.png", 60, 60, 2, 2, 2);
        AssetPool.addSpritesheet("assets/ui/tabs.png", Constants.TAB_WIDTH, Constants.TAB_HEIGHT, 2, 6, 6);
        AssetPool.addSpritesheet("assets/spikes.png", Constants.TILE_WIDTH, Constants.TILE_HEIGHT, 2, 6, 4);
        AssetPool.addSpritesheet("assets/bigSprites.png", Constants.TILE_WIDTH*2, Constants.TILE_HEIGHT*2, 2, 2, 2);
        AssetPool.addSpritesheet("assets/smallBlocks.png", Constants.TILE_WIDTH, Constants.TILE_HEIGHT, 2, 6, 1);
        AssetPool.addSpritesheet("assets/portal.png", 44, 85, 2, 2, 2);

    }

    /**
     * Updates the all game objects in the scene, after that it updates camera,
     * grid, editing buttons and cursor.
     *
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     * @see GameObject GameObject – Base object from which everything is derived from.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     */
    @Override
    public void update(double deltaTime) {

        //TODO:: Tweak values
        if (getCamera().getPosition().getY() - getGround().getY() > Constants.CAMERA_OFFSET_GROUND_Y + 70.0){
            getCamera().getPosition().setY(Constants.CAMERA_OFFSET_GROUND_Y + 70.0);
        }

        for (GameObject gameObject : getGameObjects()){
            gameObject.update(deltaTime);
        }

        cameraControls.update(deltaTime);
        grid.update(deltaTime);
        editingButtons.update(deltaTime);
        cursor.update(deltaTime);

        if(Window.getKeyListener().isKeyPressed(KeyEvent.VK_F1)){
            exportLevel("Level");
        }
        else if(Window.getKeyListener().isKeyPressed(KeyEvent.VK_F2)){
            importLevel("Level");
        }
        else if(Window.getKeyListener().isKeyPressed(KeyEvent.VK_F3)){
            Window.getWindow().changeScene(1);
        }

        if(!getGameObjectsToRemove().isEmpty()){
            for (GameObject gameObject : getGameObjectsToRemove()){
                getGameObjects().remove(gameObject);
                getRenderer().getGameObjects().get(gameObject.getzIndex()).remove(gameObject);
            }
            getGameObjectsToRemove().clear();
        }
    }

    private boolean isCurrentLevel(String fileName){

        exportLevel(".current");

        Parser.openFile(".current");
        byte[] currentLevel = Parser.getBytes();

        Parser.openFile(fileName);
        byte[] newLevel = Parser.getBytes();

        return Arrays.equals(currentLevel, newLevel);
    }

    @Override
    protected void importLevel(String fileName){
        if(isCurrentLevel(fileName)){ return; }

        for (GameObject gameObject : getGameObjects()){
            if (gameObject.isSerializable()){
                getGameObjectsToRemove().add(gameObject);
            }
        }
        for (GameObject gameObject : getGameObjectsToRemove()){
            for(List<GameObject> renderedObjects : getRenderer().getGameObjects().values()){
                renderedObjects.remove(gameObject);
            }
            getGameObjects().remove(gameObject);
        }

        super.importLevel(fileName);
    }

    private void exportLevel(String fileName) {
        try{
            FileOutputStream fos = new FileOutputStream("levels/" + fileName + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.putNextEntry(new ZipEntry(fileName + ".json"));

            int count = 0;
            zos.write("{\n".getBytes()); //? Begin JSON file
            for (GameObject gameObject : this.getGameObjects()){
                String str = gameObject.serialize(1);
                if (str.compareTo("") != 0){
                    zos.write(str.getBytes());
                    if(count != getGameObjects().size()-1){
                        zos.write(",\n".getBytes());
                    }
                }
                count++;
            }
            zos.write("\n}".getBytes()); //? End JSON file

            zos.closeEntry();
            zos.close();
            fos.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Draws the background of the game. Afterwards it renders all game objects.
     * Then it draws the editing buttons and the cursor.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see Color Color - object that represents the color of the player's textures.
     */
    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Constants.BG_COLOR);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        getRenderer().render(graphics2D);
        grid.draw(graphics2D);
        editingButtons.draw(graphics2D);
        cursor.draw(graphics2D);
    }

    public GameObject getPlayer(){
        return player;
    }

    public void setPlayer(GameObject player){
        this.player = player;
    }

    public GameObject getCursor(){
        return cursor;
    }

    public void setCursor(GameObject cursor){
        this.cursor = cursor;
    }
}
