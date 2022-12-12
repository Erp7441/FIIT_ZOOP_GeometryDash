package sk.stuba.fiit.martinszabo.geometrydash.main;

import sk.stuba.fiit.martinszabo.geometrydash.engine.Window;

// TODO remove setters from classes where not needed
// TODO remove unessasary "this."
// TODO remove "levels/.current.zip" file on program exit
// TODO nested getters and setters for long lines
// TODO make deserialize method override mandatory
// TODO safe casting
// TODO Create menu scene with Play, Editor, Quit

public class Main {

    public static void main(String[] args){
        Window window = Window.getWindow();
        window.init();

        Thread mainThread = new Thread(window);
        mainThread.start();
    }
}