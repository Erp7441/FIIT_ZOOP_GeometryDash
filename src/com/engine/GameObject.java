package com.engine;

import com.file.Parser;
import com.file.Serialization;
import com.util.Transform;

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

    /**
     * Initializes the game object
     *
     * @param name name of the game object for debugging purposes.
     * @param transform Object that has two attributes. One vector for position and one vector for rotation.
     * @see Transform Transform - Class containing one vector for position.
     */
    public GameObject(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
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
        return null; // TODO refactor this method
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
        GameObject newGameObject = new GameObject("Generated", this.transform.copy());
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

    @Override
    public String serialize(int tabSize) {
        if(!serializable) { return ""; }

        StringBuilder builder = new StringBuilder();

        // Game Object
        builder.append(beginObjectProperty("GameObject", tabSize));
        builder.append(this.transform.serialize(tabSize + 1));
        builder.append(addEnding(true, true));

        builder.append(addStringProperty("Name", this.name, tabSize + 1, true, !this.getComponents().isEmpty()));
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

        GameObject gameObject = new GameObject(name, transform);

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

    public void setTransform(Transform transform){
        this.transform = transform;
    }

    public void setSerializable(boolean value){
        this.serializable = value;
    }
}
