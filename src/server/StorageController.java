package server;

import commonData.view.ConsoleView;
import commonData.modelHandlers.Respondent;
import commonData.enums.InputType;
import commonData.exceptions.WrongTagException;
import commonData.requests.interfaces.Request;
import org.xml.sax.SAXException;
import commonData.fileSystem.ScriptHandler;
import commonData.fileSystem.XMLHandler;
import server.model.LinkedListStorage;
import server.model.commands.CommandExecutor;
import server.model.commands.сommands.*;
import server.model.interfaces.IStore;

public class StorageController {
    IStore storage;
    CommandExecutor commandExecutor;
    public String executeCommand(Request request){
        return commandExecutor.executeCommand(request);
    }
    private StorageController(IStore storage, CommandExecutor commandExecutor){
        this.storage = storage;
        this.commandExecutor = commandExecutor;
    }
    public static StorageController init(){
        IStore storage;
        Respondent.setView(new ConsoleView());
        ScriptHandler.setFileName("StudyGroups.xml");
        Respondent.setInputType(InputType.XML_FILE);
        try{
            storage = XMLHandler.getStorageFromXML();
            Respondent.sendToOutput("XML-file is parsed.");
        } catch (SAXException | WrongTagException e) {
            //System.out.println(e.getMessage());
            //Respondent.sendToOutput(MessageType.PARSE_FAIL.getMessage() + "\n");
            //Respondent.sendToOutput(e.getMessage() + "\n");
            // log
            Server.getLogger().info(e.getMessage());
            ScriptHandler.setValidData(false);
            storage = new LinkedListStorage();
        } catch (Throwable e) {
            Server.getLogger().info(e.getMessage());
            //Respondent.sendToOutput(MessageType.PARSE_FAIL.getMessage() + "\n");
            //Respondent.sendToOutput(e.getMessage() + "\n");
            // log
            ScriptHandler.setValidFileName(false);
            if (ScriptHandler.getFileName() == null) ScriptHandler.setFileName("");
            storage = new LinkedListStorage();
        }
        System.out.println(storage);
        CommandExecutor commandExecutor = new CommandExecutor(
                new AddCommand(storage),
                new InfoCommand(storage),
                new ShowCommand(storage),
                new ClearCommand(storage),
                new RemoveFirstCommand(storage),
                new PrintAscedingCommand(storage),
                new MinByCoordinatesCommand(storage),
                new RemoveFormOfEducationCommand(storage),
                new InsertCommand(storage),
                new RemoveIdCommand(storage),
                new UpdateCommand(storage),
                new IsIdxInRangeCommand(storage),
                new DoesIdExistsCommand(storage),
                new ExitCommand(storage)
        );
        return new StorageController(storage, commandExecutor);
    }
}
