package commonData.commandData;

/**
 * CommandType contains description and other name of commands.
 */

public enum CommandType {
    HELP("help", "help : вывести справку по доступным командам"),
    INFO("info", "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)"),
    SHOW("show", "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении"),
    ADD("add", "add {element} : добавить новый элемент в коллекцию"),
    UPDATE("update", "update id {element} : обновить значение элемента коллекции, id которого равен заданному"),
    REMOVE_BY_ID("remove_by_id", "remove_by_id id : удалить элемент из коллекции по его id"),
    CLEAR("clear", "clear : очистить коллекцию"),
    EXIT("exit", "exit : сохранить коллекцию в файл и закончить программу"),
    EXECUTE_SCRIPT("execute_script", "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме."),
    INSERT_AT("insert_at", "insert_at index {element} :добавить новый элемент в заданную позицию"),
    REMOVE_FIRST("remove_first", "remove_first : удалить первый элемент из коллекции"),
    HISTORY("history", "history :вывести последние 12 команд (без их аргументов)"),
    REMOVE_ANY_BY_FORM_OF_EDUCATION("remove_any_by_form_of_education", "remove_any_by_form_of_education form0fEducation : удалить из коллекции один элемент, значение поля formOfEducation которого эквивалентно заданному"),
    MIN_BY_COORDINATES("min_by_coordinates", "min_by_coordinates : вывести любой объект из коллекции, значение поля coordinates которого является минимальным"),
    PRINT_ASCENDING("print_ascending", "print_ascending : вывести элементы коллекции в порядке возрастания"),
    @UtilCommand
    CHECK_IS_INDEX_IN_RANGE("check_is_index_in_range", "check_is_index_in_range : checks index is in range"),
    @UtilCommand
    CHECK_ID_EXISTS("check_id_exists", "check_id_exists : checks id exists");
    private final String name;
    private final String description;
    private CommandType(String name, String description){
        this.name = name;
        this.description = description;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
}
