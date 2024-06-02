package server.model.commands.сommands;

import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.Request;

public class InfoCommand implements Command {
    private final IStore storage;
    public InfoCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(Request request){
        return storage.info();
    }
}
