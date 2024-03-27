package model.commands.storageCommands;

import controller.Respondent;
import model.commands.Command;
import requests.interfaces.IRequestElement;
import model.interfaces.IStore;

public class AddCommand implements Command<IRequestElement> {
    private final IStore storage;
    public AddCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(IRequestElement request){
        storage.add(request.getElement());
        Respondent.sendToOutput("The element is added.\n");
    }
}
