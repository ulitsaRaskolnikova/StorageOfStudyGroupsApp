package controller.exceptions;

public class WrongTagException extends Exception {

    public WrongTagException() {
    }

    public WrongTagException(String message) {
        super(message);
    }

    public WrongTagException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongTagException(Throwable cause) {
        super(cause);
    }

    public WrongTagException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
