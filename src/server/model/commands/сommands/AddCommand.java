package server.model.commands.сommands;

import commonData.commandData.Command;
import commonData.requests.interfaces.IRequestElement;
import server.model.interfaces.IStore;

public class AddCommand implements Command<IRequestElement> {
    private final IStore storage;
    public AddCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(IRequestElement request){
        storage.add(request.getElement());
        return "The element is added.";
    }
}
