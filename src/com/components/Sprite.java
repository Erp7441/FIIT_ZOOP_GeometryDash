package com.components;


import com.engine.Component;

import com.file.Parser;
import com.util.AssetPool;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;

/**
 * Sprite game object component that can be used to load and draw assets in
 * the game world. When attached to a game object the draw method is drawing this
 * sprite object which will be representing the game object inside the game world.
 *
 * @see Component Component – An add-on to the game object.
 */
public class Sprite extends Component {
    private String file;
    private int width;
    private int height;
    private BufferedImage image;
    private boolean isSubsprite = false;
    private int row;
    private int column;
    private int index;

    /**
     * Loads the sprite image from the specified file to assets pool. If the sprite image
     * already exists it throws an exception because we don't want to load the sprite image
     * many times as that would result in a performance penalty or even a memory leak.
     *
     * @param file File path to a sprite texture file.
     */
    public Sprite(String file) {
        this.file = file;

        try{

            if(AssetPool.hasSprite(this.file)){
                throw new FileAlreadyExistsException("Asset already exists" + this.file);
            }
            this.image = ImageIO.read(new File(file));
            this.width = image.getWidth();
            this.height = image.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Constructor for whole spritesheet asset.
     * @param image Sprite texture inside the game asset's folder.
     */
    public Sprite(BufferedImage image, String file){
        this.image = image; //! Aggregation
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.file = file;
    }

    /**
     * Constructor for single sprite from spritesheet.
     * @param image Sprite texture inside the game asset's folder.
     */
    public Sprite(BufferedImage image, String file, int row, int column, int index){
        this.image = image; //! Aggregation
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.row = row;
        this.column = column;
        this.index = index;
        this.isSubsprite = true;
        this.file = file;
    }

    /**
     * Draws the texture of this component on specified parent game object position.
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     */
    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(this.image, (int) getGameObject().getTransform().getPosition().getX(), (int) getGameObject().getTransform().getPosition().getY(), this.width, this.height, null);
    }

    /**
     * @return Copy of the current sprite with the same parameters, distinguishing whether it is a full sprite or a subsprite.
     */
    @Override
    public Component copy() {
        if(!isSubsprite){
            return new Sprite(this.image, this.file);
        }
        else{
            return new Sprite(this.image, this.file, this.row, this.column, this.index);
        }
    }

    public String getFile(){
        return file;
    }

    public void setFile(String file){
        this.file = file;
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public BufferedImage getImage(){
        return image;
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }

    public boolean isSubsprite(){
        return isSubsprite;
    }

    public void setSubsprite(boolean subsprite){
        isSubsprite = subsprite;
    }

    public int getRow(){
        return row;
    }

    public void setRow(int row){
        this.row = row;
    }

    public int getColumn(){
        return column;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("Sprite", tabSize));
        builder.append(addBooleanProperty("isSubsprite", isSubsprite, tabSize + 1, true, true));
        builder.append(addStringProperty("FilePath", file, tabSize + 1, true, isSubsprite));

        if(isSubsprite){
            builder.append(addIntProperty("row", row, tabSize + 1, true, true));
            builder.append(addIntProperty("column", column, tabSize + 1, true, true));
            builder.append(addIntProperty("index", index, tabSize + 1, true, false));
        }

        builder.append(endObjectProperty(tabSize));
        return builder.toString();

    }

    public static Component deserialize(){
        boolean isSubsprite = Parser.consumeBooleanProperty("isSubsprite");
        Parser.consume(',');
        String filePath = Parser.consumeStringProperty("FilePath");

        if(isSubsprite){
            Parser.consume(',');
            Parser.consumeIntProperty("row");
            Parser.consume(',');
            Parser.consumeIntProperty("column");
            Parser.consume(',');
            int index = Parser.consumeIntProperty("index");

            if(!AssetPool.hasSpritesheet(filePath)){
                System.out.println("Spritesheet '" + filePath + "' not found!");
                System.exit(-1);
            }
            Parser.consumeEndObjectProperty();
            return AssetPool.getSpritesheet(filePath).getSprites().get(index).copy();
        }

        if(!AssetPool.hasSpritesheet(filePath)){
            System.out.println("Spritesheet '" + filePath + "' not found!");
            System.exit(-1);
        }

        Parser.consumeEndObjectProperty();
        return AssetPool.getSprite(filePath).copy();
    }
}
