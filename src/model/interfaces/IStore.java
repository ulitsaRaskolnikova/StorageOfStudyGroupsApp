package model.interfaces;

import model.data.enums.FormOfEducation;
import model.data.interfaces.IHaveCoordinates;
import model.data.interfaces.IHaveFormOfEducation;
import model.data.interfaces.IHaveId;
import model.data.interfaces.XMLString;

import java.util.LinkedList;

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
     boolean getSuccess();
}







