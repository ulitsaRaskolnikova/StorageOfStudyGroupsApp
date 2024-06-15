package server.model.commands.сommands;

import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.IRequestIdElement;

public class UpdateCommand implements Command<IRequestIdElement> {
    private IStore storage;
    public UpdateCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(IRequestIdElement request){
        request.getElement().setId(request.getId());
        try {
            storage.update(request.getId(), request.getElement(), request.getLogin());
        } catch(Exception e){
            return e.getMessage();
        }
        return "The element is updated.\n";
    }
}
