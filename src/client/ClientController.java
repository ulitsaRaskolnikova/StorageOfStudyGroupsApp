package client;

import client.commands.ExecuteScriptCommand;
import client.commands.HelpCommand;
import client.commands.HistoryCommand;
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

import static commonData.enums.InputType.*;

/**
 * Controller starts the program execution and execute user commands
 */
public class ClientController {
    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        Respondent.setInputType(VIEW);
        Respondent.setView(new ConsoleView());

        int port = getPort();
        Client client = new Client();
        client.setPort(port);

        String ans;
        Authorization authorization = new Authorization();
        do {
            authorization.getAuthorizationTypeByUser();
            authorization.authorize();
            ans = authorization.checkUserInfo(client);
            Respondent.sendToOutput(ans);
        } while (!ans.equals("Success!"));

        try{
            executeCommands(
                    client,
                    authorization,
                    new ExecuteScriptCommand(),
                    new HelpCommand(),
                    new HistoryCommand()
                    );
        } catch(Exception e) {
            Respondent.sendToOutput(e.getMessage() + "\n");
        }
    }
    private static int getPort(){
        while (true) {
            Respondent.sendToOutputWithoutLN("Write port: ");
            try {
                int port = Integer.parseInt(Respondent.getInput());
                if (port < 3000 || port >= 65536) {
                    throw new IOException();
                }
                return port;
            } catch (Throwable e) {
                Respondent.sendToOutput("Try again");
            }
        }
    }
    public static void executeCommands(
            Client client,
            Authorization authorization,
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
                        Request requestCheck = new RequestId(CommandType.CHECK_IS_INDEX_IN_RANGE, Handler.parseId(input));
                        //requestCheck.setLogin(authorization.getLogin());
                        String ans = client.dispatch(requestCheck);
                        if (!ans.equals("true")){
                            Respondent.sendToOutput(MessageType.OUT_OF_RANGE);
                            continue;
                        }
                    }
                    if (commandType == CommandType.UPDATE){
                        Request requestCheck = new RequestId(CommandType.CHECK_ID_EXISTS, Handler.parseId(input));
                        requestCheck.setLogin(authorization.getLogin());
                        String ans = client.dispatch(requestCheck);
                        if (!ans.equals("true")){
                            Respondent.sendToOutput(ans);
                            continue;
                        }
                    }
                    if (Respondent.getInputType() == VIEW) Respondent.sendToOutput(StudyGroup.class.getSimpleName() + ":");
                    Element element = ElementBuilderHelper.buildElement(StudyGroup.class, "StudyGroup", null);
                    ((StudyGroup) element).setUser(authorization.getLogin());
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
                    Request requestCheck = new RequestId(CommandType.CHECK_ID_EXISTS, Handler.parseId(input));
                    requestCheck.setLogin(authorization.getLogin());
                    String ans = client.dispatch(requestCheck);
                    if (!ans.equals("true")){
                        Respondent.sendToOutput(ans);
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
                        authorization,
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
            request.setLogin(authorization.getLogin());
            Respondent.sendToOutput(client.dispatch(request));
            if (isExit) break;
            historyCommand.add(commandType);
        }
    }
}
