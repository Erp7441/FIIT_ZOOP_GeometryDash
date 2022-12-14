package sk.stuba.fiit.martinszabo.geometrydash.components;

import sk.stuba.fiit.martinszabo.geometrydash.engine.Component;
import sk.stuba.fiit.martinszabo.geometrydash.engine.Window;

public class MenuButton extends Component<MenuButton>{

    ButtonType type;
    public MenuButton(ButtonType type){
        this.type = type;
    }

    public void execute(){
        switch(type){
            case PLAY: {
                Window.getWindow().changeScene(1);
                break;
            }
            case EDITOR: {
                Window.getWindow().changeScene(0);
                break;
            }
            case QUIT: {
                System.exit(0);
            }
        }
    }

    @Override
    public Component<MenuButton> copy(){
        return new MenuButton(this.type);
    }

    @Override
    public String serialize(int tabSize){
        return null;
    }
}
