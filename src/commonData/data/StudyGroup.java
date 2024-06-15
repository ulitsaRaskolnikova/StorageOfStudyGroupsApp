package commonData.data;

import commonData.data.enums.FormOfEducation;
import commonData.data.enums.Semester;
import commonData.data.interfaces.Element;
import commonData.data.interfaces.IHaveId;
import commonData.data.utils.IdMaker;
import commonData.data.utils.InputField;
import commonData.data.validation.Min;
import commonData.data.validation.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * StudyGroup is a data element.
 */

@Getter
@Setter
public class StudyGroup implements Element, IHaveId, Comparable<StudyGroup>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Min(value = 1)
    @NotNull
    private Long id = IdMaker.getId(); //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @InputField
    @NotNull

    private String name; //Поле не может быть null, Строка не может быть пустой
    @InputField
    @NotNull
    private Coordinates coordinates; //Поле не может быть null
    @NotNull
    private LocalDate creationDate = LocalDate.now(); //поле не может быть null, Значение этого поля должно генерироваться автоматически
    @InputField
    @Min(value = 1)
    private int studentsCount; //Значение поля должно быть больше 0
    @InputField
    private FormOfEducation formOfEducation; //Поле может быть null
    @InputField
    @NotNull
    private Semester semesterEnum; //Поле не может быть null
    @InputField
    private Person groupAdmin; //Поле может быть null
    private String user;
    public void setId(Long id){
        this.id = id;
    }
    @Override
    public int compareTo(StudyGroup studyGroup){
        return Long.compare(id, studyGroup.getId());
    }
    @Override
    public String toString(){
        return getClass().getName()
                + "{id=" + id
                + ",name=" + name
                + ",coordinates=" + coordinates
                + ",creationDate=" + creationDate
                + ",studentCount=" + studentsCount
                + ",formOfEducation=" + formOfEducation
                + ",semesterEnum=" + semesterEnum
                + ",groupAdmin=" + groupAdmin
                + ",user=" + user + "}";
    }

    @Override
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    @Override
    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }
    @Override
    public void setId(long id){
        this.id = id;
    }
    @Override
    public long getId() {
        return id;
    }
    public String toXMLString(){
        return "<studyGroup>" +
                "<name>" + name + "</name>" +
                coordinates.toXMLString() +
                "<studentsCount>" + studentsCount + "</studentsCount>" +
                "<formOfEducation>" + (formOfEducation == null ? "" : formOfEducation) + "</formOfEducation>" +
                "<semesterEnum>" + semesterEnum + "</semesterEnum>" +
                (groupAdmin == null ? "<groupAdmin></groupAdmin>" : groupAdmin.toXMLString()) +
                "<user>" + user + "</user>" +
                "</studyGroup>";
    }
}