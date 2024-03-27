package model.commands.storageCommands;

import controller.Controller;
import controller.Respondent;
import controller.enums.InputType;
import fileSystem.ScriptHandler;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.IRequestFileName;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.logging.FileHandler;

public class ExecuteScriptCommand implements Command<IRequestFileName> {
    private final IStore storage;
    private final HashSet<String> files = new HashSet<>();
    public ExecuteScriptCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(IRequestFileName request){
        if (files.contains(request.getFileName())){
            Respondent.sendToOutput("The reсursion of files is detected.\n");
            Respondent.setInputType(InputType.VIEW);
            return;
        }
        try{
            ScriptHandler.setScannerInput(request.getFileName());
        } catch(FileNotFoundException e){
            Respondent.sendToOutput(e.getMessage() + "\n");
            return;
        }
        files.add(request.getFileName());
        InputType lastInputType = Respondent.getInputType();
        Respondent.setInputType(InputType.SCRIPT);
        Controller.executeCommands(storage, request.getCommandExecutor());
        Respondent.setInputType(lastInputType);
        files.remove(request.getFileName());
    }
}
