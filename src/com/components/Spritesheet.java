package com.components;

import com.util.AssetPool;
import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    public List<Sprite> sprites = null;
    public int tileWidth = 0;
    public int tileHeight = 0;
    public int spacing = 0;

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
                sprites.add(new Sprite(parent.image.getSubimage(imageX, imageY, tileWidth, tileHeight)));
                count++;
                if (count > size - 1){
                    break;
                }
            }
            row++;
        }

    }
}
