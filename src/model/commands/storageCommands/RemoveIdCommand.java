package model.commands.storageCommands;

import controller.Respondent;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.IRequestId;

public class RemoveIdCommand implements Command<IRequestId> {
    private IStore storage;
    public RemoveIdCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(IRequestId request){
        storage.remove(request.getId());
        Respondent.sendToOutput("The element is deleted.\n");
    }
}
