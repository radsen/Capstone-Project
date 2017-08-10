package com.autollow.model;

/**
 * Created by radsen on 7/31/17.
 */

public class Document {

    public static final int INSURANCE = 1;
    public static final int EMISSIONS = 2;

    private String desc;
    private String type;
    private String number;
    private boolean required = true;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
