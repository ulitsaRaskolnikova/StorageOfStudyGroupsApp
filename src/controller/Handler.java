package controller;

import fileSystem.ScriptHandler;
import model.commands.CommandType;
import model.data.enums.EyeColor;
import model.data.enums.FormOfEducation;
import model.data.enums.HairColor;
import model.data.enums.Semester;
import requests.RequestFormOfEducation;
import requests.RequestId;
import requests.RequestIndexElement;
import requests.RequestOnlyCommand;
import requests.interfaces.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

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
    public static boolean isSemesterEnum(String input){
        return Arrays.stream(Semester.values()).anyMatch(element -> Objects.equals(input.strip(), element.toString()));
    }
    public static boolean isEyeColor(String input){
        return Arrays.stream(EyeColor.values()).anyMatch(element -> Objects.equals(input.strip(), element.toString()));
    }
    public static boolean isHairColor(String input){
        return Arrays.stream(HairColor.values()).anyMatch(element -> Objects.equals(input.strip(), element.toString()));
    }
    public static boolean isCommand(String input){
        return Arrays.stream(CommandType.values()).anyMatch(element -> Objects.equals(input.strip(), element.getName()));
    }
    public static boolean isLocalDate(String input){
        Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        return DATE_PATTERN.matcher(input).matches();
    }
    public static boolean isValidData(String input, String name, String parentObjName){
        boolean isValid = false;
        //System.out.println(parentObjName);
        //System.out.println(name + " " + input);
        switch (parentObjName){
            case "StudyGroup":
                switch (name){
                    case "id": return isId(input);
                    case "creationDate": return isLocalDate(input);
                    case "name": return !Objects.equals(input, "");
                    case "studentsCount": return isId(input) || Objects.equals(input, maxIntStr);
                    case "formOfEducation": return Objects.equals(input, "") || isFormOfEducation(input);
                    case "semesterEnum": return isSemesterEnum(input);
                }
                return false;
            case "Coordinates":
                switch (name){
                    case "x": return isCoordinatesX(input);
                    case "y":
                        try{
                            Double.parseDouble(input);
                        } catch(NumberFormatException e){
                            return false;
                        }
                        return true;
                }
                return false;
            case "Person":
                switch (name){
                    case "name": return !Objects.equals(input, "");
                    case "weight":
                        try{
                            Float num = Float.parseFloat(input);
                            return Float.compare(num, 0) > 0;
                        } catch(NumberFormatException e){
                            return false;
                        }
                    case "eyeColor": return Objects.equals(input, "") || isEyeColor(input);
                    case "hairColor": return isHairColor(input);
                }
                return false;
            case "Location":
                switch (name){
                    case "x": return isLong(input);
                    case "y": return isInteger(input);
                    case "z": return isInteger(input);
                    case "name": return input.length() <= 454;
                }

        }
        return true;
    }
}
