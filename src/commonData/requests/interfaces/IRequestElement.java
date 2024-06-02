package commonData.requests.interfaces;

import commonData.data.interfaces.Element;

public interface IRequestElement extends Request {
    void setElement(Element element);
    Element getElement();
}
