package sk.stuba.fiit.martinszabo.geometrydash.file;

public interface Serialize{

    String serialize(int tabSize);

    String addStringProperty(String name, String value, int tabSize, boolean newline, boolean comma);

    String addIntProperty(String name, int value, int tabSize, boolean newline, boolean comma);

    String addFloatProperty(String name, float value, int tabSize, boolean newline, boolean comma);

    String addDoubleProperty(String name, double value, int tabSize, boolean newline, boolean comma);

    String addBooleanProperty(String name, boolean value, int tabSize, boolean newline, boolean comma);

    String beginObjectProperty(String name, int tabSize);

    String endObjectProperty(int tabSize);

    String addTabs(int tabSize);

    String addEnding(boolean newline, boolean comma);
}
