package com.components;

import com.engine.Component;

import com.engine.Window;
import com.util.AssetPool;
import com.util.Constants;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;

/**
 * Player game object component that makes it controllable by the player.
 * It is used to generate square texture style, simulating physics and
 * drawing the square to the screen.
 *
 * @see Component Component – An add-on to the game object.
 */
public class Player extends Component {

    private Sprite layerOne;
    private Sprite layerTwo;
    private Sprite layerThree;
    private Sprite spaceship;
    private int width;
    private int height;
    private boolean onGround = true;
    private PlayerState state;

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
     * @see Color Color - object that represents the color of the player's textures.
     */
    public Player(Sprite layerOne, Sprite layerTwo, Sprite layerThree, Color colorOne, Color colorTwo) {

        this.spaceship = AssetPool.getSprite("assets/player/spaceship.png");

        int threshold = 200; // Color threshold for changing the texture color values
        this.layerOne = layerOne; //! Aggregation
        this.layerTwo = layerTwo; //! Aggregation
        this.layerThree = layerThree; //! Aggregation
        this.width = Constants.PLAYER_WIDTH;
        this.height = Constants.PLAYER_HEIGHT;
        this.state = PlayerState.NORMAL;

        // Layer one color placement
        for (int y = 0; y < layerOne.getImage().getWidth(); y++){
            for (int x = 0; x < layerOne.getImage().getHeight(); x++){
                Color color = new Color(layerOne.getImage().getRGB(x, y));
                if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold){
                    layerOne.getImage().setRGB(x, y, colorOne.getRGB());
                }
            }
        }

        // Layer two color placement
        for (int y = 0; y < layerTwo.getImage().getWidth(); y++){
            for (int x = 0; x < layerTwo.getImage().getHeight(); x++){
                Color color = new Color(layerTwo.getImage().getRGB(x, y));
                if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold){
                    layerTwo.getImage().setRGB(x, y, colorTwo.getRGB());
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
        transform.translate(getGameObject().getX(), getGameObject().getY());
        transform.rotate(getGameObject().getTransform().getRotation(), (this.width * getGameObject().getTransform().getScale().getX()) / 2.0, (this.height * getGameObject().getTransform().getScale().getY()) / 2.0);
        transform.scale(getGameObject().getTransform().getScale().getX(), getGameObject().getTransform().getScale().getY());

        if(state == PlayerState.NORMAL){
            graphics2D.drawImage(this.layerOne.getImage(), transform, null);
            graphics2D.drawImage(this.layerTwo.getImage(), transform, null);
            graphics2D.drawImage(this.layerThree.getImage(), transform, null);
        }
        else if(state == PlayerState.FLYING){

            // Draw smaller player
            transform.setToIdentity();
            transform.translate(getGameObject().getX(), getGameObject().getY());
            transform.rotate(getGameObject().getTransform().getRotation(), (this.width * getGameObject().getTransform().getScale().getX()) / 4.0, (this.height * getGameObject().getTransform().getScale().getY()) / 4.0);
            transform.scale(getGameObject().getTransform().getScale().getX() / 1.5, getGameObject().getTransform().getScale().getY() / 1.5);
            transform.translate(15, 15);

            graphics2D.drawImage(this.layerOne.getImage(), transform, null);
            graphics2D.drawImage(this.layerTwo.getImage(), transform, null);
            graphics2D.drawImage(this.layerThree.getImage(), transform, null);

            // Then draw spaceship
            transform.setToIdentity();
            transform.translate(getGameObject().getX(), getGameObject().getY());
            transform.rotate(getGameObject().getTransform().getRotation(), (this.width * getGameObject().getTransform().getScale().getX()) / 2.0, (this.height * getGameObject().getTransform().getScale().getY()) / 2.0);
            transform.scale(getGameObject().getTransform().getScale().getX(), getGameObject().getTransform().getScale().getY());

            graphics2D.drawImage(spaceship.getImage(), transform, null);
        }
    }

    @Override
    public void update(double deltaTime){
        if(onGround && Window.getWindow().getKeyListener().isKeyPressed(KeyEvent.VK_SPACE)){
            if(state == PlayerState.NORMAL){
                addJumpForce();
            }
            this.onGround = false;
        }

        if(state == PlayerState.FLYING && Window.getWindow().getKeyListener().isKeyPressed(KeyEvent.VK_SPACE)){
            addFlyForce();
            onGround = false;
        }

        if(this.state != PlayerState.FLYING && !onGround){
            getGameObject().getTransform().setRotation(getGameObject().getTransform().getRotation() + 10.0 * deltaTime);
        }
        else if (this.state != PlayerState.FLYING){
            getGameObject().getTransform().setRotation(getGameObject().getTransform().getRotation() % 360);
            if(getGameObject().getTransform().getRotation() > 180 && getGameObject().getTransform().getRotation() < 360){
                getGameObject().getTransform().setRotation(0.0);
            }
            else if(getGameObject().getTransform().getRotation() > 0 && getGameObject().getTransform().getRotation() < 180){
                getGameObject().getTransform().setRotation(0.0);
            }
        }
    }

    private void addFlyForce(){
        getGameObject().getComponent(RigidBody.class).getVelocity().setY(Constants.JUMP_FORCE);
    }

    private void addJumpForce(){
        getGameObject().getComponent(RigidBody.class).getVelocity().setY(Constants.JUMP_FORCE);
    }

    public void die(){
        getGameObject().setX(500);
        getGameObject().setY(350);
        getGameObject().getComponent(RigidBody.class).getVelocity().setY(0);
        getGameObject().getTransform().setRotation(0);
        Window.getCamera().setX(0);
    }

    @Override
    public Component copy() {
        return null; // Copy not needed for this component
    }

    @Override
    public String serialize(int tabSize) {
        return ""; // Serialize not needed for this component
    }

    public Sprite getLayerOne(){
        return layerOne;
    }

    public void setLayerOne(Sprite layerOne){
        this.layerOne = layerOne;
    }

    public Sprite getLayerTwo(){
        return layerTwo;
    }

    public void setLayerTwo(Sprite layerTwo){
        this.layerTwo = layerTwo;
    }

    public Sprite getLayerThree(){
        return layerThree;
    }

    public void setLayerThree(Sprite layerThree){
        this.layerThree = layerThree;
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

    public boolean isOnGround(){
        return onGround;
    }

    public void setOnGround(boolean onGround){
        this.onGround = onGround;
    }

    public PlayerState getState(){
        return state;
    }

    public void setState(PlayerState state){
        this.state = state;
    }
}
