package com.engine;

import com.util.Transform;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private List<Component> components = null;
    private String name = null;
    public Transform transform = null;

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
    }

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

    public void addComponent(Component component) {
        components.add(component);
        component.gameObject = this;
    }

    public void update(double deltaTime){
        for (Component component : components) {
            component.update(deltaTime);
        }
    }

    public void draw(Graphics2D graphics2D){
        for (Component component : components) {
            component.draw(graphics2D);
        }
    }
}
