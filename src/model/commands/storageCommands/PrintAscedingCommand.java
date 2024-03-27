package model.commands.storageCommands;

import controller.Respondent;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.Request;

public class PrintAscedingCommand implements Command<Request> {
    private IStore storage;
    public PrintAscedingCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(Request request){
        storage.sort();
        Respondent.sendToOutput(storage.toString() + "\n");
    }
}
