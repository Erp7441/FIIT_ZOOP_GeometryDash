package com.engine;

import com.util.Transform;

import java.util.ArrayList;
import java.util.List;

import java.awt.Graphics2D;

/**
 * Class representing basic game object that will be used in the game.
 * This class has all the necessary methods and attributes that will be needed
 * in the game.
 */
public class GameObject {
    private List<Component> components = null;
    private String name = null;
    public Transform transform = null;

    /**
     * Inicializes the game object
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
        component.gameObject = this;
    }

    /**
     * Copies the object by creating new one with the same attributes as this one.
     * This method also needs to loop through all components and add them to the list of components
     * of the new object.
     *
     * @return new game object instance with same attributes as this one
     */
    public GameObject copy(){
        GameObject newGameObject = new GameObject("Generated", transform.copy());
        for (Component component : components) {
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
     * @param deltaTime Diffrence between last mouse update time and current mouse update time.
     */
    public void update(double deltaTime){
        for (Component component : components) {
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
        for (Component component : components) {
            component.draw(graphics2D);
        }
    }
}
