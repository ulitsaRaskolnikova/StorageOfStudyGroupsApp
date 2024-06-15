package commonData.requests;

import commonData.commandData.CommandType;
import commonData.enums.AuthorizationType;
import lombok.Getter;
import lombok.Setter;

public class RequestUser extends RequestOnlyCommand {
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private AuthorizationType authorizationType;
    @Getter
    private String pepper;
    public RequestUser(CommandType commandType, AuthorizationType authorizationType, String login, String password) {
        super(commandType);
        super.setLogin(login);
        this.password = password;
        this.authorizationType = authorizationType;

    }
}
