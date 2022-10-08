package com.components;


import com.engine.Component;
import com.util.AssetPool;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Sprite extends Component {
    public String file;
    public int width, height;
    public BufferedImage image;

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

    public Sprite(BufferedImage image){
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(this.image, (int) gameObject.transform.position.x, (int) gameObject.transform.position.y, this.width, this.height, null);
    }
}
