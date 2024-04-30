package model.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.data.enums.EyeColor;
import model.data.enums.HairColor;
import model.data.interfaces.XMLString;
import model.data.utils.InputField;
import model.data.validation.LowerLimit;
import model.data.validation.Min;
import model.data.validation.NotNull;

@Getter
@Setter
@ToString
public class Person implements XMLString {
    @InputField
    @NotNull
    @Min(value = 1)
    private String name; //Поле не может быть null, Строка не может быть пустой
    @InputField
    @NotNull
    @LowerLimit(value = 0)
    private Float weight; //Поле не может быть null, Значение поля должно быть больше 0
    @InputField
    private EyeColor eyeColor; //Поле может быть null
    @InputField
    @NotNull
    private HairColor hairColor; //Поле не может быть null
    @InputField
    @NotNull
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
