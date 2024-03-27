package requests;

import model.commands.CommandType;
import model.data.enums.FormOfEducation;
import requests.interfaces.IRequestFormOfEducation;

public class RequestFormOfEducation extends RequestOnlyCommand implements IRequestFormOfEducation {
    FormOfEducation formOfEducation;
    public RequestFormOfEducation(CommandType commandType, FormOfEducation formOfEducation) {
        super(commandType);
        this.formOfEducation = formOfEducation;
    }

    @Override
    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    @Override
    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }
}
