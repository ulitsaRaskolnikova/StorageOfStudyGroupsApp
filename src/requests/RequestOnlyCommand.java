package requests;

import model.commands.CommandType;
import requests.interfaces.Request;

public class RequestOnlyCommand implements Request {
    private CommandType commandType;
    public RequestOnlyCommand(CommandType commandType){
        this.commandType = commandType;
    }
    @Override
    public void setCommandType(CommandType commandType){
        this.commandType = commandType;
    }
    @Override
    public CommandType getCommandType(){
        return commandType;
    }
}
