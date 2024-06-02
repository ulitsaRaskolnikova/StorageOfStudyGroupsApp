package server.model.commands.сommands;

import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.Request;

public class ShowCommand implements Command {
    private final IStore storage;
    public ShowCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(Request request){
        return storage.toString();
    }
}
