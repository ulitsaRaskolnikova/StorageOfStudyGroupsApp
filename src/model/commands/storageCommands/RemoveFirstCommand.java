package model.commands.storageCommands;

import controller.Respondent;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.IRequestId;
import requests.interfaces.Request;

public class RemoveFirstCommand implements Command<Request> {
    private IStore storage;
    public RemoveFirstCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(Request request){
        storage.removeFirst();
        Respondent.sendToOutput("The first element is removed.\n");
    }
}