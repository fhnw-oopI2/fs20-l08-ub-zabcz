package ch.fhnw.oop2.tasky.part1.model;

import java.util.Arrays;
import java.util.List;

/**
 * Die Zustände, welche eine Task haben kann.
 */
public enum Status {
    Todo, Doing, Done, Review;

    Status(){

    }

    public static List<Status> getAllStati(){
        return Arrays.asList(Status.values());
    }

}
