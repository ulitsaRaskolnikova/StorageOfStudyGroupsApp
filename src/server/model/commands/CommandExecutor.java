package server.model.commands;

import commonData.commandData.Command;
import commonData.commandData.CommandType;
import commonData.requests.interfaces.Request;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * CommandExecutor executes app commands.
 */
public class CommandExecutor {
    private Request request = null;
    private Command add;
    private Command update;
    private Command removeId;
    private Command clear;
    private Command insert;
    private Command removeFirst;
    private Command removeFormOfEducation;
    private Command minByCoordinates;
    private Command printAscending;
    private Command info;
    private Command show;
    private Command isIdxInRange;
    private Command doesIdExists;
    private Command exit;
    public CommandExecutor(
            Command add,
            Command info,
            Command show,
            Command clear,
            Command removeFirst,
            Command printAscending,
            Command minByCoordinates,
            Command removeFormOfEducation,
            Command insert,
            Command removeId,
            Command update,
            Command isIdxInRange,
            Command doesIdExists,
            Command exit
    ){
        this.add = add;
        this.info = info;
        this.show = show;
        this.clear = clear;
        this.removeFirst = removeFirst;
        this.printAscending = printAscending;
        this.minByCoordinates = minByCoordinates;
        this.removeFormOfEducation = removeFormOfEducation;
        this.insert = insert;
        this.removeId = removeId;
        this.update = update;
        this.isIdxInRange = isIdxInRange;
        this.doesIdExists = doesIdExists;
        this.exit = exit;
    }
    private final HashMap<CommandType, Callable<String>> map = new HashMap<>(){
        {
            put(CommandType.INFO, () -> info.execute(request));
            put(CommandType.SHOW, () -> show.execute(request));
            put(CommandType.ADD, () -> add.execute(request));
            put(CommandType.REMOVE_FIRST, () -> removeFirst.execute(request));
            put(CommandType.UPDATE, () -> update.execute(request));
            put(CommandType.REMOVE_BY_ID, () -> removeId.execute(request));
            put(CommandType.CLEAR, () -> clear.execute(request));
            put(CommandType.INSERT_AT, () -> insert.execute(request));
            put(CommandType.REMOVE_ANY_BY_FORM_OF_EDUCATION, () -> removeFormOfEducation.execute(request));
            put(CommandType.MIN_BY_COORDINATES, () -> minByCoordinates.execute(request));
            put(CommandType.PRINT_ASCENDING, () -> printAscending.execute(request));
            put(CommandType.CHECK_IS_INDEX_IN_RANGE, () -> isIdxInRange.execute(request));
            put(CommandType.CHECK_ID_EXISTS, () -> doesIdExists.execute(request));
            put(CommandType.EXIT, () -> exit.execute(request));
        }
    };
    public String executeCommand(Request request){
        this.request = request;
        try {
            return map.get(request.getCommandType()).call();
        } catch (Exception e){
            return e.getMessage();
        }

    }

}
