package commonData.requests.interfaces;

import commonData.data.enums.FormOfEducation;

public interface IRequestFormOfEducation extends Request {
    void setFormOfEducation(FormOfEducation formOfEducation);
    FormOfEducation getFormOfEducation();
}
