package commonData.modelHandlers;

import commonData.data.validation.*;
import commonData.commandData.CommandType;
import commonData.data.enums.FormOfEducation;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * Handler validate input and parse commands and fields.
 */
public class Handler {
    public static boolean isValidCommandInput(String input){
        String[] sep = input.strip().split("\s+", 2);
        if (isCommand(sep[0])){
            CommandType commandType = CommandType.valueOf(sep[0].toUpperCase());
            switch (commandType){
                case INSERT_AT: return sep.length == 2 && isIndex(sep[1].strip());
                case UPDATE, REMOVE_BY_ID: return sep.length == 2 && isId(sep[1].strip());
                case REMOVE_ANY_BY_FORM_OF_EDUCATION: return sep.length == 2 && isFormOfEducation(sep[1].strip());
                case EXECUTE_SCRIPT: return sep.length == 2 && isScriptName(sep[1].strip());
            }
            if (sep.length == 1) return true;
        }
        return false;
    }
    public static String parseScriptName(String input){
        String[] sep = input.strip().split("\s+", 2);
        return sep[1].strip();
    }
    public static long parseId(String input){
        String[] sep = input.strip().split("\s+", 2);
        return Long.parseLong(sep[1].strip());
    }
    public static int parseIndex(String input){
        String[] sep = input.strip().split("\s+", 2);
        return Integer.parseInt(sep[1].strip());
    }
    public static FormOfEducation parseFormOfEducation(String input){
        String[] sep = input.strip().split("\s+", 2);
        return FormOfEducation.valueOf(sep[1].strip());
    }
    public static CommandType parseCommandType(String input){
        String[] sep = input.strip().split("\s+", 2);
        return CommandType.valueOf(sep[0].toUpperCase());
    }
    public static boolean isScriptName(String input){
        File file = new File(input);
        return file.exists();
    }
    public static boolean isLong(String input){
        input = input.strip();
        return isId(input) || input.equals("0") ||
                (input.matches("-[123456789]\\d+") && (isId(input.substring(1))
                        || input.substring(1).equals(maxLongStr)));
    }
    private final static String maxIntStr = String.valueOf(Integer.MAX_VALUE);
    private final static int maxIntLen = maxIntStr.length();
    public static boolean isIndex(String input){
        input = input.strip();
        return input.matches("(0|[123456789]\\d*)") &&
                (input.length() < maxIntLen ||
                        (input.length() == maxIntLen && input.compareTo(maxIntStr) < 0));
    }
    private final static String maxLongStr = String.valueOf(Long.MAX_VALUE);
    private final static int maxLongLen = maxLongStr.length();
    public static boolean isId(String input){
        input = input.strip();
        return input.matches("[123456789]\\d*") &&
                (input.length() < maxLongLen ||
                        (input.length() == maxLongLen && input.compareTo(maxLongStr) <= 0));
    }
    public static boolean isFormOfEducation(String input){
        return Arrays.stream(FormOfEducation.values()).anyMatch(element -> Objects.equals(input.strip(), element.toString()));
    }
    public static boolean isCoordinatesX(String input){
        return isInteger(input) && Integer.parseInt(input) <= 724;
    }
    public static boolean isInteger(String input){
        input = input.strip();
        if (input.matches("-?[123456789]\\d*")){
            if (input.matches("-[123456789]\\d*")){
                input = input.substring(1);
            }
            return (input.length() < maxIntLen ||
                    (input.length() == maxIntLen && input.compareTo(maxIntStr) <= 0));
        }
        return input.equals("0");
    }
    public static boolean isCommand(String input){
        return Arrays.stream(CommandType.values()).anyMatch(element -> Objects.equals(input.strip(), element.getName()));
    }
    public static <T> boolean isValidData(String input, String name, T parent) throws NoSuchFieldException {
        Field field = parent.getClass().getDeclaredField(name);
        if (field.isAnnotationPresent(NotNull.class) && input.isEmpty()) return false;
        try{
            PrimitiveTypeConverter.castObject(field.getType(), input);
        } catch (Throwable e){
            return false;
        }
        double d;
        if (field.isAnnotationPresent(Min.class)) {
            if (getLongObj(field, input) < field.getAnnotation(Min.class).value()) return false;
        }
        if (field.isAnnotationPresent(Max.class)){
            if (getLongObj(field, input) > field.getAnnotation(Max.class).value()) return false;
        }
        if (field.isAnnotationPresent(LowerLimit.class)){
            d = Double.valueOf(input);
            if (Double.compare(d, field.getAnnotation(LowerLimit.class).value()) <= 0) return false;
        }
        if (field.isAnnotationPresent(UpperLimit.class)){
            d = Double.valueOf(input);
            if (Double.compare(d, field.getAnnotation(UpperLimit.class).value()) >= 0) return false;
        }
        return true;
    }
    public static long getLongObj(Field field, String input){
        if (field.getType().equals(String.class)){
            return Long.valueOf(input.length());
        }
        return Long.parseLong(input.strip());
    }
}
