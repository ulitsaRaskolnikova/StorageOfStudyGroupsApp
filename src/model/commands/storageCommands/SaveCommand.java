package model.commands.storageCommands;

import controller.Respondent;
import fileSystem.ScriptHandler;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.IRequestElement;
import requests.interfaces.Request;

import java.util.logging.FileHandler;

public class SaveCommand implements Command<Request> {
    private final IStore storage;
    public SaveCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(Request request){
        if (!ScriptHandler.isValidData()){
            Respondent.sendToOutput("You can't write to the file \"" + ScriptHandler.getFileName() + "\" because it contains valid data. Write other file name to save storage.\n");
            String input = Respondent.getInput();
            ScriptHandler.setValidData(true);
            ScriptHandler.setValidFileName(false);
            ScriptHandler.setFileName(input);
            execute(request);
            return;
        }
        if (ScriptHandler.isValidFileName()){
            try{
                ScriptHandler.writeFile(storage.getXMLString());
                Respondent.sendToOutput("Saved!\n");
            }
            catch(Exception e){
                Respondent.sendToOutput(e.getMessage() + "\n");
                ScriptHandler.setValidFileName(false);
                execute(request);
            }
            return;
        }
        if (!ScriptHandler.getFileName().matches("\\w+(\\/\\w+)*(\\.\\w+)?")){
            Respondent.sendToOutput("Wrong file name \"" + ScriptHandler.getFileName() + "\". Write new file name.\n");
            String input = Respondent.getInput();
            ScriptHandler.setFileName(input);
            execute(request);
            return;
        }

        //Respondent.sendToOutput("Do you want to write information to the file named \"" + ScriptHandler.getFileName() + "\"?\nPress enter if yes or write new file name.\n");
        //String input = Respondent.getInput();
        //if(input.equals("")){
        try{
            ScriptHandler.writeFile(storage.getXMLString());
            ScriptHandler.setValidFileName(true);
            Respondent.sendToOutput("Saved!\n");
        }
        catch(Exception e){
            Respondent.sendToOutput(e.getMessage() + "\n");
            ScriptHandler.setValidFileName(false);
            String input = Respondent.getInput();
            ScriptHandler.setFileName(input);
            execute(request);
        }
        //return;
        //}
        //ScriptHandler.setFileName(input);
    }
}
