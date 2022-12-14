package sk.stuba.fiit.martinszabo.geometrydash.main;

import sk.stuba.fiit.martinszabo.geometrydash.engine.Window;

public class Main {

    public static void main(String[] args){
        Window window = Window.getWindow();
        window.init();

        Thread mainThread = new Thread(window);
        mainThread.start();
    }
}