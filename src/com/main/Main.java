package com.main;

import com.engine.Window;

// TODO remove setters from classes where not needed
// TODO make attributes final where they should be
// TODO make attributes static where they should be
// TODO change primitive types to objects (should i?)
// TODO serialize and deserialize
// TODO bind player input
// TODO remove unessasary "this."
// TODO remove "levels/.current.zip" file on program exit
// TODO nested getters and setters for long lines
// TODO make deserialize method override mandatory
// TODO safe casting


// TODO FIX Playere randomly dying on cubes

public class Main {

    public static void main(String[] args){
        Window window = Window.getWindow();
        window.init();

        Thread mainThread = new Thread(window);
        mainThread.start();
    }
}