package com.util;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import com.components.Sprite;

/**
 * Assets pool class that holds all assets that are loaded in the game.
 * Takes care that all assets are loaded only once to prevent memory leaks.
 *
 * @see Sprite Sprite – a piece of 2D texture.
 */
public class AssetPool {
    private static Map<String, Sprite> sprites = new HashMap<>(); //! Composition

    private AssetPool() {
        // Private constructor used to hide public implicit constructor of this class.
    }

    /**
     * Checks if asset is already loaded in the assets pool.
     *
     * @param file Path to asset file.
     * @return Boolean true if asset is already loaded in the assets pool. Otherwise, it returns false.
     */
    public static boolean hasSprite(String file) {
        return AssetPool.sprites.containsKey(file);
    }

    /**
     * Gets loaded asset if present in asset pool. If not then
     * add it to the assets pool and return it.
     *
     * @param file Path to asset file.
     * @return Sprite object containing asset.
     * @see Sprite Sprite – a piece of 2D texture.
     */
    public static Sprite getSprite(String file) {
        File fileObject = new File(file); //! Composition
        if(!AssetPool.hasSprite(file)){
            Sprite sprite = new Sprite(file); //! Composition
            AssetPool.addSprite(file, sprite);
        }
        return AssetPool.sprites.get(fileObject.getAbsolutePath());
    }

    /**
     * Checks if the sprite exists in the asset pool. If it does
     * print out error message and exit the function to prevent
     * the sprite from being added to the asset pool twice.
     * If it does not find the sprite in the asset pool then it will
     * be added to the asset pool.
     *
     * @param file Path to asset file.
     * @param sprite Texture to be added to the asset pool.
     * @see Sprite Sprite – a piece of 2D texture.
     */
    public static void addSprite(String file, Sprite sprite) {
        File fileObject = new File(file); //! Composition
        if(!AssetPool.hasSprite(fileObject.getAbsolutePath())){
            AssetPool.sprites.put(fileObject.getAbsolutePath(), sprite);
        }
        else{
            System.err.println("Asset pool already has asset: " + fileObject.getAbsolutePath());
        }

    }
}
