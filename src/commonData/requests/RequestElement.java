package commonData.requests;

import commonData.commandData.CommandType;
import commonData.data.interfaces.Element;
import commonData.requests.interfaces.IRequestElement;

public class RequestElement extends RequestOnlyCommand implements IRequestElement {
    private CommandType commandType;
    private Element element;
    public RequestElement(CommandType commandType, Element element){
        super(commandType);
        this.element = element;
    }
    @Override
    public void setElement(Element element) {
        this.element = element;
    }
    @Override
    public Element getElement(){
        return element;
    }
    @Override
    public String toString(){
        return super.toString() +
                "[commandType=" + commandType +
                ",element=" + element + "]";
    }
}
