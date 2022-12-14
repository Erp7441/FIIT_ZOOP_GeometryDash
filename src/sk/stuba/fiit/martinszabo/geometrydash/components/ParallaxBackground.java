package sk.stuba.fiit.martinszabo.geometrydash.components;

import sk.stuba.fiit.martinszabo.geometrydash.engine.Component;
import sk.stuba.fiit.martinszabo.geometrydash.engine.GameObject;
import sk.stuba.fiit.martinszabo.geometrydash.engine.Window;
import sk.stuba.fiit.martinszabo.geometrydash.util.Constants;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class ParallaxBackground extends Component<ParallaxBackground>{

    private int width;
    private int height;
    private Sprite sprite;
    private ArrayList<GameObject> backgrounds;
    private double speed = 80.0;
    private Ground ground;
    private boolean followGround;
    private int timeStep;

    public ParallaxBackground(String file, ArrayList<GameObject> backgrounds, Ground ground, boolean followGround, boolean staticBackground){
        this.sprite = new Sprite(file);
        this.width = this.sprite.getWidth();
        this.height = this.sprite.getHeight();
        this.backgrounds = backgrounds;
        this.ground = ground;

        if(followGround) { this.speed  = Constants.PLAYER_SPEED - 35; }
        this.followGround = followGround;

        if(staticBackground) { this.speed = 0; }
    }

    @Override
    public void update(double deltaTime){

        if (backgrounds == null) { return; }

        this.timeStep++;

        this.getGameObject().setX(this.getGameObject().getX() - deltaTime * speed);
        this.getGameObject().setX(Math.floor(this.getGameObject().getX()));
        if(this.getGameObject().getX() < -width){
            double maxX = 0;
            int secondTimeStep = 0;
            for(GameObject gameObject : backgrounds){
                if(gameObject.getX() > maxX){
                    maxX = gameObject.getX();
                    secondTimeStep = gameObject.getComponent(ParallaxBackground.class).timeStep;
                }
            }

            if(secondTimeStep == this.timeStep){
                this.getGameObject().setX(maxX + width);
            }
            else{
                this.getGameObject().setX(Math.floor((maxX + width) - (deltaTime * speed)));
            }
        }

        if (followGround){
            this.getGameObject().setY(ground.getGameObject().getY());
        }
    }

    @Override
    public void draw(Graphics2D graphics2D){
        if (followGround){
            graphics2D.drawImage(
                this.sprite.getImage(),
                (int)this.getGameObject().getX(),
                (int)(this.getGameObject().getY() - Window.getCamera().getY()),
                width,
                height,
                null
            );
        }
        else{
            int gHeight = Math.min((int) (ground.getGameObject().getY() - Window.getCamera().getY()), Constants.SCREEN_HEIGHT);
            graphics2D.drawImage(
                    this.sprite.getImage(),
                    (int)this.getGameObject().getX(),
                    (int)(this.getGameObject().getY()),
                    width,
                    Constants.SCREEN_HEIGHT,
                    null
            );
            graphics2D.setColor(Constants.GROUND_COLOR);
            graphics2D.fillRect((int)(this.getGameObject().getX()), gHeight, width, Constants.SCREEN_HEIGHT);
        }
    }

    @Override
    public Component<ParallaxBackground> copy(){
        return null;
    }

    @Override
    public String serialize(int tabSize){
        return null;
    }

    public Sprite getSprite(){
        return sprite;
    }
}
