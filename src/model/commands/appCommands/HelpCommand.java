package model.commands.appCommands;

import controller.Respondent;
import model.commands.Command;
import model.commands.CommandType;
import requests.interfaces.Request;

public class HelpCommand implements Command<Request> {
    public void execute(Request request){
        for (CommandType commandType : CommandType.values()){
            Respondent.sendToOutput(commandType.getDescription() + "\n");
        }
    }
}
