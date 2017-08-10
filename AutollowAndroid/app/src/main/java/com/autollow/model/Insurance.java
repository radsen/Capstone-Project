package com.autollow.model;

/**
 * Created by radsen on 8/1/17.
 */

public class Insurance extends Document {

    private String expires;
    private String issued;
    private String starts;

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }
}
