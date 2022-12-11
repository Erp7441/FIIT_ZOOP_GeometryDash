package com.components;

import com.engine.*;
import com.file.Parser;

public class Portal extends Component{

    private PlayerState stateChanger;
    private GameObject player;
    private BoxBounds bounds;

    public Portal(PlayerState stateChanger) {
        this.stateChanger = stateChanger;
    }

    public Portal(PlayerState stateChanger, GameObject player) {
        this.stateChanger = stateChanger;
        this.player = player;
    }

    @Override
    public void start() {
        this.bounds = getGameObject().getComponent(BoxBounds.class);
        Scene scene = Window.getScene();
        if (scene instanceof LevelScene) {
            LevelScene levelScene = (LevelScene) scene;
            this.player = levelScene.getPlayer();
        }
    }

    @Override
    public void update(double deltaTime){
        if(player == null) { return; }
        if (player.getComponent(Player.class).getState() != stateChanger && BoxBounds.checkCollision(bounds, player.getComponent(BoxBounds.class))){
            player.getTransform().setRotation(0);
            player.getComponent(Player.class).setState(stateChanger);
        }
    }

    @Override
    public Component copy(){
        return new Portal(this.stateChanger, this.player);
    }

    @Override
    public String serialize(int tabSize){
        StringBuilder builder = new StringBuilder();

        // TODO add serialization for ENUM
        int state = this.stateChanger == PlayerState.FLYING ? 1 : 0;

        builder.append(beginObjectProperty("Portal", tabSize));
        builder.append(addIntProperty("StateChanger", state, tabSize + 1, true, false));
        builder.append(endObjectProperty(tabSize));

        return builder.toString();
    }

    public static Portal deserialize(){
        int state = Parser.consumeIntProperty("StateChanger");
        Parser.consumeEndObjectProperty();

        PlayerState stateChanger = state == 1 ? PlayerState.FLYING : PlayerState.NORMAL;

        return new Portal(stateChanger);
    }

    public PlayerState getStateChanger(){
        return stateChanger;
    }
}
