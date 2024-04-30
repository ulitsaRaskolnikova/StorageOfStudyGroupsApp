package controller;

import controller.enums.InputType;
import controller.enums.MessageType;
import controller.exceptions.WrongDataInputException;
import controller.exceptions.WrongTagException;
import fileSystem.XMLHandler;
import model.data.utils.InputField;
import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * ElementBuilderHelper creates new element due reflection by user's, xml and script input.
 */

public class ElementBuilderHelper {
    public static <T, E> T buildElement(Class<T> cl, String name, E parent) throws WrongDataInputException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ReflectiveOperationException, WrongTagException {
        Node node;
        if (Respondent.getInputType() == InputType.XML_FILE){
            node = XMLHandler.getNextNode();
            if (!node.getNodeName().equals(name)) throw new WrongTagException(MessageType.INVALID_XML_TAG.getMessage() + " " + node.getNodeName());
            if (name.equals("groupAdmin") && node.getTextContent().equals("")){
                return null;
            }
        }
        if (PrimitiveTypeConverter.isSimple(cl)){
            String input = Respondent.getInput();
            if (cl.isEnum()){
                input = input.toUpperCase();
            }
            return PrimitiveTypeConverter.convertObj(cl, input, name, parent);
        }

        Field[] fields = cl.getDeclaredFields();
        T obj = cl.getConstructor().newInstance();

        for (Field field : fields){
            if (!(field.isAnnotationPresent(InputField.class) || Respondent.getInputType() == InputType.XML_FILE)){
                continue;
            }

            Method method = cl.getMethod(getSetterName(field.getName()), field.getType());
            boolean isSkip = false;
            if (Respondent.getInputType() == InputType.VIEW){
                Respondent.sendToOutput(field.getName() +
                        (field.getType().isEnum() ? " " + Arrays.toString(field.getType().getEnumConstants()) : "") +
                        ": " + (PrimitiveTypeConverter.isSimple(field.getType()) ? "" : "\n"));
            }
            if (field.getName().equals("groupAdmin") && Respondent.getInputType() == InputType.VIEW){
                Respondent.sendToOutput("Press enter to skip this field\n");
                String input = Respondent.getInput();
                if (input.isEmpty()){
                    isSkip = true;
                }
            }
            if (!isSkip) method.invoke(obj, buildElement(field.getType(), field.getName(), obj));
        }
        return obj;
    }
    private static String getSetterName(String variableName){
        return "set" + variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
    }
}
