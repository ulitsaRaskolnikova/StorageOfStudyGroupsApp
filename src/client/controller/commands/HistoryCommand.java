package client.controller.commands;

import commonData.commandData.Command;
import commonData.commandData.CommandType;
import commonData.requests.interfaces.Request;

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
    public String execute(Request request){
        return list.toString();
    }
}
