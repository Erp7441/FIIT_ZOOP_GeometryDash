package sk.stuba.fiit.martinszabo.geometrydash.components;

import sk.stuba.fiit.martinszabo.geometrydash.engine.*;

import sk.stuba.fiit.martinszabo.geometrydash.util.Constants;
import sk.stuba.fiit.martinszabo.geometrydash.util.Vector2D;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

/**
 * Snapping game object component that is used to snap selected game objects to the screen grid
 * inside the editor. It is responsible for copying selected object to grid, adding the object to
 * current scene, snapping into correct position on the grid and making sure that the player won't
 * accidentally place too many objects on the grid at once.
 *
 * @see GameObject GameObject – Base object from which everything is derived from.
 * @see Component Component – An add-on to the game object.
 */
public class LevelEditorControls extends Component<LevelEditorControls> {

    private double debounceLeft = 0.0;

    
    private double debounceKeyLeft = 0.0;

    private boolean shiftKeyPressed = false;

    // TODO:: is this right?
    private final ArrayList<GameObject> selectedObjects;
    private int width;
    private int height;

    private double worldX;
    private double worldY;
    private boolean isEditing;
    private boolean wasDragged = false;
    private double dragX;
    private double dragY;
    private double dragWidth;
    private double dragHeight;

    public LevelEditorControls(int width, int height) {
        this.width = width;
        this.height = height;
        this.selectedObjects = new ArrayList<>();
    }

    public void updateSpritePosition(){
        this.worldX = Math.floor((Window.getMouseListener().getX() + Window.getCamera().getX() + Window.getMouseListener().getDx()) / this.width);
        this.worldY = Math.floor((Window.getMouseListener().getY() + Window.getCamera().getY() + Window.getMouseListener().getDy()) / this.height);
        this.getGameObject().setX(worldX * this.width - Window.getCamera().getX());
        this.getGameObject().setY(worldY * this.height - Window.getCamera().getY());
    }

    public void copyGameObjectToScene(){
        GameObject object = getGameObject().copy();
        object.getTransform().setPosition(new Vector2D(worldX * this.width, worldY * this.height));
        Window.getScene().addGameObject(object);
    }

    public void addToSelected(Vector2D mouse){
        mouse.setX(mouse.getX() + Window.getCamera().getX());
        mouse.setY(mouse.getY() + Window.getCamera().getY());

        for(GameObject gameObject : Window.getScene().getGameObjects()){
            Bounds bounds = gameObject.getComponent(Bounds.class);
            if(bounds != null && bounds.raycast(mouse)){
                if(selectedObjects.contains(gameObject)){
                    selectedObjects.remove(gameObject);
                    bounds.setSelected(false);
                }
                else{
                    selectedObjects.add(gameObject);
                    bounds.setSelected(true);
                }
                break;
            }
        }
    }

    public ArrayList<GameObject> boxCast (double x, double y, double width, double height){

        double x0 = x + Window.getCamera().getX();
        double y0 = y + Window.getCamera().getY();

        ArrayList<GameObject> objects = new ArrayList<>();
        for(GameObject gameObject : Window.getScene().getGameObjects()){
            Bounds bounds = gameObject.getComponent(Bounds.class);
            if(
                bounds != null &&
                (gameObject.getX() + bounds.getWidth() <= x0 + width &&
                gameObject.getY() + bounds.getHeight() <= y0 + height &&
                gameObject.getX() >= x0 && gameObject.getY() >= y0)
            ){
                objects.add(gameObject);
            }
        }
        return objects;
    }

    /**
     * Clears list of selected objects
     *
     * @param mouse - mouse position
     * @param addCurrentToSelected - adds object currently hovered to the selected list after clearing
     */
    public void clearSelected(Vector2D mouse, boolean addCurrentToSelected) {
        for(GameObject gameObject : selectedObjects){
            gameObject.getComponent(Bounds.class).setSelected(false);
        }
        selectedObjects.clear();
        if(addCurrentToSelected){
            addToSelected(mouse);
        }
    }

    public void escape(){
        GameObject mouseCursor = new GameObject("Mouse cursor", this.getGameObject().getTransform().copy(), this.getGameObject().getzIndex());
        mouseCursor.addComponent(this);
        LevelEditorScene scene;
        Scene windowScene = Window.getScene();
        if (windowScene instanceof LevelEditorScene) {
            scene = (LevelEditorScene) windowScene;
        }
        else{
            return;
        }
        scene.setCursor(mouseCursor);
        isEditing = false;
        clearSelected(mouseCursor.getTransform().getPosition(), false);
    }

