package client;

import commonData.commandData.CommandType;
import commonData.enums.AuthorizationType;
import commonData.modelHandlers.Respondent;
import commonData.requests.RequestUser;
import commonData.requests.interfaces.Request;
import lombok.Getter;

public class Authorization {
    AuthorizationType authorizationType;
    @Getter
    String login;
    String password;
    public void getAuthorizationTypeByUser(){
        Respondent.sendToOutput("You need to authorize! You can LOGIN or CREATE_NEW_ACCOUNT.");
        while (true){
            try {
                String input = Respondent.getInput().strip().toUpperCase();
                authorizationType = AuthorizationType.valueOf(input);
                return;
            } catch (Exception e){
                Respondent.sendToOutput("Choose [LOGIN, CREATE_NEW_ACCOUNT]");
            }
        }
    }
    public String checkUserInfo(Client client) {
        Request request = new RequestUser(CommandType.CHECK_USER_INFO, authorizationType, login, password);
        try {
            return client.dispatch(request);
        } catch (Exception e){
            return e.getMessage();
        }
    }
    public void authorize(){
        Respondent.sendToOutput("Enter the login.");
        if (authorizationType == AuthorizationType.CREATE_NEW_ACCOUNT){
            Respondent.sendToOutput("Login should be shorter than 20 characters and it can't contain spaces.");
        }
        while (true) {
            try {
                login = Respondent.getInput().strip();
                if (login.contains(" ")){
                    Respondent.sendToOutput("Login shouldn't contain spaces.");
                    continue;
                } else if (login.length() > 20){
                    Respondent.sendToOutput("Login should be shorter than 20 characters");
                    continue;
                } else if (login.isEmpty()){
                    Respondent.sendToOutput("Login can't be empty.");
                    continue;
                }
                break;
            } catch (Exception e){
                Respondent.sendToOutput(e.getMessage());
            }
        }
        Respondent.sendToOutput("Enter the password. It should be longer than 5 characters and it can't be empty.");
        if (authorizationType == AuthorizationType.CREATE_NEW_ACCOUNT){
            Respondent.sendToOutput("Password can't contain spaces");
        }
        while (true) {
            try {
                password = Respondent.getInput().strip();
                if (password.contains(" ")){
                    Respondent.sendToOutput("Password can't contain spaces.");
                    continue;
                } else if (password.length() > 255){
                    Respondent.sendToOutput("Password should be shorter than 255 characters.");
                    continue;
                } else if (password.length() < 6){
                    Respondent.sendToOutput("Password should be longer than 5 characters.");
                    continue;
                }
                break;
            } catch (Exception e){
                Respondent.sendToOutput(e.getMessage());
            }
        }
    }
}
