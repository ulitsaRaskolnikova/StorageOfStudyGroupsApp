package commonData.data;

import commonData.data.interfaces.XMLString;
import commonData.data.utils.InputField;
import commonData.data.validation.Max;
import commonData.data.validation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class Location implements XMLString, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
