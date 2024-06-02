package commonData.commandData;

import commonData.requests.interfaces.Request;

public interface Command<T extends Request> {
    String execute (T request);
}
