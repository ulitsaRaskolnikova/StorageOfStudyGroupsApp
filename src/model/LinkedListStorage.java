package model;

import model.data.Coordinates;
import model.data.StudyGroup;
import model.data.enums.FormOfEducation;
import model.data.interfaces.*;
import model.interfaces.IStore;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static java.time.LocalDate.now;

/**
 *LinkedListStorage contains elements and execute some app commands.
 */
public class LinkedListStorage<E extends IHaveFormOfEducation & IHaveCoordinates & IHaveId & Comparable<E> & XMLString> implements IStore<E> {
    private final List<E> lst = new LinkedList<>();
    private final LocalDate date = now();
    private boolean success = true;
    public int getSize(){
        return lst.size();
    }

    @Override
    public String getXMLString(){
        StringBuilder result = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><listOfStudyGroups>");
        for (E element : lst){
            result.append(element.toXMLString());
        }
        result.append("</listOfStudyGroups>");
        return result.toString();
    }

    @Override
    public boolean checkId(long id){
        ListIterator<E> iterator = lst.listIterator(0);
        while (iterator.hasNext()){
            if (iterator.next().getId() == id){
                return true;
            }
        }
        return false;
    }
    @Override
    public String info() {
        return "Type of the collection: " + lst.getClass().getName() +
                "\nType of an element: " + (lst.isEmpty() ? "None" : lst.get(0).getClass().getName()) +
                "\nDate of initialization: " + date +
                "\nSize: " + lst.size() + "\n";
    }

    @Override
    public String toString() {
        return lst.toString();
    }

    @Override
    public void add(E element) {
        lst.add(element);
    }

    @Override
    public void update(long id, E element) {
        ListIterator<E> iterator = lst.listIterator(0);
        while (iterator.hasNext()){
            if (iterator.next().getId() == id){
                iterator.remove();
                iterator.add(element);
                break;
            }
        }
    }

    @Override
    public void remove(long id) {
        ListIterator<E> iterator = lst.listIterator(0);
        while (iterator.hasNext()){
            if (iterator.next().getId() == id){
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void clear() {
        lst.clear();
    }

    @Override
    public void insert(int idx, E element) {
        lst.add(idx, element);
    }

    @Override
    public void removeFirst() {
        if (lst.size() > 0){
            lst.remove(0);
        }
    }

    @Override
    public void removeAnyByFormOfEducation(FormOfEducation formOfEducation) {
        ListIterator<E> iterator = lst.listIterator();
        setSuccess(false);
        while (iterator.hasNext()){
            if (iterator.next().getFormOfEducation() == formOfEducation){
                iterator.remove();
                setSuccess(true);
                break;
            }
        }
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
        int xMaxValue = 274;
        var minCoordinates = new Coordinates(xMaxValue, Double.MAX_VALUE);
        E minCoordinatesElement = null;
        ListIterator<E> iterator = lst.listIterator();
        while (iterator.hasNext()){
            var currentElement = iterator.next();
            if (minCoordinates.compareTo(currentElement.getCoordinates()) >= 0){
                minCoordinates = currentElement.getCoordinates();
                minCoordinatesElement = currentElement;
            }
        }
        return minCoordinatesElement;
    }
    @Override
    public void sort(){
        Collections.sort(lst);
    }
}
