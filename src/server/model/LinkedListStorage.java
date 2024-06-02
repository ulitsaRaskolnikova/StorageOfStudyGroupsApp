package server.model;

import commonData.data.Coordinates;
import commonData.data.enums.FormOfEducation;
import commonData.data.interfaces.IHaveCoordinates;
import commonData.data.interfaces.IHaveFormOfEducation;
import commonData.data.interfaces.IHaveId;
import commonData.data.interfaces.XMLString;
import server.model.interfaces.IStore;

import java.time.LocalDate;
import java.util.*;

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
    public boolean inRange(long id){
        return id <= lst.size();
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
        E el = lst.stream().filter(x -> x.getId() == id).findAny().get();
        if (el != null) {
            lst.remove(el);
            lst.add(element);
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
    public void remove(long id) {
        E el = lst.stream().filter(x -> x.getId() == id).findAny().get();
        if (el != null) lst.remove(el);
//        ListIterator<E> iterator = lst.listIterator(0);
//        while (iterator.hasNext()){
//            if (iterator.next().getId() == id){
//                iterator.remove();
//                break;
//            }
//        }
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
        setSuccess(false);
        var el = lst.stream().filter(x -> x.getFormOfEducation() == formOfEducation).findAny().get();
        if (el != null){
            setSuccess(true);
            lst.remove(el);
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
    }
    @Override
    public void sort(){
        Collections.sort(lst);
    }
}
