package sk.stuba.fiit.martinszabo.geometrydash.engine;

import sk.stuba.fiit.martinszabo.geometrydash.components.*;
import sk.stuba.fiit.martinszabo.geometrydash.util.AssetPool;
import sk.stuba.fiit.martinszabo.geometrydash.util.Constants;
import sk.stuba.fiit.martinszabo.geometrydash.util.Transform;
import sk.stuba.fiit.martinszabo.geometrydash.util.Vector2D;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class MenuScene extends LevelEditorScene{

    private double debounceLeft = 0.0;
    private GameObject cursor = null;

    private GameObject logo = null;
    private GameObject playButton = null;
    private GameObject editorButton = null;
    private GameObject quitButton = null;
    public MenuScene(String name){
        super(name);
    }

    @Override
    public void init(){
        initAssetPool();
        initBackgrounds(5, true);

        cursor = new GameObject("Mouse Cursor", new Transform(new Vector2D()), 10);
        cursor.addComponent(new BoxBounds(10, 10));

        Sprite logoSprite = AssetPool.getSprite("assets/ui/menuButtons/logo.png");
        Sprite playButtonSprite = AssetPool.getSprite("assets/ui/menuButtons/playButton.png");
        Sprite editorButtonSprite = AssetPool.getSprite("assets/ui/menuButtons/editorButton.png");
        Sprite quitButtonSprite = AssetPool.getSprite("assets/ui/menuButtons/quitButton.png");

        logo = new GameObject("Logo", new Transform(new Vector2D((Constants.SCREEN_WIDTH / 2.0) - Constants.BUTTON_OFFSET_X, (Constants.SCREEN_HEIGHT / 2.0) - (Constants.BUTTON_OFFSET_Y / 2.0) + 30) ), 10, true, false);
        logo.getTransform().setScale(new Vector2D(0.5, 0.5));
        logo.addComponent(logoSprite);

        playButton = initializeButton("Play", (Constants.SCREEN_WIDTH / 2.0) - (playButtonSprite.getWidth() / 2.0) , Constants.SCREEN_HEIGHT / 2.0, playButtonSprite, ButtonType.PLAY);
        editorButton = initializeButton("Editor", ((Constants.SCREEN_WIDTH / 2.0) - (editorButtonSprite.getWidth() / 2.0)) / 2.0, Constants.SCREEN_HEIGHT / 2.0, editorButtonSprite, ButtonType.EDITOR);
        quitButton = initializeButton("Quit", ((Constants.SCREEN_WIDTH) - (quitButtonSprite.getWidth())) - (Constants.BUTTON_OFFSET_Y / 2.0), Constants.SCREEN_HEIGHT / 2.0, quitButtonSprite, ButtonType.QUIT);

        addGameObject(logo);
        addGameObject(playButton);
        addGameObject(editorButton);
        addGameObject(quitButton);
    }

    private GameObject initializeButton(String name, double x, double y, Sprite sprite, ButtonType type){
        GameObject button = new GameObject(name, new Transform(new Vector2D(x, y)), 10, true, false);
        Bounds buttonBounds = new BoxBounds(sprite.getWidth(), sprite.getHeight());
        MenuButton buttonComponent = new MenuButton(type);
        button.addComponent(sprite);
        button.addComponent(buttonBounds);
        button.addComponent(buttonComponent);
        return button;
    }

    @Override
    public void update(double deltaTime){
        debounceLeft -= deltaTime;

        // Updates cursor position
        Bounds cursorBounds = cursor.getComponent(Bounds.class);
        cursor.setX(Window.getMouseListener().getX() - cursorBounds.getWidth() / 2);
        cursor.setY(Window.getMouseListener().getY() - cursorBounds.getHeight() / 2);
        cursor.update(deltaTime);

        logo.update(deltaTime);
        playButton.update(deltaTime);
        editorButton.update(deltaTime);
        quitButton.update(deltaTime);

        if (
            Window.getMouseListener().isMousePressed() &&
            Window.getMouseListener().getMouseButton() == MouseEvent.BUTTON1 &&
            debounceLeft < 0
        ){
            debounceLeft = Constants.DEBOUNCE_TIME_MOUSE; // Mouse clicked

            for (GameObject gameObject : getGameObjects()){
                Bounds bounds = gameObject.getComponent(Bounds.class);
                if (bounds != null && Bounds.checkCollision(bounds, cursorBounds)){
                    gameObject.getComponent(MenuButton.class).execute();
                }
            }
        }
    }


    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.setColor(Constants.BG_COLOR);
        graphics2D.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        getRenderer().render(graphics2D);
        cursor.draw(graphics2D);
        logo.draw(graphics2D);
        playButton.draw(graphics2D);
        editorButton.draw(graphics2D);
        quitButton.draw(graphics2D);
    }
}
