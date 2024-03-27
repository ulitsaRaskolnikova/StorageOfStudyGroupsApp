package model.commands.storageCommands;

import controller.enums.MessageType;
import controller.Respondent;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.IRequestFormOfEducation;

public class RemoveFormOfEducationCommand implements Command<IRequestFormOfEducation> {
    private IStore storage;
    public RemoveFormOfEducationCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(IRequestFormOfEducation request){
        storage.removeAnyByFormOfEducation(request.getFormOfEducation());
        if (!storage.getSuccess()){
            Respondent.sendToOutput(MessageType.NO_SUCH_FORM_OF_EDUCATION);
        }
        else{
            Respondent.sendToOutput("The element is deleted.\n");
        }
    }
}
