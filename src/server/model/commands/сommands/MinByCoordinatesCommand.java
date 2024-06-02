package server.model.commands.сommands;

import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.Request;

public class MinByCoordinatesCommand implements Command<Request> {
    private IStore storage;
    public MinByCoordinatesCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(Request request){
        return storage.minByCoordinates().toString();
    }
}
