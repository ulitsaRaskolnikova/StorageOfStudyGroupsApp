package commonData.commandData;

import commonData.requests.interfaces.Request;

import java.security.NoSuchAlgorithmException;

public interface Command<T extends Request> {
    String execute (T request) throws NoSuchAlgorithmException;
}
