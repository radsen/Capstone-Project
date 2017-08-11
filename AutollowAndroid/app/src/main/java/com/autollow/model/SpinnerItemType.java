package com.autollow.model;

/**
 * Created by radsen on 8/10/17.
 */

public class SpinnerItemType {

    int id;
    String key;
    String description;

    public SpinnerItemType(){}

    public SpinnerItemType(int id, String key, String description){
        this.id = id;
        this.key = key;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
