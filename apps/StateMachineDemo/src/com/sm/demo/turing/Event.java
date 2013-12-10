package com.sm.demo.turing;

import org.xml.sax.Attributes;

public class Event extends Element {
    public Event(Attributes attr) {
        parseAttribute(attr);
    }

    @Override
    protected void parseAttribute(Attributes attr) {
        String id = attr.getValue("id");
        setId(id);
        super.parseAttribute(attr);
    }
}
