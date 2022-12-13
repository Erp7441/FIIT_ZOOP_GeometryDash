package sk.stuba.fiit.martinszabo.geometrydash.engine;

import sk.stuba.fiit.martinszabo.geometrydash.util.Constants;
import sk.stuba.fiit.martinszabo.geometrydash.util.Time;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * This is the main window which displays the game. It manages flow of the game,
 * user input, generating frames, switching scene, etc.
 *
 * @see JFrame JFrame - Main window where content can be displayed.
 * @see Runnable Runnable - Interface that enables class to be run by a thread.
 * @see MouseListener MouseListener - Responsible for handling player mouse movement.
 * @see KeyListener KeyListener - Responsible for handling key events from the player.
 * @see Scene Scene - Object where the game happends
 * @see Image Image - Object that can hold image of texture.
 * @see Graphics Graphics - Handler for graphics operations within a window.
 */
public class Window extends JFrame implements Runnable {

    private MouseListener mouseListener;
    private KeyListener keyListener;
    private boolean isInEditor = true;

    private static Window window = null;
    private boolean isRunning = true;
    private Scene currentScene = null;

    /**
     * Doing drawing using double buffering prevents the texture from
     * being half loaded when drawing to the screen. It is done by loading
     * the texture first, then drawing them to object (doubleBufferGraphics) and
     * finally drawing the whole object on to the screen.
     */
    private Image doubleBufferImage = null;
    private Graphics doubleBufferGraphics = null;

    /**
     * Initializes the window with all parameters needed for creation (size, visibility, etc...).
     *
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see MouseListener MouseListener - Responsible for handling player mouse movement.
     * @see KeyListener KeyListener - Responsible for handling key events from the player.
     */
    private Window(){
        this.mouseListener = new MouseListener(); //! Composition
        this.keyListener = new KeyListener(); //! Composition
        
        this.setSize(Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT);
        this.setTitle(Constants.SCREEN_TITLE);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addKeyListener(keyListener);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.setLocationRelativeTo(null);
    }

    public static Camera getCamera(){
        return getScene().getCamera();
    }

    /**
     * Initializes the default scene for window.
     */
    public final void init(){
        changeScene(2);
    }

    /**
     * Returns current scene object.
     *
     * @return current scene.
     */
    public Scene getCurrentScene(){
        return currentScene;
    }

    /**
     * Switches the current scene based on scene identifier.
     *
     * @param scene scene identifier
     */
    public final void changeScene(int scene){
        switch (scene){
            case 0:
                isInEditor = true;
                this.currentScene = new LevelEditorScene("Level Editor"); //! Composition
                this.currentScene.init();
                break;
            case 1:
                isInEditor = false;
                this.currentScene = new LevelScene("Level"); //! Composition
                this.currentScene.init();
                break;
            case 2:
                isInEditor = false;
                this.currentScene = new MenuScene("Menu");
                this.currentScene.init();
                break;
            default:
                System.err.println("Invalid scene");
                currentScene = null;
        }
    }

    /**
     * Tries to get window. If not found it will create it first.
     *
     * @return current window.
     */
    public static Window getWindow(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    /**
     * Updates current scene and draws all objects in the scene.
     *
     * @param deltaTime  Difference between last mouse update time and current mouse update time.
     */
    public void update(double deltaTime){
        currentScene.update(deltaTime);
        draw(getGraphics());
    }

    /**
     * Draws current scene.
     *
     * @param graphics Graphics handler instance.
     * @see Graphics Graphics - Handler for graphics operations within a window.
     */
    public void draw(Graphics graphics){
        if(doubleBufferImage == null){
            doubleBufferImage = createImage(getWidth(), getHeight());
            doubleBufferGraphics = doubleBufferImage.getGraphics();
        }

        renderOffscreen(doubleBufferGraphics);
        graphics.drawImage(doubleBufferImage, 0, 0, getWidth(), getHeight(), null);
    }

    /**
     * Renders the scene offscreen.
     *
     * @param graphics Graphics handler instance.
     * @see Graphics Graphics - Handler for graphics operations within a window.
     */
    public void renderOffscreen(Graphics graphics){
        Graphics2D graphics2D = (Graphics2D)graphics;
        currentScene.draw(graphics2D);
    }

    /**
     * Runs the main game loop.
     */
    @Override
    public void run(){
        double lastFrameTime = 0.0;
        try{
            // Game loop
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

    public static MouseListener getMouseListener(){
        return getWindow().mouseListener;
    }

    public static KeyListener getKeyListener(){
        return getWindow().keyListener;
    }

    public static boolean isInEditor(){
        return getWindow().isInEditor;
    }

    public static Scene getScene(){
        return getWindow().getCurrentScene();
    }

}