    /**
     * Updates the position where to snap currently selected preview game object on the grid. First we set
     * debounce time so that the player can't add too many items to the grid at once. Then if parent
     * game object of this component is a Sprite class we look for coordinates to snap the preview
     * object to one of the grid cells that is currently closest to the mouse position.
     * <p>
     * If the player is within the game plane and clicks left mouse button it resets debounce time and
     * places the preview object in currently snapped cell on the grid by copying the preview object and
     * setting its transform to the preview object transform. Lastly it will add the newly created object
     * to scene game objects list.
     *
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     * @see Window Window – Window where the game is being rendered.
     * @see Constants Constants – Constants that manipulate the state calculation of the game.
     * @see Vector2D Vector2D – Vector that has lenght and a direction within the 2D space.
     */
    @Override
    public void update(double deltaTime){
        debounceLeft -= deltaTime;
        debounceKeyLeft -= deltaTime;

        if(Window.getKeyListener().isKeyPressed(KeyEvent.VK_ESCAPE) && Window.getKeyListener().isKeyPressed(KeyEvent.VK_SHIFT) && debounceKeyLeft < 0){
            debounceKeyLeft = Constants.DEBOUNCE_TIME_KEY;
            Window.getWindow().changeScene(2);
            return;
        }

        if (!isEditing && this.getGameObject().getComponent(Sprite.class) != null){
            this.isEditing = true;
        }

        if (isEditing){
            if(!selectedObjects.isEmpty()){
                clearSelected(new Vector2D(Window.getMouseListener().getX(), Window.getMouseListener().getY()), false);
            }
            updateSpritePosition();
        }

        checkMouse();

        if (Window.getKeyListener().isKeyPressed(KeyEvent.VK_ESCAPE)){
            escape();
        }

        shiftKeyPressed = Window.getKeyListener().isKeyPressed(KeyEvent.VK_SHIFT);
        checkMovement();
        checkRotation();
        checkDuplication();
        checkRemoval();

    }

    private void checkMouse(){
        if (
            Window.getMouseListener().getY() < Constants.TAB_OFFSET_Y &&
            Window.getMouseListener().isMousePressed() &&
            Window.getMouseListener().getMouseButton() == MouseEvent.BUTTON1 &&
            debounceLeft < 0 &&
            !wasDragged
        ){
            debounceLeft = Constants.DEBOUNCE_TIME_MOUSE; // Mouse clicked

            if (isEditing){
                copyGameObjectToScene();
            }
            else if (Window.getKeyListener().isKeyPressed(KeyEvent.VK_SHIFT)){
                addToSelected(new Vector2D(Window.getMouseListener().getX(), Window.getMouseListener().getY()));
            }
            else{
                clearSelected(new Vector2D(Window.getMouseListener().getX(), Window.getMouseListener().getY()), true);
            }
        }
        else if (!Window.getMouseListener().isMousePressed() && wasDragged){
            wasDragged = false;
            clearSelected(new Vector2D(Window.getMouseListener().getX(), Window.getMouseListener().getY()), true);
            ArrayList<GameObject> objects = boxCast(dragX, dragY, dragWidth, dragHeight); // Responsible for creating selection box for objects
            for (GameObject gameObject : objects){
                selectedObjects.add(gameObject);
                Bounds bounds = gameObject.getComponent(Bounds.class);
                if (bounds != null){
                    bounds.setSelected(true);
                }
            }
        }
    }

    private void checkMovement(){
        if (debounceKeyLeft <= 0 && Window.getKeyListener().isKeyPressed(KeyEvent.VK_LEFT)){
            move(Direction.LEFT);
            debounceKeyLeft = Constants.DEBOUNCE_TIME_KEY;
        }
        else if (debounceKeyLeft <= 0 && Window.getKeyListener().isKeyPressed(KeyEvent.VK_RIGHT)){
            move(Direction.RIGHT);
            debounceKeyLeft = Constants.DEBOUNCE_TIME_KEY;
        }
        else if (debounceKeyLeft <= 0 && Window.getKeyListener().isKeyPressed(KeyEvent.VK_UP)){
            move(Direction.UP);
            debounceKeyLeft = Constants.DEBOUNCE_TIME_KEY;
        }
        else if (debounceKeyLeft <= 0 && Window.getKeyListener().isKeyPressed(KeyEvent.VK_DOWN)){
            move(Direction.DOWN);
            debounceKeyLeft = Constants.DEBOUNCE_TIME_KEY;
        }
    }

    private void checkRotation(){
        if (debounceKeyLeft <= 0 && (Window.getKeyListener().isKeyPressed(KeyEvent.VK_E))){
            rotate(90);
            debounceKeyLeft = Constants.DEBOUNCE_TIME_KEY;
        }
        else if(debounceKeyLeft <= 0 && (Window.getKeyListener().isKeyPressed(KeyEvent.VK_Q))){
            rotate(-90);
            debounceKeyLeft = Constants.DEBOUNCE_TIME_KEY;
        }
    }

