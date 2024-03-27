package requests.interfaces;

import model.commands.CommandExecutor;
import model.commands.CommandType;
import model.data.StudyGroup;
import model.interfaces.IStore;

public interface IRequestFileName extends Request {
    void setFileName(String name);
    String getFileName();
    void setCommandExecutor(CommandExecutor commandExecutor);
    CommandExecutor getCommandExecutor();
}
