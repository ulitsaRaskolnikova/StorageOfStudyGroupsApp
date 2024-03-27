package model.data.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * IdMaker makes new individual id.
 */
public class IdMaker {
    @Setter
    private static long id = 0;
    public static long getId(){
        id++;
        return id;
    }
    public static long getCurrentId(){
        return id;
    }
}
