package commonData.requests.interfaces;

import server.model.commands.CommandExecutor;

public interface IRequestFileName extends Request {
    void setFileName(String name);
    String getFileName();
}
