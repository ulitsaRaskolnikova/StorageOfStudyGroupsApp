package fileSystem;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Scanner;

/**
 * ScriptHandler handles script and get input from it.
 */

public class ScriptHandler {
    private static Scanner scanner;
    @Setter
    @Getter
    private static boolean isValidFileName = true;
    @Setter
    @Getter
    private static boolean isValidData = true;
    @Setter
    @Getter
    private static String fileName = System.getenv("FILE_PATH");
    public static void setScannerInput(String input) throws FileNotFoundException {
        File file = new File(input);
        ScriptHandler.scanner = new Scanner(file);
    }
    public static String getLine(){
        if (!scanner.hasNext()){
            scanner.close();
            return "exit";
        }
        return scanner.nextLine();
    }
    public static void writeFile(String message) throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(fileName), "UTF8");
        writer.write(message);
        writer.close();
    }
}
