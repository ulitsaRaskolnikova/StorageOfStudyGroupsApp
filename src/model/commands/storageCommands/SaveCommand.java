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
            Respondent.sendToOutput("You can't write this file because it contains valid data. Write other file name to save storage.\n");
            String input = Respondent.getInput();
            if (ScriptHandler.getFileName().equals(input)){
                execute(request);
                return;
            }
            ScriptHandler.setValidData(true);
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
        }
        else{
            if (!ScriptHandler.getFileName().matches("^(.+)\\/([^\\/]+)$\n")){
                Respondent.sendToOutput("Wrong file name. Write new file name.\n");
                String input = Respondent.getInput();
                ScriptHandler.setFileName(input);
                execute(request);
                return;
            }
            Respondent.sendToOutput("Do you want to create file named \"" + ScriptHandler.getFileName() + "\"?\nPress enter if yes or write new file name.\n");
            String input = Respondent.getInput();
            if(input.equals("")){
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
            ScriptHandler.setFileName(input);
            execute(request);
        }
    }
}
