package server;

import commonData.data.StudyGroup;
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

import java.sql.SQLException;

public class StorageController {
    IStore storage;
    CommandExecutor commandExecutor;
    public String executeCommand(Request request){
        return commandExecutor.executeCommand(request);
    }
    private StorageController(IStore storage, CommandExecutor commandExecutor) {
        this.storage = storage;
        this.commandExecutor = commandExecutor;
    }
    public static StorageController initDB(SQLController sqlController){
        IStore storage;
        try {
            storage = sqlController.getData();
        } catch (SQLException e) {
            storage = new LinkedListStorage<StudyGroup>();
            //log
            System.out.println(e.getMessage());
        }
        var commandExecutor = initCommandExecutor(storage, sqlController);
        return new StorageController(storage, commandExecutor);
    }
    public static StorageController initXML(SQLController sqlController){
        IStore storage;
        Respondent.setView(new ConsoleView());
        ScriptHandler.setFileName("StudyGroups.xml");
        Respondent.setInputType(InputType.XML_FILE);
        try{
            storage = XMLHandler.getStorageFromXML();
            Respondent.sendToOutput("XML-file is parsed.");
        } catch (SAXException | WrongTagException e) {
            // log
            Server.getLogger().info(e.getMessage());
            ScriptHandler.setValidData(false);
            storage = new LinkedListStorage();
        } catch (Throwable e) {
            Server.getLogger().info(e.getMessage());
            // log
            ScriptHandler.setValidFileName(false);
            if (ScriptHandler.getFileName() == null) ScriptHandler.setFileName("");
            storage = new LinkedListStorage();
        }
        System.out.println(storage);
        var commandExecutor = initCommandExecutor(storage, sqlController);
        return new StorageController(storage, commandExecutor);
    }
    private static CommandExecutor initCommandExecutor(IStore storage, SQLController sqlController){
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
                new ExitCommand(storage),
                new CheckUserInfoCommand(sqlController)
        );
        return commandExecutor;
    }
}
