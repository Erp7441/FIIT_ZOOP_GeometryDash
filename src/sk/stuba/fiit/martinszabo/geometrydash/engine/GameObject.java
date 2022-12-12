package sk.stuba.fiit.martinszabo.geometrydash.engine;

import sk.stuba.fiit.martinszabo.geometrydash.file.Parser;
import sk.stuba.fiit.martinszabo.geometrydash.file.Serialization;
import sk.stuba.fiit.martinszabo.geometrydash.util.Transform;

import java.util.ArrayList;
import java.util.List;

import java.awt.Graphics2D;

/**
 * Class representing basic game object that will be used in the game.
 * This class has all the necessary methods and attributes that will be needed
 * in the game.
 */
public class GameObject extends Serialization{
    private List<Component> components;
    private String name;
    private Transform transform;
    private boolean serializable = true;
    private boolean ui = false;
    private int zIndex;

    /**
     * Initializes the game object
     *
     * @param name name of the game object for debugging purposes.
     * @param transform Object that has two attributes. One vector for position and one vector for rotation.
     * @see Transform Transform - Class containing one vector for position.
     */
    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.zIndex = zIndex;
    }

    public GameObject(String name, Transform transform, int zIndex, boolean ui, boolean serializable) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.zIndex = zIndex;
        this.ui = ui;
        this.serializable = serializable;
    }

    public GameObject(String name, Transform transform, int zIndex, boolean ui) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.zIndex = zIndex;
        this.ui = ui;
    }

    /**
     * Gets specific component type determined by the class name.
     *
     * @param componentClass class name of the component
     * @return component that matches the type specified in parameter
     * @param <T> generic type of the component
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try{
                    return componentClass.cast(component);
                } catch (ClassCastException e){
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return null;
    }

    /**
     * Removes specific component type determined by the class name.
     *
     * @param componentClass class name of the component
     * @return component that matches the type specified in parameter
     * @param <T> generic type of the component
     */
    public <T extends Component> T removeComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                components.remove(component);
                return null;
            }
        }
        return null;
    }

    /**
     * Gets all components of this class instance.
     *
     * @return component list containing all components of this class instance
     */
    public List<Component> getComponents(){
        return this.components;
    }

    /**
     * Adds component to the list of components of this object.
     *
     * @param component component to be added to this game object instance.
     */
    public void addComponent(Component component) {
        components.add(component);
        component.setGameObject(this);
    }

    /**
     * Copies the object by creating new one with the same attributes as this one.
     * This method also needs to loop through all components and add them to the list of components
     * of the new object.
     *
     * @return new game object instance with same attributes as this one
     */
    public GameObject copy(){
        GameObject newGameObject = new GameObject("Generated", this.transform.copy(), this.zIndex);
        for (Component component : this.components) {
            Component copy = component.copy();
            if(copy != null){
                newGameObject.addComponent(copy);
            }
        }
        return newGameObject;
    }

    /**
     * Updates all components of the game object.
     *
     * @param deltaTime Difference between last mouse update time and current mouse update time.
     */
    public void update(double deltaTime){
        for (Component component : this.components) {
            component.update(deltaTime);
        }
    }

    /**
     * Draws all components of this game object
     *
     * @param graphics2D 2D graphics handler instance.
     * @see Graphics2D Graphics2D - Handler for 2D operations within a window.
     */
    public void draw(Graphics2D graphics2D){
        for (Component component : this.components) {
            component.draw(graphics2D);
        }
    }

    public void draw(Graphics2D graphics2D, Class<?> componentClassToSkip){
        for (Component component : this.components) {
            if (!componentClassToSkip.isAssignableFrom(component.getClass())) {
                component.draw(graphics2D);
            }
        }
    }

    @Override
    public String serialize(int tabSize) {
        if(!serializable) { return ""; }

        StringBuilder builder = new StringBuilder();

        // Game Object
        builder.append(beginObjectProperty("GameObject", tabSize));
        
        // Transform
        builder.append(this.transform.serialize(tabSize + 1));
        builder.append(addEnding(true, true));

        // Name
        builder.append(addStringProperty("Name", this.name, tabSize + 1, true, true));
        builder.append(addIntProperty("ZIndex", this.zIndex, tabSize + 1, true, !this.getComponents().isEmpty()));
        if(!this.getComponents().isEmpty()){
            builder.append(beginObjectProperty("Components", tabSize + 1));
        }

        int count = 0;
        for(Component component : this.components){
            String str = component.serialize(tabSize + 2);
            if(str.compareTo("") != 0){
                builder.append(str);
                builder.append(addEnding(true, count != components.size() - 1));
            }
            count++;
        }

        if(!this.getComponents().isEmpty()){
            builder.append(endObjectProperty(tabSize + 1));
        }
        builder.append(addEnding(true, false));
        builder.append(endObjectProperty(tabSize));
        return builder.toString();
    }

    public static GameObject deserialize(){
        Parser.consumeBeginObjectProperty("GameObject");

        Transform transform = Transform.deserialize();
        Parser.consume(',');
        String name = Parser.consumeStringProperty("Name");
        Parser.consume(',');
        int zIndex = Parser.consumeIntProperty("ZIndex");

        GameObject gameObject = new GameObject(name, transform, zIndex);

        if(Parser.peek() == ','){
            Parser.consume(',');
            Parser.consumeBeginObjectProperty("Components");
            gameObject.addComponent(Parser.parseComponent());

            while(Parser.peek() == ','){
                Parser.consume(',');
                gameObject.addComponent(Parser.parseComponent());
            }

            Parser.consumeEndObjectProperty();
        }
        Parser.consumeEndObjectProperty();
        return gameObject;
    }

    public Transform getTransform(){
        return transform;
    }

    public boolean isUi(){
        return ui;
    }

    public void setTransform(Transform transform){
        this.transform = transform;
    }

    public void setSerializable(boolean value){
        this.serializable = value;
    }

    public void setUi(boolean ui){
        this.ui = ui;
    }

    public double getX(){
        return getTransform().getPosition().getX();
    }

    public double getY(){
        return getTransform().getPosition().getY();
    }
    
    public void setX(double value){
        getTransform().getPosition().setX(value);
    }

    public void setY(double value){
        getTransform().getPosition().setY(value);
    }

    public int getzIndex(){
        return zIndex;
    }

    public boolean isSerializable(){
        return serializable;
    }
}
