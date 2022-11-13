package com.components;

import com.util.AssetPool;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads whole spritesheet splitting it into a multiple textures and then adding those
 * textures to the asset pool.
 */
public class Spritesheet {
    private List<Sprite> sprites;
    private int tileWidth;
    private int tileHeight;
    private int spacing;

    /**
     *  Loads spritesheet splitting it into a multiple textures and then adding them to the asset pool
     *  in a loop until all the sprite have been successfully separated from the spritesheet.
     * <p>
     *  We loop through every column until we reach the final column. Then we move to the next row and start
     *  looping through the every column again. On each iteration we add the current tile to the assets pool,
     *  and we continue this process until all the sprites have been separated from the spritesheet and added
     *  to the assets pool.
     *
     * @param picture File path to a sprite texture file.
     * @param tileWidth Width of a single tile within the sprite sheet.
     * @param tileHeight Height of a single tile within the sprite sheet.
     * @param spacing Spacing between each tile of the sprite sheet.
     * @param columns Number of columns of the sprite sheet.
     * @param size Number of tiles in the sprite sheet.
     */
    public Spritesheet(String picture, int tileWidth, int tileHeight, int spacing , int columns, int size) {
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.spacing = spacing;

        Sprite parent = AssetPool.getSprite(picture);
        sprites = new ArrayList<>();
        int row = 0;
        int count = 0;
        while (count < size) {
            for (int column = 0; column < columns; column++) {
                int imageX = (column * tileWidth) + (column * spacing);
                int imageY = (row * tileHeight) + (row * spacing);
                sprites.add(new Sprite(parent.getImage().getSubimage(imageX, imageY, tileWidth, tileHeight), row, column, count)); //! Composition
                count++;
                if (count > size - 1){
                    break;
                }
            }
            row++;
        }

    }

    public List<Sprite> getSprites(){
        return sprites;
    }

    public void setSprites(List<Sprite> sprites){
        this.sprites = sprites;
    }

    public int getTileWidth(){
        return tileWidth;
    }

    public void setTileWidth(int tileWidth){
        this.tileWidth = tileWidth;
    }

    public int getTileHeight(){
        return tileHeight;
    }

    public void setTileHeight(int tileHeight){
        this.tileHeight = tileHeight;
    }

    public int getSpacing(){
        return spacing;
    }

    public void setSpacing(int spacing){
        this.spacing = spacing;
    }
}
