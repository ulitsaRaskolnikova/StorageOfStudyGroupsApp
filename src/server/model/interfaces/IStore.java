package server.model.interfaces;

import commonData.data.enums.FormOfEducation;
import commonData.data.interfaces.IHaveCoordinates;
import commonData.data.interfaces.IHaveFormOfEducation;
import commonData.data.interfaces.IHaveId;
import commonData.data.interfaces.XMLString;

public interface IStore<E extends IHaveFormOfEducation & IHaveCoordinates & IHaveId & Comparable<E> & XMLString> {
     String info();

     String toString();
     String getXMLString();

     void add(E element);

     void update(long id, E element);

     void remove(long id);

     void clear();

     void insert(int idx, E element);

     void removeFirst();

     void removeAnyByFormOfEducation(FormOfEducation formOfEducation);

     E minByCoordinates();
     void sort();
     int getSize();
     boolean checkId(long id);
     boolean inRange(long id);
     boolean getSuccess();
}







