package requests;

import model.commands.Command;
import model.commands.CommandType;
import model.data.interfaces.Element;
import requests.interfaces.IRequestIdElement;

public class RequestIdElement implements IRequestIdElement {
    private long id;
    private Element element;
    private CommandType commandType;
    public RequestIdElement(CommandType commandType, long id, Element element){
        this.commandType = commandType;
        this.id = id;
        this.element = element;
    }

    @Override
    public void setElement(Element element) {
        this.element = element;
    }

    @Override
    public Element getElement() {
        return element;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    @Override
    public CommandType getCommandType() {
        return commandType;
    }
}
