package model.commands.appCommands;

import controller.Respondent;
import model.commands.Command;
import model.commands.CommandType;
import model.interfaces.IStore;
import requests.interfaces.IRequestElement;
import requests.interfaces.Request;

import java.util.LinkedList;
import java.util.List;

public class HistoryCommand implements Command<Request> {
    private List<String> list = new LinkedList<>();
    public void add(CommandType commandType){
        list.add(commandType.getName());
        if (list.size() > 12){
            list.remove(0);
        }
    }
    @Override
    public void execute(Request request){
        Respondent.sendToOutput(list.toString() + "\n");
    }
}
