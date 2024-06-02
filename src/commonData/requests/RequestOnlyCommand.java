package commonData.requests;

import commonData.commandData.CommandType;
import commonData.requests.interfaces.Request;

import java.io.Serial;
import java.io.Serializable;

public class RequestOnlyCommand implements Request, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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

    @Override
    public String toString() {
        return "RequestOnlyCommand{" +
                "commandType=" + commandType +
                '}';
    }
}
