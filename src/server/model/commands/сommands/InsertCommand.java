package server.model.commands.сommands;

import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.IRequestIndexElement;

public class InsertCommand implements Command<IRequestIndexElement> {
    private IStore storage;
    public InsertCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(IRequestIndexElement request){
        storage.insert(request.getIndex(), request.getElement());
        return "The element is inserted.";
    }
}