    private void checkDuplication(){
        if (debounceKeyLeft <= 0 && (Window.getKeyListener().isKeyPressed(KeyEvent.VK_D) && Window.getKeyListener().isKeyPressed(KeyEvent.VK_CONTROL))){
            duplicate();
            debounceKeyLeft = Constants.DEBOUNCE_TIME_KEY;
        }
    }

    private void checkRemoval(){
        if (debounceKeyLeft <= 0 && (Window.getKeyListener().isKeyPressed(KeyEvent.VK_DELETE))){
            for (GameObject gameObject : selectedObjects){
                Window.getScene().removeGameObject(gameObject);
            }
            selectedObjects.clear();
        }
    }

    private void move(Direction direction){

        Vector2D distance = new Vector2D(0.0, 0.0);
        double scale = 1.0;

        if(shiftKeyPressed){
            scale = 0.1;
        }

        switch(direction){
            case UP:{
                distance.setY(-Constants.TILE_HEIGHT * scale);
                break;
            }
            case DOWN:{
                distance.setY(Constants.TILE_HEIGHT * scale);
                break;
            }
            case LEFT:{
                distance.setX(-Constants.TILE_WIDTH * scale);
                break;
            }
            case RIGHT:{
                distance.setX(Constants.TILE_WIDTH * scale);
                break;
            }
            default:{
                System.out.println("Error: Direction has no enum value '" + direction + "'");
                System.exit(-1);
                break;
            }
        }
        for(GameObject gameObject : selectedObjects){
            gameObject.setX(gameObject.getX() + distance.getX());
            gameObject.setY(gameObject.getY() + distance.getY());

            double gridX = (Math.floor(gameObject.getX() / Constants.TILE_WIDTH) + 1.0) * Constants.TILE_WIDTH;
            double gridY = Math.floor(gameObject.getY() / Constants.TILE_HEIGHT) * Constants.TILE_HEIGHT;

            if (gameObject.getX() < gridX + 1.0 && gameObject.getX() > gridX -1.0){
                gameObject.setX(gridX);
            }
            if (gameObject.getY() < gridY + 1.0 && gameObject.getY() > gridY - 1.0){
                gameObject.setY(gridY);
            }
            TriangleBounds bounds = gameObject.getComponent(TriangleBounds.class);
            if (bounds != null){
                bounds.calculateTransformation();
            }
        }
    }

    private void rotate(double degrees){
        for (GameObject gameObject : selectedObjects){
            gameObject.getTransform().setRotation(gameObject.getTransform().getRotation() + degrees);
            TriangleBounds bounds = gameObject.getComponent(TriangleBounds.class);
            if (bounds != null){
                bounds.calculateTransformation();
            }
        }
    }

    private void duplicate(){
        for (GameObject gameObject : selectedObjects){
            Window.getScene().addGameObject(gameObject.copy());
        }
    }

    /**
     *  Draws the currently selected game object sprite on the screen grid cell with
     *  specified amount of alpha to indicate that it is not yet placed in the grid cell.
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     * @see Sprite Sprite – a piece of 2D texture.
     */
    @Override
    public void draw(Graphics2D graphics2D){
        if(isEditing){
            Sprite sprite = getGameObject().getComponent(Sprite.class);
            if(sprite != null){
                AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                graphics2D.setComposite(alphaComposite);
                graphics2D.drawImage(sprite.getImage(), (int) getGameObject().getX(), (int) getGameObject().getY(), sprite.getWidth(), sprite.getHeight(), null);
                alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f);
                graphics2D.setComposite(alphaComposite);

            }
        }
        else if (Window.getMouseListener().isMouseDragged() && Window.getMouseListener().getMouseButton() == MouseEvent.BUTTON1){
            wasDragged = true;
            graphics2D.setColor(new Color(1, 1, 1, 0.3f));
            dragX = Window.getMouseListener().getX();
            dragY = Window.getMouseListener().getY();
            dragWidth = Window.getMouseListener().getDx();
            dragHeight = Window.getMouseListener().getDy();
            if (dragWidth < 0) {
                dragWidth *= -1;
                dragX -= dragWidth;
            }
            if (dragHeight < 0) {
                dragHeight *= -1;
                dragY -= dragHeight;
            }
            graphics2D.fillRect((int) dragX, (int) dragY, (int) dragWidth, (int) dragHeight);
        }
    }

    @Override
    public Component<LevelEditorControls> copy() {
        return null; // Copy not needed for this component
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

    @Override
    public String serialize(int tabSize) {
        return ""; // Serialize not needed for this component
    }
}
