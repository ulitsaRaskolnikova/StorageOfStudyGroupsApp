package server.model;

import commonData.data.StudyGroup;
import commonData.data.enums.FormOfEducation;
import commonData.data.interfaces.IHaveCoordinates;
import commonData.data.interfaces.IHaveFormOfEducation;
import commonData.data.interfaces.IHaveId;
import commonData.data.interfaces.XMLString;
import server.SQLController;
import server.model.interfaces.IStore;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static java.time.LocalDate.now;

/**
 *LinkedListStorage contains elements and execute some app commands.
 */
public class LinkedListStorage<E extends IHaveFormOfEducation & IHaveCoordinates & IHaveId & Comparable<E> & XMLString> implements IStore<E> {
    private final List<E> lst = new LinkedList<>();
    private final LocalDate date = now();
    private boolean success = true;
    private final ReentrantLock lock = new ReentrantLock();
    private final SQLController sqlController = new SQLController();
    public int getSize(){
        lock.lock();
        try {
            return lst.size();
        } finally {
            lock.unlock();
        }
    }
    @Override
    public String getXMLString(){
        lock.lock();
        try {
            StringBuilder result = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><listOfStudyGroups>");
            for (E element : lst) {
                result.append(element.toXMLString());
            }
            result.append("</listOfStudyGroups>");
            return result.toString();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean checkId(long id, String user){
        lock.lock();
        try {
            return lst.stream().anyMatch(x -> x.getId() == id && ((StudyGroup) x).getUser().equals(user));
        } finally {
            lock.unlock();
        }
        /*
        ListIterator<E> iterator = lst.listIterator(0);
        while (iterator.hasNext()){
            if (iterator.next().getId() == id){
                return true;
            }
        }
        return false;*/
    }
    @Override
    public boolean inRange(long id){
        lock.lock();
        try {
            return id <= lst.size();
        } finally {
            lock.unlock();
        }
    }
    @Override
    public String info() {
        lock.lock();
        try {
            return "Type of the collection: " + lst.getClass().getName() +
                    "\nType of an element: " + (lst.isEmpty() ? "None" : lst.get(0).getClass().getName()) +
                    "\nDate of initialization: " + date +
                    "\nSize: " + lst.size() + "\n";
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return lst.toString();
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void addOnly(E element){
        lock.lock();
        try {
            lst.add(element);
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void add(E element) throws SQLException {
        lock.lock();
        try {
            sqlController.add((StudyGroup) element);
            lst.add(element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(long id, E element, String user) throws SQLException, AccessPermissionException {
        lock.lock();
        try {
            E el = lst.stream().filter(x -> x.getId() == id).findAny().get();
            if (el != null) {
                if (!((StudyGroup) el).getUser().equals(user)){
                    throw new AccessPermissionException();
                }
                sqlController.update((StudyGroup) element);
                lst.remove(el);
                lst.add(element);
            }
        } finally {
            lock.unlock();
        }
//        ListIterator<E> iterator = lst.listIterator(0);
//        while (iterator.hasNext()){
//            if (iterator.next().getId() == id){
//                iterator.remove();
//                iterator.add(element);
//                break;
//            }
//        }
    }

    @Override
    public void remove(long id, String user) throws SQLException, AccessPermissionException {
        lock.lock();
        try{
            E el = lst.stream().filter(x -> x.getId() == id).findAny().get();
            if (el != null) {
                if (!((StudyGroup) el).getUser().equals(user)){
                    throw new AccessPermissionException();
                }
                sqlController.remove(id);
                lst.remove(el);
            }
        } finally {
            lock.unlock();
        }
//        ListIterator<E> iterator = lst.listIterator(0);
//        while (iterator.hasNext()){
//            if (iterator.next().getId() == id){
//                iterator.remove();
//                break;
//            }
//        }
    }

    @Override
    public void clear(String user) throws SQLException {
        lock.lock();
        try {
            sqlController.removeUserObjects(user);
            lst.removeIf(x -> ((StudyGroup) x).getUser().equals(user));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void insert(int idx, E element) throws SQLException {
        lock.lock();
        try {
            sqlController.add((StudyGroup) element);
            lst.add(idx, element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeFirst(String user) throws SQLException, AccessPermissionException {
        lock.lock();
        try {
            if (!((StudyGroup) lst.get(0)).getUser().equals(user)){
                throw new AccessPermissionException();
            }
            if (lst.size() > 0){
                sqlController.remove(lst.get(0).getId());
                lst.remove(0);
            }
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void removeAnyByFormOfEducation(FormOfEducation formOfEducation, String user) throws SQLException {
        lock.lock();
        try {
            setSuccess(false);
            var el = lst.stream().filter(x -> (x.getFormOfEducation() == formOfEducation) && ((StudyGroup) x).getUser().equals(user)).findAny().get();
            if (el != null) {
                setSuccess(true);
                sqlController.remove(el.getId());
                lst.remove(el);
            }
        } finally {
            lock.unlock();
        }

//        ListIterator<E> iterator = lst.listIterator();
//        setSuccess(false);
//        while (iterator.hasNext()){
//            if (iterator.next().getFormOfEducation() == formOfEducation){
//                iterator.remove();
//                setSuccess(true);
//                break;
//            }
//        }
    }
    @Override
    public boolean getSuccess(){
        return success;
    }
    private void setSuccess(boolean success){
        this.success = success;
    }

    @Override
    public E minByCoordinates() {
        lock.lock();
        try {
            E minCoordinatesElement = lst.stream().min(Comparator.comparing(IHaveCoordinates::getCoordinates)).get();
//        int xMaxValue = 274;
//        var minCoordinates = new Coordinates(xMaxValue, Double.MAX_VALUE);
//        ListIterator<E> iterator = lst.listIterator();
//        while (iterator.hasNext()){
//            var currentElement = iterator.next();
//            if (minCoordinates.compareTo(currentElement.getCoordinates()) >= 0){
//                minCoordinates = currentElement.getCoordinates();
//                minCoordinatesElement = currentElement;
//            }
//        }
            return minCoordinatesElement;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void sort(){
        lock.lock();
        try {
            Collections.sort(lst);
        } finally {
            lock.unlock();
        }
    }
}
