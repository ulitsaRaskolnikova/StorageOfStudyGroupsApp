package requests;

import model.commands.CommandExecutor;
import model.commands.CommandType;
import model.data.StudyGroup;
import model.data.interfaces.Element;
import model.interfaces.IStore;
import requests.interfaces.IRequestFileName;

public class RequestFileName extends RequestOnlyCommand implements IRequestFileName {
    private String name;
    private CommandExecutor commandExecutor;
    public RequestFileName(CommandType commandType, String name, CommandExecutor commandExecutor){
        super(commandType);
        this.name = name;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void setFileName(String name) {
        this.name = name;
    }

    @Override
    public String getFileName() {
        return name;
    }
    @Override
    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}
