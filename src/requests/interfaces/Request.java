package requests.interfaces;

import model.commands.CommandType;

/**
 * Request and it's subclasses describes type of request to model.
 */

public interface Request {
    void setCommandType(CommandType commandType);
    CommandType getCommandType();
}
