package server.model.commands.сommands;

import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.IRequestId;

public class RemoveIdCommand implements Command<IRequestId> {
    private IStore storage;
    public RemoveIdCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(IRequestId request){
        storage.remove(request.getId());
        return "The element is deleted.";
    }
}
