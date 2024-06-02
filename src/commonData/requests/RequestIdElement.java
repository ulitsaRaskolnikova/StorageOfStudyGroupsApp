package commonData.requests;

import commonData.commandData.CommandType;
import commonData.data.interfaces.Element;
import commonData.requests.interfaces.IRequestIdElement;

public class RequestIdElement extends RequestOnlyCommand implements IRequestIdElement {
    private long id;
    private Element element;
    public RequestIdElement(CommandType commandType, long id, Element element){
        super(commandType);
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
    public String toString() {
        return "RequestIdElement{" +
                "id=" + id +
                ", element=" + element +
                '}';
    }
}
