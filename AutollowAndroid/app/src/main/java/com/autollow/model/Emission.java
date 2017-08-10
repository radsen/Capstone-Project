package com.autollow.model;

/**
 * Created by radsen on 8/1/17.
 */

public class Emission extends Document {

    private String expires;
    private String issued;

    public String getExpiration() {
        return expires;
    }

    public void setExpiration(String expires) {
        this.expires = expires;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }
}
