package model.commands.storageCommands;

import controller.Respondent;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.IRequestIndexElement;

public class InsertCommand implements Command<IRequestIndexElement> {
    private IStore storage;
    public InsertCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(IRequestIndexElement request){
        storage.insert(request.getIndex(), request.getElement());
        Respondent.sendToOutput("The element is inserted.\n");
    }
}
