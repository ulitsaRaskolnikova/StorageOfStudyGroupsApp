package commonData.modelHandlers;

import commonData.enums.MessageType;
import commonData.view.ConsoleView;
import commonData.enums.InputType;
import commonData.fileSystem.ScriptHandler;
import commonData.fileSystem.XMLHandler;
import lombok.Getter;
import lombok.Setter;
import commonData.view.View;

import java.util.NoSuchElementException;

/**
 * Respondent gets input from view, xml, script and updates view.
 */


public class Respondent {
    @Getter
    @Setter
    private static InputType inputType;
    private static View view;
    public Respondent(){
        view = new ConsoleView();
    }
    public Respondent(View view){
        Respondent.view = view;
    }
    public static void setView(View view){
        Respondent.view = view;
    }
    public static String getInput() throws NoSuchElementException {
        return switch (inputType) {
            case VIEW -> view.getInput();
            case XML_FILE -> XMLHandler.getValue();
            case SCRIPT -> ScriptHandler.getLine();
        };
    }
    public static void sendToOutput(MessageType messageType){
        view.show(messageType.getMessage());
    }
    public static void sendToOutputWithoutLN(String message){
        view.show(message);
    }
    public static void sendToOutput(String message){
        view.show(message + "\n");
    }
}
