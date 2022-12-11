package com.engine;

import com.components.*;

import com.file.Parser;
import com.ui.MainContainer;
import com.util.AssetPool;
import com.util.Constants;
import com.util.Transform;
import com.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Scene that displays the level editor. This scene can edit the LevelScene objects
 * which means that player can model their own level.
 */
public class LevelEditorScene extends Scene {

    private GameObject player = null;
    private GameObject ground = null;
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
        cursor.addComponent(new SnapToGrid(Constants.TILE_WIDTH, Constants.TILE_HEIGHT));

        Spritesheet layerOne = AssetPool.getSpritesheet("assets/player/layerOne.png");
        Spritesheet layerTwo = AssetPool.getSpritesheet("assets/player/layerTwo.png");
        Spritesheet layerThree = AssetPool.getSpritesheet("assets/player/layerThree.png");

        player = new GameObject("Some game object", new Transform(new Vector2D(300.0,300.0)), 0); //! Composition
        Player playerComp = new Player(
                layerOne.getSprites().get(0),
                layerTwo.getSprites().get(0),
                layerThree.getSprites().get(0),
                Color.RED,
                Color.GREEN
        );
        player.addComponent(playerComp);
        addGameObject(player);

        ground = new GameObject("Ground", new Transform(new Vector2D(0, Constants.GROUND_Y)), 1); //! Composition
        ground.addComponent(new Ground());
        addGameObject(ground);

        ground.setSerializable(false);
        player.setSerializable(false);

    }

    public void initAssetPool(){
        AssetPool.addSpritesheet("assets/player/layerOne.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerTwo.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerThree.png", 42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/groundSprites.png", 42, 42, 2, 6, 12);
        AssetPool.addSpritesheet("assets/buttonSprites.png", 60, 60, 2, 2, 2);
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

        if (getCamera().getPosition().getY() - ground.getY() > Constants.CAMERA_OFFSET_GROUND_Y){
            getCamera().getPosition().setY(Constants.CAMERA_OFFSET_GROUND_Y);
        }

        for (GameObject gameObject : getGameObjects()){
            gameObject.update(deltaTime);
        }

        cameraControls.update(deltaTime);
        grid.update(deltaTime);
        editingButtons.update(deltaTime);
        cursor.update(deltaTime);

        if(Window.getWindow().getKeyListener().isKeyPressed(KeyEvent.VK_F1)){
            exportLevel("Level");
        }
        else if(Window.getWindow().getKeyListener().isKeyPressed(KeyEvent.VK_F2)){
            importLevel("Level");
        }
        else if(Window.getWindow().getKeyListener().isKeyPressed(KeyEvent.VK_F3)){
            Window.getWindow().changeScene(1);
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
        graphics2D.setColor(Color.DARK_GRAY);
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
