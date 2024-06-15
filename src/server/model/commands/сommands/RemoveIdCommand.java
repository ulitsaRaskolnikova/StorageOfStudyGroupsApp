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
        try {
            storage.remove(request.getId(), request.getLogin());
        } catch (Exception e){
            return e.getMessage();
        }
        return "The element is deleted.";
    }
}
