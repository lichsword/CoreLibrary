package com.sm.demo.turing;

import org.xml.sax.Attributes;

public class Element {

    private String id;
    protected String tag;

    public String getTag() {
        return tag;
    }

    protected void parseAttribute(Attributes attr) {

    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
