package server.model.commands.сommands;

import commonData.commandData.Command;
import commonData.data.interfaces.Element;
import commonData.requests.interfaces.IRequestId;
import server.model.interfaces.IStore;

import java.util.Iterator;

public class DoesIdExistsCommand implements Command<IRequestId> {
    private final IStore storage;
    public DoesIdExistsCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(IRequestId request){
        return storage.checkId(request.getId()) ? "true" : "false";
    }
}
