package com.components;

import com.engine.Component;

import com.util.Constants;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;

/**
 * Player game object component that makes it controlable by the player.
 * It is used to generate square texture style, simulating physics and
 * drawing the square to the screen.
 *
 * @see Component Component – An add-on to the game object.
 */
public class Player extends Component {

    Sprite layerOne = null;
    Sprite layerTwo = null;
    Sprite layerThree = null;
    public int width = 0, height = 0;

    /**
     * Player constructor gets three texture layers and two colors for layer one and
     * for layer two. Iterating through x and y pixel coordinates of both layer one
     * and two he will get color at current pixel and if it is within the threshold
     * it will change the pixel color to the one provided in parameters. This means
     * that we can have any combinations of two colors on the player.
     *
     * @param layerOne First layer of texture for the player.
     * @param layerTwo Second layer of texture for the player.
     * @param layerThree Third layer of texture for the player.
     * @param colorOne First color for the player texture.
     * @param colorTwo Second color for the player texture.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see Sprite Sprite – a piece of 2D texture.
     * @see Color Color - object that represents the color of the player's textures.'
     */
    public Player(Sprite layerOne, Sprite layerTwo, Sprite layerThree, Color colorOne, Color colorTwo){
        int threshold = 200; // Color threshold for chaning the texture color values
        this.layerOne = layerOne;
        this.layerTwo = layerTwo;
        this.layerThree = layerThree;
        this.width = Constants.PLAYER_WIDTH;
        this.height = Constants.PLAYER_HEIGHT;

        // Layer one color placement
        for (int y = 0; y < layerOne.image.getWidth(); y++){
            for (int x = 0; x < layerOne.image.getHeight(); x++){
                Color color = new Color(layerOne.image.getRGB(x, y));
                if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold){
                    layerOne.image.setRGB(x, y, colorOne.getRGB());
                }
            }
        }

        // Layer two color placement
        for (int y = 0; y < layerTwo.image.getWidth(); y++){
            for (int x = 0; x < layerTwo.image.getHeight(); x++){
                Color color = new Color(layerTwo.image.getRGB(x, y));
                if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold){
                    layerTwo.image.setRGB(x, y, colorTwo.getRGB());
                }
            }
        }
    }

    /**
     * This function draws the player in such way that it first translates the player to new coordinate system
     * at [0,0] coordinates then it applies all the rotation and scaling, and finally it translates the whole
     * coordinate system back to a point specified in the original coordinate system. Translations are way faster
     * than calculating the slopes of every bounding line on the object.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     */
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

    @Override
    public Component copy() {
        return null; // Copy not needed for this component
    }
}
