package com.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 * Constants class that contains variables for modifying most aspects of the game.
 */
public class Constants {

    private Constants() {
        // Private constructor used to hide public implicit constructor of this class.
    }

    public static final int FPS = 60;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final String SCREEN_TITLE = "Geometry Dash";

    public static final int PLAYER_HEIGHT = 42;
    public static final int PLAYER_WIDTH = 42;
    public static final double JUMP_FORCE = -650;
    public static final double PLAYER_SPEED = 395;

    public static final int GROUND_Y = 714;

    public static final int CAMERA_OFFSET_X = 300;
    public static final int CAMERA_OFFSET_Y = 325;
    public static final int CAMERA_OFFSET_GROUND_Y = 50;

    public static final double GRAVITY = 2820;
    public static final double TERMINAL_VELOCITY = 1900;

    public static final int TILE_HEIGHT = 42;
    public static final int TILE_WIDTH = 42;

    public static final int BUTTON_OFFSET_X = 400;
    public static final int BUTTON_OFFSET_Y = 560;
    public static final  int BUTTON_SPACING_HZ = 10;
    public static final int BUTTON_SPACING_VT = 5;
    public static final int BUTTON_HEIGHT = 60;
    public static final int BUTTON_WIDTH = 60;

    public static final Color BG_COLOR = new Color(15.f / 255.f, 98.f / 255.f, 212.f / 255.f, 1.f);
    public static final Color GROUND_COLOR = new Color(28.f / 255.f, 70.f / 255.f, 148.f / 255.f, 1.f);

    public static final int CONTAINER_OFFSET_Y = 535;
    public static final int TAB_WIDTH = 75;
    public static final int TAB_HEIGHT = 38;
    public static final int TAB_OFFSET_X = 380;
    public static final int TAB_OFFSET_Y = 497;
    public static final int TAB_HORIZONTAL_SPACING = 10;

    public static final Stroke LINE = new BasicStroke(1.0f);
    public static final Stroke THICK_LINE = new BasicStroke(2.0f);
}
