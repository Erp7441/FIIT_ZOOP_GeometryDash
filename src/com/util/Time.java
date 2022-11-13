package com.util;

/**
 * Time class that tracks time started and time the game is running.
 */
public class Time {
    private static double timeStarted = System.nanoTime();

    private Time(){
        // Private constructor used to hide public implicit constructor of this class.
    }

    public static double getTime(){
        return (System.nanoTime() - timeStarted) * 1E-9;
    }

    public static double getTimeStarted(){
        return timeStarted;
    }

}
