package server.model.commands.сommands;

import commonData.commandData.Command;
import commonData.requests.interfaces.IRequestElement;
import commonData.requests.interfaces.IRequestId;
import server.model.interfaces.IStore;

public class IsIdxInRangeCommand implements Command<IRequestId> {
    private final IStore storage;
    public IsIdxInRangeCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(IRequestId request){
        return storage.inRange(request.getId()) ? "true" : "false";
    }
}
