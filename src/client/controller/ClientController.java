package client.controller;

import client.controller.commands.ExecuteScriptCommand;
import client.controller.commands.HelpCommand;
import client.controller.commands.HistoryCommand;
import commonData.enums.MessageType;
import commonData.exceptions.WrongDataInputException;
import commonData.modelHandlers.ElementBuilderHelper;
import commonData.modelHandlers.Handler;
import commonData.modelHandlers.Respondent;
import commonData.requests.*;
import commonData.commandData.CommandType;
import commonData.data.StudyGroup;
import commonData.data.interfaces.Element;
import commonData.requests.interfaces.Request;
import commonData.view.ConsoleView;

import java.io.IOException;
import java.sql.PreparedStatement;

import static commonData.enums.InputType.*;

/**
 * Controller starts the program execution and execute user commands
 */
public class ClientController {
    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        Respondent.setInputType(VIEW);
        Respondent.setView(new ConsoleView());

        Respondent.sendToOutputWithoutLN("Write port: ");
        int port = 55555;
        try{
            port = Integer.parseInt(Respondent.getInput());
            if (port < 3000 || port >= 65536){
                throw new IOException();
            }
        } catch (Throwable e){
            Respondent.sendToOutput(e.getMessage());
            Respondent.sendToOutput("Try again");
            main(new String[]{});
            return;
        }
        Client client = new Client();
        client.setPort(port);
        try{
            executeCommands(
                    client,
                    new ExecuteScriptCommand(),
                    new HelpCommand(),
                    new HistoryCommand()
                    );
        } catch(Exception e) {
            Respondent.sendToOutput(e.getMessage() + "\n");
        }
    }
    public static void executeCommands(
            Client client,
            ExecuteScriptCommand executeScriptCommand,
            HelpCommand helpCommand,
            HistoryCommand historyCommand
            ) throws Exception
    {
        boolean isExit = false;
        boolean isContinue = false;
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
                    if (commandType == CommandType.INSERT_AT){
                        String ans = client.dispatch(new RequestId(CommandType.CHECK_IS_INDEX_IN_RANGE, Handler.parseId(input)));
                        if (!ans.equals("true")){
                            Respondent.sendToOutput(MessageType.OUT_OF_RANGE);
                            continue;
                        }
                    }
                    if (commandType == CommandType.UPDATE){
                        String ans = client.dispatch(new RequestId(CommandType.CHECK_ID_EXISTS, Handler.parseId(input)));
                        if (!ans.equals("true")){
                            Respondent.sendToOutput(MessageType.NO_SUCH_ID);
                            continue;
                        }
                    }
                    if (Respondent.getInputType() == VIEW) Respondent.sendToOutput(StudyGroup.class.getSimpleName() + ":");
                    Element element = ElementBuilderHelper.buildElement(StudyGroup.class, "StudyGroup", null);
                    Respondent.sendToOutput(StudyGroup.class.getSimpleName() + " is completed.");
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
                    String ans = client.dispatch(new RequestId(CommandType.CHECK_ID_EXISTS, Handler.parseId(input)));
                    if (!ans.equals("true")){
                        Respondent.sendToOutput(MessageType.NO_SUCH_ID);
                        continue;
                    }
                    request = new RequestId(commandType, Handler.parseId(input));
                    break;
                case REMOVE_ANY_BY_FORM_OF_EDUCATION:
                    request = new RequestFormOfEducation(commandType, Handler.parseFormOfEducation(input));
                    break;
                case EXECUTE_SCRIPT:
                    executeScriptCommand.execute(
                        Handler.parseScriptName(input),
                        client,
                        executeScriptCommand,
                        helpCommand,
                        historyCommand
                    );
                    isContinue = true;
                    break;
                case HELP:
                    Respondent.sendToOutputWithoutLN(helpCommand.execute(request));
                    isContinue = true;
                    break;
                case HISTORY:
                    Respondent.sendToOutput(historyCommand.execute(request));
                    isContinue = true;
                    break;
                case EXIT:
                    isExit = true;
                    request = new RequestOnlyCommand(CommandType.EXIT);
                    break;
            }
            if (isContinue) {
                isContinue = false;
                continue;
            }
            Respondent.sendToOutput(client.dispatch(request));
            if (isExit) break;
            historyCommand.add(commandType);
        }
    }
}
