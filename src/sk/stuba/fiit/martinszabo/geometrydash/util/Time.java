package sk.stuba.fiit.martinszabo.geometrydash.util;

/**
 * Time class that tracks time started and time the game is running.
 */
public class Time {
    private static double timeLevelStarted = System.nanoTime();

    private Time(){
        // Private constructor used to hide public implicit constructor of this class.
    }

    public static double getTime(){
        return (System.nanoTime() - timeLevelStarted) * 1E-9;
    }

    public static void resetTimeStarted(){
        timeLevelStarted = Constants.TIME_GAME_STARTED;
    }

}
