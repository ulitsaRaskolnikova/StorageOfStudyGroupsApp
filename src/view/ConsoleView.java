package view;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * ConsoleView gets input from console.
 */
public class ConsoleView implements View{
    private static final Scanner scanner = new Scanner(System.in);
    public String getInput() throws NoSuchElementException{
        return scanner.nextLine();
    }
    public void show(String output){
        System.out.print(output);
    }
}
