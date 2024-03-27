package model.commands;

import requests.interfaces.Request;

public interface Command<T extends Request> {
    void execute (T request);
}
