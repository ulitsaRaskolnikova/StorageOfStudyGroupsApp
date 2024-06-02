package server.model.commands.сommands;

import commonData.commandData.Command;
import commonData.fileSystem.ScriptHandler;
import server.model.interfaces.IStore;
import commonData.requests.interfaces.Request;

public class ExitCommand implements Command {
    private final IStore storage;
    private boolean isOriginFileName = true;
    public ExitCommand(IStore storage) {
        this.storage = storage;
    }
    public String execute(Request request){
        try {
            ScriptHandler.writeFile(storage.getXMLString());
            System.out.println("Storage is saved!");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "Session is over";
    }
}
