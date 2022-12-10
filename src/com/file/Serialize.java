package com.file;

public interface Serialize{
    public abstract String serialize(int tabSize);

    public String addStringProperty(String name, String value, int tabSize, boolean newline, boolean comma);

    public String addIntProperty(String name, int value, int tabSize, boolean newline, boolean comma);

    public String addFloatProperty(String name, float value, int tabSize, boolean newline, boolean comma);

    public String addDoubleProperty(String name, double value, int tabSize, boolean newline, boolean comma);

    public String addBooleanProperty(String name, boolean value, int tabSize, boolean newline, boolean comma);

    public String beginObjectProperty(String name, int tabSize);

    public String endObjectProperty(int tabSize);

    public String addTabs(int tabSize);

    public String addEnding(boolean newline, boolean comma);
}
