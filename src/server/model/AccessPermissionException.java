package server.model;

public class AccessPermissionException extends Exception{
    public AccessPermissionException(){
        super("You can't update or remove other's object.");
    }
    public AccessPermissionException(String message){
        super(message);
    }
}
