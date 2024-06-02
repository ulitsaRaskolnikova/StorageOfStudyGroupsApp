package commonData.data;

import commonData.data.interfaces.XMLString;
import commonData.data.utils.InputField;
import commonData.data.validation.Max;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@ToString
public class Coordinates implements Comparable<Coordinates>, XMLString, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @InputField
    @Max(value = 724)
    private int x; //Максимальное значение поля: 724
    @InputField
    private double y;
    public Coordinates(){
        this.x = 0;
        this.y = 0;
    }
    public Coordinates(int x, double y){
        this.x = x;
        this.y = y;
    }
    @Override
    public int compareTo(Coordinates coordinates){
        if(x < coordinates.getX()){
            return -1;
        }
        else if(x > coordinates.getX()){
            return 1;
        }
        else{
            return Double.compare(y, coordinates.getY());
        }
    }
    @Override
    public String toXMLString(){
        return "<coordinates>" +
                "<x>" + x + "</x>" +
                "<y>" + y + "</y>" +
                "</coordinates>";
    }
}
