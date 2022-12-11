package com.components;

import com.engine.Component;
import com.engine.GameObject;
import com.util.Vector2D;

public abstract class Bounds extends Component{
    private BoundsType type;
    private boolean selected;


    abstract public double getWidth();
    abstract public double getHeight();
    abstract public boolean raycast(Vector2D position);

    public static boolean checkCollision(Bounds a, Bounds b) {
        // One bound will always be a box
        if(a.type == b.type && a.type == BoundsType.BOX) {
            return BoxBounds.checkCollision((BoxBounds)a, (BoxBounds)b);
        }
        else if(a.type == BoundsType.BOX && b.type == BoundsType.TRIANGLE){
            return TriangleBounds.checkCollision((BoxBounds)a, (TriangleBounds)b);
        }
        else if(a.type == BoundsType.TRIANGLE && b.type == BoundsType.BOX){
            return TriangleBounds.checkCollision((BoxBounds)b, (TriangleBounds)a);
        }
        else{
            return false;
        }
    }

    public static void resolveCollision(Bounds b, GameObject player){
        if(b.type == BoundsType.BOX){
            BoxBounds box = (BoxBounds)b;
            box.resolveCollision(player);
        } else if (b.type == BoundsType.TRIANGLE){
            player.getComponent(Player.class).die();
        }
    }

    public BoundsType getType(){
        return type;
    }

    public void setType(BoundsType type){
        this.type = type;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
}
