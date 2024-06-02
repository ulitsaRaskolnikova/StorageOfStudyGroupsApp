package commonData.data;

import commonData.data.enums.EyeColor;
import commonData.data.enums.HairColor;
import commonData.data.interfaces.XMLString;
import commonData.data.utils.InputField;
import commonData.data.validation.LowerLimit;
import commonData.data.validation.Min;
import commonData.data.validation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class Person implements XMLString, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
