package model.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.data.enums.EyeColor;
import model.data.enums.HairColor;
import model.data.interfaces.XMLString;
import model.data.utils.InputField;

@Getter
@Setter
@ToString
public class Person implements XMLString {
    @InputField
    private String name; //Поле не может быть null, Строка не может быть пустой
    @InputField
    private Float weight; //Поле не может быть null, Значение поля должно быть больше 0
    @InputField
    private EyeColor eyeColor; //Поле может быть null
    @InputField
    private HairColor hairColor; //Поле не может быть null
    @InputField
    private Location location; //Поле не может быть null
    @Override
    public String toXMLString(){
        return "<groupAdmin>" +
                "<name>" + name + "</name>" +
                "<weight>" + weight + "</weight>" +
                "<eyeColor>" + (eyeColor == null ? "" : eyeColor) + "</eyeColor>" +
                "<hairColor>" + hairColor + "</hairColor>" +
                location.toXMLString() +
                "</groupAdmin>";
    }
}
