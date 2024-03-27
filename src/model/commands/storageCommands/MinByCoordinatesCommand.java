package model.commands.storageCommands;

import controller.Respondent;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.IRequestId;
import requests.interfaces.Request;

public class MinByCoordinatesCommand implements Command<Request> {
    private IStore storage;
    public MinByCoordinatesCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(Request request){
        Respondent.sendToOutput(storage.minByCoordinates().toString() + "\n");
    }
}
