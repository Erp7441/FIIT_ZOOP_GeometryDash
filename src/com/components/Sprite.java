package com.components;


import com.engine.Component;

import com.util.AssetPool;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.File;

/**
 * Sprite game object component that can be used to load and draw assets in
 * the game world. When attached to a game object the draw method is drawing this
 * sprite object which will be representing the game object inside the game world.
 *
 * @see Component Component – An add-on to the game object.
 */
public class Sprite extends Component {
    public String file;
    public int width, height;
    public BufferedImage image;
    public boolean isSubsprite = false;
    public int row, column, index;

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
                throw new Exception("Asset already exists" + this.file);
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
    public Sprite(BufferedImage image){
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    /**
     * Constructor for single sprite from spritesheet.
     * @param image Sprite texture inside the game asset's folder.
     */
    public Sprite(BufferedImage image, int row, int column, int index){
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.row = row;
        this.column = column;
        this.index = index;
        this.isSubsprite = true;
    }

    /**
     * Draws the texture of this component on specified parent game object position.
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     */
    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(this.image, (int) gameObject.transform.position.x, (int) gameObject.transform.position.y, this.width, this.height, null);
    }

    /**
     * @return Copy of the current sprite with the same parameters, distinguishing whether it is a full sprite or a subsprite.
     */
    @Override
    public Component copy() {
        if(!isSubsprite){
            return new Sprite(this.image);
        }
        else{
            return new Sprite(this.image, this.row, this.column, this.index);
        }
    }
}
