package com.util;

/**
 * Time class that tracks time started and time the game is running.
 */
public class Time {
    public static double timeStarted = System.nanoTime();

    public static double getTime(){
        return (System.nanoTime() - timeStarted) * 1E-9;
    }


}
