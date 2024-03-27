package requests;

import model.commands.CommandType;
import model.data.interfaces.Element;
import requests.interfaces.IRequestIndexElement;

public class RequestIndexElement extends RequestOnlyCommand implements IRequestIndexElement {
    private CommandType commandType;
    private int idx;
    private Element element;
    public RequestIndexElement(CommandType commandType, int idx, Element element){
        super(commandType);
        this.idx = idx;
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
    public void setIndex(int idx) {
        this.idx = idx;
    }

    @Override
    public int getIndex() {
        return idx;
    }

    @Override
    public String toString(){
        return super.toString() +
                "[commandType=" + commandType +
                ",idx=" + idx +
                ",element=" + element + "]";
    }
}
