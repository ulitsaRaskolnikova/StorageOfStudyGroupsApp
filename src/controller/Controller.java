package controller;

import controller.enums.InputType;
import controller.enums.MessageType;
import controller.exceptions.WrongDataInputException;
import controller.exceptions.WrongTagException;
import fileSystem.ScriptHandler;
import fileSystem.XMLHandler;
import model.LinkedListStorage;
import model.commands.CommandExecutor;
import model.commands.CommandType;
import model.commands.appCommands.HelpCommand;
import model.commands.appCommands.HistoryCommand;
import model.commands.storageCommands.*;
import model.data.StudyGroup;
import model.data.interfaces.Element;
import model.interfaces.IStore;
import org.xml.sax.SAXException;
import requests.*;
import requests.interfaces.Request;
import view.ConsoleView;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.NoSuchElementException;

import static controller.enums.InputType.*;

/**
 * Controller starts the program execution and execute user commands
 */
public class Controller {
    public static void main(String[] args) throws ReflectiveOperationException {
        Respondent.setView(new ConsoleView());
        Respondent.setInputType(XML_FILE);
        IStore storage = null;
        try{
            storage = XMLHandler.getStorageFromXML();
            Respondent.sendToOutput("XML-file is parsed.\n");
        } catch (SAXException | WrongTagException e){
            Respondent.sendToOutput(MessageType.PARSE_FAIL.getMessage() + "\n");
            Respondent.sendToOutput(e.getMessage() + "\n");
            ScriptHandler.setValidData(false);
            storage = new LinkedListStorage();

        } catch (Throwable e) {
            Respondent.sendToOutput(MessageType.PARSE_FAIL.getMessage() + "\n");
            Respondent.sendToOutput(e.getMessage() + "\n");
            ScriptHandler.setValidFileName(false);
            if (ScriptHandler.getFileName() == null) ScriptHandler.setFileName("");
            storage = new LinkedListStorage();
        }
        CommandExecutor commandExecutor = new CommandExecutor(
                new HelpCommand(),
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
                new SaveCommand(storage),
                new ExecuteScriptCommand(storage),
                new HistoryCommand()
        );
        Respondent.setInputType(VIEW);
        try{
            executeCommands(storage, commandExecutor);
        } catch(WrongDataInputException e){
            Respondent.sendToOutput(e.getMessage() + "\n");
        } catch(NoSuchElementException e){

        }
    }
    public static void executeCommands(IStore storage, CommandExecutor commandExecutor) throws WrongDataInputException, NoSuchElementException {
        boolean isExit = false;
        while(true){
            String input = Respondent.getInput();
            if (!Handler.isValidCommandInput(input)){
                if (Respondent.getInputType() == SCRIPT){
                    throw new WrongDataInputException("Invalid command \"" + input + "\".");
                }
                Respondent.sendToOutput(MessageType.WRONG_COMMAND_INPUT);
                continue;
            }
            CommandType commandType = Handler.parseCommandType(input);
            Request request = new RequestOnlyCommand(commandType);
            switch(commandType) {
                case UPDATE, INSERT_AT, ADD:
                    if (commandType == CommandType.INSERT_AT && Handler.parseId(input) > storage.getSize()){
                        Respondent.sendToOutput(MessageType.OUT_OF_RANGE);
                        continue;
                    }
                    if (commandType == CommandType.UPDATE && !storage.checkId(Handler.parseId(input))){
                        Respondent.sendToOutput(MessageType.NO_SUCH_ID);
                        continue;
                    }
                    if (Respondent.getInputType() == VIEW) Respondent.sendToOutput(StudyGroup.class.getSimpleName() + ":\n");
                    Element element = null;
                    try{
                        element = ElementBuilderHelper.buildElement(StudyGroup.class, "StudyGroup", "");
                    } catch (WrongDataInputException | NoSuchElementException e){
                        throw e;
                    } catch(Exception ex){
                        Respondent.sendToOutput(ex.getMessage() + "\n");
                    }
                    Respondent.sendToOutput(StudyGroup.class.getSimpleName() + " is completed.\n");
                    switch(commandType){
                        case UPDATE:
                            request = new RequestIdElement(commandType, Handler.parseId(input), element);
                            break;
                        case INSERT_AT:
                            request = new RequestIndexElement(commandType, Handler.parseIndex(input), element);
                            break;
                        case ADD:
                            request = new RequestElement(commandType, element);
                            break;
                    }
                    break;
                case REMOVE_BY_ID:
                    long id = Handler.parseId(input);
                    if (!storage.checkId(id)){
                        Respondent.sendToOutput(MessageType.NO_SUCH_ID);
                        continue;
                    }
                    request = new RequestId(commandType, id);
                    break;
                case REMOVE_ANY_BY_FORM_OF_EDUCATION:
                    request = new RequestFormOfEducation(commandType, Handler.parseFormOfEducation(input));
                    break;
                case EXIT:
                    isExit = true;
                    break;
                case EXECUTE_SCRIPT:
                    request = new RequestFileName(commandType, Handler.parseScriptName(input), commandExecutor);
                    break;
            }
            if (isExit) break;
            commandExecutor.executeCommand(request);
            commandExecutor.getHistory().add(commandType);
        }
    }

}
