package commonData.requests;

import commonData.commandData.CommandType;
import commonData.requests.interfaces.Request;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

public class RequestOnlyCommand implements Request, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private CommandType commandType;
    private String login;
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
    public void setLogin(String login){
        this.login = login;
    }
    @Override
    public String getLogin(){
        return login;
    }

    @Override
    public String toString() {
        return "RequestOnlyCommand{" +
                "commandType=" + commandType +
                '}';
    }
}
