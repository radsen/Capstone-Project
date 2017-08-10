package com.autollow.model;

/**
 * Created by radsen on 8/1/17.
 */

public class DocumentFactory {
    public static Class getDocument(String type){
        Class document = null;

        if (type.equals("I")){
            document = Insurance.class;
        } else if (type.equals("E")){
            document = Emission.class;
        }

        return document;
    }
}
