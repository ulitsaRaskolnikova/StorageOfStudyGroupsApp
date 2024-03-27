package model.commands;

import lombok.Getter;
import model.commands.appCommands.HistoryCommand;
import requests.interfaces.*;

import java.lang.reflect.Field;
import java.util.HashMap;

import static model.commands.CommandType.*;

/**
 * CommandExecutor executes app commands.
 */
public class CommandExecutor {
    private Request request = null;
    private Command help;
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
    private Command save;
    private Command executeScript;
    @Getter
    private HistoryCommand history;
    public CommandExecutor(
            Command help,
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
            Command save,
            Command executeScript,
            HistoryCommand history
    ){
        this.help = help;
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
        this.save = save;
        this.executeScript = executeScript;
        this.history = history;
    }
    private final HashMap<CommandType, Runnable> map = new HashMap<CommandType, Runnable>(){
        {
            put(HELP, () -> {
                help.execute(request);
            });
            put(INFO, () -> {
                info.execute(request);
            });
            put(SHOW, () -> {
                show.execute(request);
            });
            put(ADD, () -> {
                add.execute(request);
            });
            put(REMOVE_FIRST, () -> {
                removeFirst.execute(request);
            });
            put(PRINT_ASCENDING, () -> {
                printAscending.execute(request);
            });
            put(UPDATE, () -> {
                update.execute(request);
            });
            put(REMOVE_BY_ID, () -> {
                removeId.execute(request);
            });
            put(CLEAR, () -> {
                clear.execute(request);
            });
            put(INSERT_AT, () -> {
                insert.execute(request);
            });
            put(REMOVE_ANY_BY_FORM_OF_EDUCATION, () -> {
                removeFormOfEducation.execute(request);
            });
            put(MIN_BY_COORDINATES, () -> {
                minByCoordinates.execute(request);
            });
            put(PRINT_ASCENDING, () -> {
                printAscending.execute(request);
            });
            put(SAVE, () -> {
                save.execute(request);
            });
            put(EXECUTE_SCRIPT, () -> {
                executeScript.execute(request);
            });
            put(HISTORY, () -> {
                history.execute(request);
            });
        }
    };
    public void executeCommand(Request request){
        this.request = request;
        map.get(request.getCommandType()).run();
    }

}
