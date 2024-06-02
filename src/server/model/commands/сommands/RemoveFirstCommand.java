package server.model.commands.сommands;

import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.Request;

public class RemoveFirstCommand implements Command<Request> {
    private IStore storage;
    public RemoveFirstCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(Request request){
        storage.removeFirst();
        return "The first element is removed.";
    }
}