package com.util;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import com.components.Sprite;

public class AssetPool {
    static Map<String, Sprite> sprites = new HashMap<>();

    public static boolean hasSprite(String file) {
        return AssetPool.sprites.containsKey(file);
    }

    public static Sprite getSprite(String file) {
        File fileObject = new File(file);
        if(AssetPool.hasSprite(file)){
            return AssetPool.sprites.get(fileObject.getAbsolutePath().toString());
        }
        else{
            Sprite sprite = new Sprite(file);
            AssetPool.addSprite(file, sprite);
            return AssetPool.sprites.get(fileObject.getAbsolutePath());
        }
    }

    /**
     *
     * @param file absolute path to the file
     * @param sprite
     */
    public static void addSprite(String file, Sprite sprite) {
        File fileObject = new File(file);
        if(!AssetPool.hasSprite(fileObject.getAbsolutePath())){
            AssetPool.sprites.put(fileObject.getAbsolutePath(), sprite);
        }
        else{
            System.err.println("Asset pool already has asset: " + fileObject.getAbsolutePath());
        }

    }
}
