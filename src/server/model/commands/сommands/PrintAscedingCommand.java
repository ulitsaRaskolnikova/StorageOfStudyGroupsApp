package server.model.commands.сommands;

import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.Request;

public class PrintAscedingCommand implements Command<Request> {
    private IStore storage;
    public PrintAscedingCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(Request request){
        storage.sort();
        return storage.toString();
    }
}
