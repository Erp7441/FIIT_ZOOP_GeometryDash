package com.components;

import com.engine.Component;
import com.util.Constants;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;


public class Player extends Component {

    Sprite layerOne = null;
    Sprite layerTwo = null;
    Sprite layerThree = null;
    public int width = 0, height = 0;

    public Player(Sprite layerOne, Sprite layerTwo, Sprite layerThree, Color colorOne, Color colorTwo){
        int threshold = 200;
        this.layerOne = layerOne;
        this.layerTwo = layerTwo;
        this.layerThree = layerThree;
        this.width = Constants.PLAYER_WIDTH;
        this.height = Constants.PLAYER_HEIGHT;

        for (int y = 0; y < layerOne.image.getWidth(); y++){
            for (int x = 0; x < layerOne.image.getHeight(); x++){
                Color color = new Color(layerOne.image.getRGB(x, y));
                if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold){
                    layerOne.image.setRGB(x, y, colorOne.getRGB());
                }
            }
        }

        for (int y = 0; y < layerTwo.image.getWidth(); y++){
            for (int x = 0; x < layerTwo.image.getHeight(); x++){
                Color color = new Color(layerTwo.image.getRGB(x, y));
                if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold){
                    layerTwo.image.setRGB(x, y, colorTwo.getRGB());
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D){
        AffineTransform transform = new AffineTransform();
        transform.setToIdentity();
        transform.translate(gameObject.transform.position.x, gameObject.transform.position.y);
        transform.rotate(gameObject.transform.rotation, (this.width * gameObject.transform.scale.x) / 2.0, (this.height * gameObject.transform.scale.y) / 2.0);
        transform.scale(gameObject.transform.scale.x, gameObject.transform.scale.y);

        graphics2D.drawImage(this.layerOne.image, transform, null);
        graphics2D.drawImage(this.layerTwo.image, transform, null);
        graphics2D.drawImage(this.layerThree.image, transform, null);
    }
}
