package server.model.commands.сommands;

import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.Request;

public class ClearCommand implements Command<Request> {
    private IStore storage;
    public ClearCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(Request request){
        storage.clear();
        return "The storage is clear.";
    }
}
