package commonData.enums;

/**
 * MessageType contains types of a message and it's text.
 */

public enum MessageType {
    ENTER_ID("\tid:"),
    WRONG_COMMAND_INPUT("Invalid command or data!\n"),
    WRONG_DATA_INPUT("Invalid data"),
    OUT_OF_RANGE("Index is out of range!\n"),
    NO_SUCH_ID("There isn't element with such id in the collection!\n"),
    NO_SUCH_FORM_OF_EDUCATION("There isn't element with such form of education!\n"),
    INVALID_XML_TAG("Invalid xml tag"),
    PARSE_FAIL("File wasn't parsed because of the following mistake:");
    private final String message;
    private MessageType(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
