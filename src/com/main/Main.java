package com.main;

import com.engine.Window;

// TODO remove setters from classes where not needed
// TODO make attributes final where they should be
// TODO make attributes static where they should be

public class Main {

    public static void main(String[] args) {
        Window window = Window.getWindow();
        window.init();

        Thread mainThread = new Thread(window);
        mainThread.start();
    }
}