package model.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.data.interfaces.XMLString;
import model.data.utils.InputField;
import model.data.validation.Max;
import model.data.validation.NotNull;

@Getter
@Setter
@ToString
public class Location implements XMLString {
    @InputField
    private long x;
    @InputField
    @NotNull
    private Integer y; //Поле не может быть null
    @InputField
    @NotNull
    private Integer z; //Поле не может быть null
    @InputField
    @Max(value = 454)
    private String name; //Длина строки не должна быть больше 454, Поле может быть null
    @Override
    public String toXMLString(){
        return "<location>" +
                "<x>" + x + "</x>" +
                "<y>" + y + "</y>" +
                "<z>" + z + "</z>" +
                "<name>" + name + "</name>" +
                "</location>";
    }
}
