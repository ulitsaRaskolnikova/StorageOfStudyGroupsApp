package server.model.commands.сommands;

import commonData.enums.MessageType;
import commonData.commandData.Command;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.IRequestFormOfEducation;

public class RemoveFormOfEducationCommand implements Command<IRequestFormOfEducation> {
    private IStore storage;
    public RemoveFormOfEducationCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public String execute(IRequestFormOfEducation request){
        storage.removeAnyByFormOfEducation(request.getFormOfEducation());
        if (!storage.getSuccess()){
            return MessageType.NO_SUCH_FORM_OF_EDUCATION.getMessage();
        }
        else{
            return "The element is deleted.";
        }
    }
}
