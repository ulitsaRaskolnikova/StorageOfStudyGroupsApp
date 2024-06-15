package client.commands;

import commonData.commandData.UtilCommand;
import commonData.commandData.Command;
import commonData.commandData.CommandType;
import commonData.requests.interfaces.Request;

public class HelpCommand implements Command<Request> {
    public String execute(Request request){
        StringBuilder sb = new StringBuilder();
        for (CommandType commandType : CommandType.values()){
            if (!commandType.getClass().isAnnotationPresent(UtilCommand.class)){
                sb.append(commandType.getDescription());
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
