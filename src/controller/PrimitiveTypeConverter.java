package controller;

import controller.enums.MessageType;
import controller.exceptions.WrongDataInputException;
import model.data.utils.IdMaker;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.valueOf;

/**
 * PrimitiveTypeConverter converts primitive types.
 */
public class PrimitiveTypeConverter {
    private static final Map<Class<?>, Class<?>> WRAPPER_TYPE_MAP;
    static {
        WRAPPER_TYPE_MAP = new HashMap<Class<?>, Class<?>>(16);
        WRAPPER_TYPE_MAP.put(Integer.class, int.class);
        WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
        WRAPPER_TYPE_MAP.put(Character.class, char.class);
        WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
        WRAPPER_TYPE_MAP.put(Double.class, double.class);
        WRAPPER_TYPE_MAP.put(Float.class, float.class);
        WRAPPER_TYPE_MAP.put(Long.class, long.class);
        WRAPPER_TYPE_MAP.put(Short.class, short.class);
        WRAPPER_TYPE_MAP.put(Void.class, void.class);
    }
    public static <T> boolean isWrapper(Class<T> cl){
        return WRAPPER_TYPE_MAP.containsKey(cl);
    }
    public static boolean isSimple(Class<?> cl){
        return cl.isPrimitive() || cl.isEnum() || isWrapper(cl) || isString(cl) || cl.equals(LocalDate.class);
    }
    public static <T> boolean isString(Class<T> cl){
        return cl.equals(String.class);
    }
    public static <T> T convertObj(Class<T> objType, String obj, String name, String parentObjName) throws ReflectiveOperationException, WrongDataInputException {
        while (!Handler.isValidData(obj, name, parentObjName)){
            switch (Respondent.getInputType()){
                case VIEW:
                    Respondent.sendToOutput(MessageType.WRONG_DATA_INPUT.getMessage() + " in " + name + "\n");
                    Respondent.sendToOutput(name + ": ");
                    obj = Respondent.getInput();
                    break;
                case XML_FILE:
                    throw new WrongDataInputException(MessageType.WRONG_DATA_INPUT.getMessage() + " in " + name);
            }
        }
        if (name.equals("id")){
            IdMaker.setId(Math.max(IdMaker.getCurrentId(), Long.parseLong(obj)));
        }
        switch (objType.getSimpleName()){
            case "Integer", "int": return (T) Integer.valueOf(obj);
            case "Long", "long": return (T) Long.valueOf(obj);
            case "Double", "double": return (T) Double.valueOf(obj);
            case "Float", "float": return (T) Float.valueOf(obj);
            case "String": return Objects.equals(obj, "") ? null : (T) obj;
            case "LocalDate": return (T) LocalDate.parse(obj);
        }
        if (objType.isEnum()){
            return Objects.equals(obj, "") ? null : (T) Enum.valueOf((Class<Enum>) objType, obj);
        }
        return null;
    }
}
