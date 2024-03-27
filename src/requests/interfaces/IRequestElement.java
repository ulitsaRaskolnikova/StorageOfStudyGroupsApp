package requests.interfaces;

import model.data.interfaces.Element;

public interface IRequestElement extends Request {
    void setElement(Element element);
    Element getElement();
}
