package com.components;

import com.engine.Component;
import com.engine.GameObject;

enum BoundsType {
    Box,
    Triangle
}

public abstract class Bounds extends Component{
    public BoundsType type;

    abstract public double getWidth();
    abstract public double getHeight();

    public static boolean checkCollision(Bounds a, Bounds b) {
        if(a.type == b.type && a.type == BoundsType.Box) {
            return BoxBounds.checkCollision((BoxBounds)a, (BoxBounds)b);
        }
        else{
            return false;
        }
    }

    public static void resolveCollision(Bounds b, GameObject player){
        if(b.type == BoundsType.Box){
            BoxBounds box = (BoxBounds)b;
            box.resolveCollision(player);
        }
    }
}
