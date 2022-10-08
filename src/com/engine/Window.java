package com.engine;
import com.util.Constants;
import com.util.Time;

import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Window extends JFrame implements Runnable {

    public MouseListener mouseListener = null;
    public KeyListener keyListener = null;
    public boolean isInEditor = true;

    private static Window window = null;
    private boolean isRunning = true;
    private Scene currentScene = null;
    private Image doubleBufferImage = null;
    private Graphics doubleBufferGraphics = null;

    public Window(){
        this.mouseListener = new MouseListener();
        this.keyListener = new KeyListener();
        
        this.setSize(Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT);
        this.setTitle(Constants.SCREEN_TITLE);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(keyListener);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.setLocationRelativeTo(null);
    }

    public void init(){
        changeScene(1);
    }

    public Scene getCurrentScene(){
        return currentScene;
    }

    public void changeScene(int scene){
        switch (scene){
            case 0:
                isInEditor = true;
                this.currentScene = new LevelEditorScene("Level Editor");
                break;
            case 1:
                isInEditor = false;
                currentScene = new LevelScene("Level");
                break;
            default:
                System.err.println("Invalid scene");
                currentScene = null;
        }
    }

    public static Window getWindow(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void update(double deltaTime){
        currentScene.update(deltaTime);
        draw(getGraphics());
    }

    public void draw(Graphics graphics){
        if(doubleBufferImage == null){
            doubleBufferImage = createImage(getWidth(), getHeight());
            doubleBufferGraphics = doubleBufferImage.getGraphics();
        }

        renderOffscreen(doubleBufferGraphics);
        graphics.drawImage(doubleBufferImage, 0, 0, getWidth(), getHeight(), null);
    }

    public void renderOffscreen(Graphics graphics){
        Graphics2D graphics2D = (Graphics2D)graphics;
        currentScene.draw(graphics2D);
    }

    @Override
    public void run(){
        double lastFrameTime = 0.0;
        try{
            // Game loop;
            while(isRunning){
                double time = Time.getTime();
                double deltaTime = time - lastFrameTime;
                lastFrameTime = time;

                update(deltaTime);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
