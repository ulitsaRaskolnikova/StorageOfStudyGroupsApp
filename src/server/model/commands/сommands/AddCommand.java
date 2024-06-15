package server.model.commands.сommands;

import commonData.commandData.Command;
import commonData.requests.interfaces.IRequestElement;
import server.model.interfaces.IStore;

public class AddCommand implements Command<IRequestElement> {
    private final IStore storage;
    public AddCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(IRequestElement request){
        try {
            storage.add(request.getElement());
        } catch (Exception e){
            return e.getMessage();
        }
        return "The element is added.";
    }
}
