package commonData.requests;

import commonData.commandData.CommandType;
import commonData.requests.interfaces.IRequestFileName;

public class RequestFileName extends RequestOnlyCommand implements IRequestFileName {
    private String name;
    public RequestFileName(CommandType commandType, String name) {
        super(commandType);
        this.name = name;
    }
    @Override
    public void setFileName(String name) {
        this.name = name;
    }
    @Override
    public String getFileName() {
        return name;
    }
}
