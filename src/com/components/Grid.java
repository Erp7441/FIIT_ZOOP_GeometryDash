package com.components;

import com.engine.Camera;
import com.engine.Component;
import com.engine.Window;

import com.util.Constants;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;

public class Grid extends Component {

    Camera camera;
    public int width, height;
    private int numYLines = 31;
    private int numXLines = 31;

    public Grid(){
        this.camera = Window.getWindow().getCurrentScene().camera;
        this.width = Constants.TILE_WIDTH;
        this.height = Constants.TILE_HEIGHT;
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(new Color(0.2f, 0.2f, 0.2f, 0.8f));

        double bottom = Math.min(Constants.GROUND_Y - camera.position.y, Constants.SCREEN_HEIGHT);
        double startX = Math.floor(this.camera.position.x / this.width) * this.width - this.camera.position.x;
        double startY = Math.floor(this.camera.position.y / this.height) * this.height - this.camera.position.y;

        for (int column = 0; column <= numYLines; column++){
            graphics2D.draw(new Line2D.Double(startX, 0, startX, bottom));
            startX += this.width;
        }

        for (int row = 0; row <= numXLines; row++){
            if(camera.position.y + startY < Constants.GROUND_Y){
                graphics2D.draw(new Line2D.Double(0, startY, Constants.SCREEN_WIDTH, startY));
                startY += this.height;
            }
        }
    }
}
