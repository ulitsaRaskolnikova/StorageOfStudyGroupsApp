package model.data;

import lombok.*;
import lombok.NonNull;
import model.data.enums.FormOfEducation;
import model.data.enums.Semester;
import model.data.interfaces.Element;
import model.data.interfaces.IHaveId;
import model.data.utils.IdMaker;
import model.data.utils.InputField;

import java.time.LocalDate;

/**
 * StudyGroup is a data element.
 */

@Getter
@Setter
public class StudyGroup implements Element, IHaveId, Comparable<StudyGroup> {
    {
        id = IdMaker.getId();
        creationDate = LocalDate.now();
    }
    public void setId(Long id){
        this.id = id;
    }
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @InputField
    private String name; //Поле не может быть null, Строка не может быть пустой
    @InputField
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //поле не может быть null, Значение этого поля должно генерироваться автоматически
    @InputField
    private int studentsCount; //Значение поля должно быть больше 0
    @InputField
    private FormOfEducation formOfEducation; //Поле может быть null
    @InputField
    private Semester semesterEnum; //Поле не может быть null
    @InputField
    private Person groupAdmin; //Поле может быть null
    @Override
    public int compareTo(StudyGroup studyGroup){
        return Long.compare(id, studyGroup.getId());
    }
    @Override
    public String toString(){
        return getClass().getName()
                + "[id=" + id
                + ",name=" + name
                + ",coordinates=" + coordinates
                + ",creationDate=" + creationDate
                + ",studentCount=" + studentsCount
                + ",formOfEducation=" + formOfEducation
                + ",semesterEnum=" + semesterEnum
                + ",groupAdmin=" + groupAdmin + "]";
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
                "<id>" + id + "</id>" +
                "<name>" + name + "</name>" +
                coordinates.toXMLString() +
                "<creationDate>" + creationDate + "</creationDate>" +
                "<studentsCount>" + studentsCount + "</studentsCount>" +
                "<formOfEducation>" + (formOfEducation == null ? "" : formOfEducation) + "</formOfEducation>" +
                "<semesterEnum>" + semesterEnum + "</semesterEnum>" +
                (groupAdmin == null ? "<groupAdmin></groupAdmin>" : groupAdmin.toXMLString()) +
                "</studyGroup>";
    }
}