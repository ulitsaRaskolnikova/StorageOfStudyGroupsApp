package client.controller.commands;

import client.controller.Client;
import client.controller.ClientController;
import commonData.modelHandlers.Respondent;
import commonData.enums.InputType;
import commonData.exceptions.WrongDataInputException;
import commonData.fileSystem.ScriptHandler;

import java.io.FileNotFoundException;
import java.util.HashSet;

public class ExecuteScriptCommand {
    private final HashSet<String> files = new HashSet<>();
    public void execute(
            String fileName,
            Client client,
            ExecuteScriptCommand executeScriptCommand,
            HelpCommand helpCommand,
            HistoryCommand historyCommand
    ) {
        if (files.contains(fileName)){
            Respondent.sendToOutput("The recursion of files is detected.\n");
            Respondent.setInputType(InputType.VIEW);
            return;
        }
        try{
            ScriptHandler.setScannerInput(fileName);
        } catch(FileNotFoundException e){
            Respondent.sendToOutput(e.getMessage() + "\n");
            return;
        }
        files.add(fileName);
        InputType lastInputType = Respondent.getInputType();
        Respondent.setInputType(InputType.SCRIPT);
        try{
            ClientController.executeCommands(
                    client,
                    executeScriptCommand,
                    helpCommand,
                    historyCommand
            );
        } catch(WrongDataInputException e){
            Respondent.sendToOutput(e.getMessage() + "\n");
            Respondent.setInputType(InputType.VIEW);
            return;
        } catch(Exception e){
            return;
        }
        Respondent.setInputType(lastInputType);
        files.remove(fileName);
    }
}
