package requests.interfaces;

import model.data.enums.FormOfEducation;

public interface IRequestFormOfEducation extends Request {
    void setFormOfEducation(FormOfEducation formOfEducation);
    FormOfEducation getFormOfEducation();
}
