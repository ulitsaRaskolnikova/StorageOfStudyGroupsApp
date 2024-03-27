package model.commands.storageCommands;

import controller.Respondent;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.IRequestIdElement;

public class UpdateCommand implements Command<IRequestIdElement> {
    private IStore storage;
    public UpdateCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(IRequestIdElement request){
        request.getElement().setId(request.getId());
        storage.update(request.getId(), request.getElement());
        Respondent.sendToOutput("The element is updated.\n");
    }
}
