package commonData.requests.interfaces;

import commonData.commandData.CommandType;

/**
 * Request and it's subclasses describes type of request to model.
 */

public interface Request {
    void setCommandType(CommandType commandType);
    CommandType getCommandType();
}
