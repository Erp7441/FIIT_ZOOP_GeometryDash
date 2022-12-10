package com.file;

import com.components.Sprite;
import com.engine.Component;
import com.engine.GameObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Parser{
    private static int offset = 0;
    private static int line = 1;
    private static byte[] bytes;

    public static byte[] getBytes(){
        return bytes;
    }

    public static void openFile(String fileName){
        // Checks if file exists. If it does then get its lenght.
        File temp = new File("levels/" + fileName + ".zip");
        if(!temp.exists()){ return; }
        int lenght = (int)temp.length();

        try {
            ZipFile zip = new ZipFile("levels/" + fileName + ".zip");
            ZipEntry json = zip.getEntry(fileName + ".json");
            InputStream stream = zip.getInputStream(json);
            bytes = readAllBytes(stream);

            // Resets variables
            if(offset != 0){
                offset = 0;
            }
            if(line != 1){
                line = 1;
            }
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static byte[] readAllBytes(InputStream stream) throws IOException{
        byte[] buffer = new byte[1000];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int tempByte = 0;
        while((tempByte = stream.read(buffer)) != -1){
            bos.write(buffer, 0, tempByte);
        }
        return bos.toByteArray();
    }

    public static void skipWhitespace(){
        while(!atEnd() && (peek() == ' ' || peek() == '\n' || peek() == '\t' || peek() == '\r')){
            if(peek() == '\n') { line++; }
            advance();
        }
    }

    public static char peek() {
        return (char)bytes[offset];
    }

    public static char advance(){
        char current = peek();
        offset++;
        return current;
    }

    public static void consume(char chracter){
        char current = peek();
        if(current != chracter){
            System.out.println("Expecting '" + chracter + "' but got '" + current + "' at line: " + line);
            System.exit(-1);
        }
        offset++;
    }

    public static boolean atEnd(){
        return offset == (bytes.length)-1;
    }

    public static int parseInt(){
        skipWhitespace();
        char character;
        StringBuilder builder = new StringBuilder();

        while(!atEnd() && isDigit(peek()) || peek() == '-'){
            character = advance();
            builder.append(character);
        }

        return Integer.parseInt(builder.toString());
    }

    public static double parseDouble(){
        skipWhitespace();
        char character;
        StringBuilder builder = new StringBuilder();

        while(!atEnd() && isDigit(peek()) || peek() == '-' || peek() == '.'){
            character = advance();
            builder.append(character);
        }

        return Double.parseDouble(builder.toString());
    }

    public static float parseFloat(){
        float value = (float)parseDouble();
        consume('f');
        return value;
    }

    public static String parseString(){
        skipWhitespace();
        char character;
        StringBuilder builder = new StringBuilder();

        consume('"');
        while(!atEnd() && peek() != '"'){
            character = advance();
            builder.append(character);
        }
        consume('"');
        return builder.toString();
    }
    public static boolean parseBoolean(){
        skipWhitespace();
        StringBuilder builder = new StringBuilder();

        if(!atEnd() && peek() == 't'){
            builder.append("true");
            consumeBoolean(true);
        }
        else if(!atEnd() && peek() == 'f'){
            builder.append("false");
            consumeBoolean(false);
        }
        else{
            System.out.println("Expecting 'true' or 'false'. Got '" + peek() + " at line: " + line);
            System.exit(-1);
        }

        return Boolean.parseBoolean(builder.toString());
    }

    public static Component parseComponent(){
        String componentName = Parser.parseString();
        switch(componentName){
            case "Sprite": {
                skipWhitespace();
                Parser.consume(':');
                skipWhitespace();
                Parser.consume('{');
                return Sprite.deserialize();
            }
            default: {
                System.out.println("Could not find component '" + componentName + "' at line: " + line);
            }
        }
        return null;
    }

    public static GameObject parseGameObject(){
        if (bytes.length == 0 || atEnd()) { return null; }

        if(peek() == ',') { consume(','); }
        skipWhitespace();
        if (atEnd()) { return null; }

        return GameObject.deserialize();
    }

    private static boolean isDigit(char chracter){
        return chracter >= '0' && chracter <= '9';
    }

    public static void consumeBeginObjectProperty(String property){
        consumeProperty(property);
        consume('{');
    }
    public static void consumeEndObjectProperty(){
        skipWhitespace();
        consume('}');
    }
    public static String consumeStringProperty(String property){
        consumeProperty(property);
        return parseString();
    }
    public static int consumeIntProperty(String property){
        consumeProperty(property);
        return parseInt();
    }
    public static double consumeDoubleProperty(String property){
        consumeProperty(property);
        return parseDouble();
    }
    public static float consumeFloatProperty(String property){
        consumeProperty(property);
        return parseFloat();
    }

    public static boolean consumeBooleanProperty(String property){
        consumeProperty(property);
        return parseBoolean();
    }

    private static void consumeProperty(String property){
        skipWhitespace();
        checkString(property);
        skipWhitespace();
        consume(':');
        skipWhitespace();
    }

    private static void checkString(String str){
        String parsed = Parser.parseString();
        if (parsed.compareTo(str) != 0){
            System.out.println("Expecting '" + str +"' got '" + parsed + "' at line: " + line);
            System.exit(-1);
        }
    }

    private static void consumeBoolean(boolean value){
        if(value){
            consume('t');
            consume('r');
            consume('u');
            consume('e');
        }
        else{
            consume('f');
            consume('a');
            consume('l');
            consume('s');
            consume('e');
        }
    }
}
