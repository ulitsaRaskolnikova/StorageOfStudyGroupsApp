package model.commands.storageCommands;

import controller.Respondent;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.Request;

public class ClearCommand implements Command<Request> {
    private IStore storage;
    public ClearCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(Request request){
        storage.clear();
        Respondent.sendToOutput("The storage is clear.\n");
    }
}
