package server.model.interfaces;

import commonData.data.enums.FormOfEducation;
import commonData.data.interfaces.IHaveCoordinates;
import commonData.data.interfaces.IHaveFormOfEducation;
import commonData.data.interfaces.IHaveId;
import commonData.data.interfaces.XMLString;
import server.model.AccessPermissionException;

import java.sql.SQLException;

public interface IStore<E extends IHaveFormOfEducation & IHaveCoordinates & IHaveId & Comparable<E> & XMLString> {
     String info();

     String toString();
     String getXMLString();

     void add(E element) throws SQLException;
     void addOnly(E element);

     void update(long id, E element, String user) throws SQLException, AccessPermissionException;

     void remove(long id, String user) throws SQLException, AccessPermissionException;

     void clear(String user) throws SQLException;

     void insert(int idx, E element) throws SQLException;

     void removeFirst(String user) throws SQLException, AccessPermissionException;

     void removeAnyByFormOfEducation(FormOfEducation formOfEducation, String user) throws SQLException;

     E minByCoordinates();
     void sort();
     int getSize();
     boolean checkId(long id, String user);
     boolean inRange(long id);
     boolean getSuccess();
}







