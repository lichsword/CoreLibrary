package com.sm.demo.turing;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.sm.demo.turing.xml.SMBySax;
import com.sm.demo.turing.xml.SMBySax.Condition;

public class State extends Element {

    // private String name = null;
    // private Action beginAction;
    // private Action loopAction;
    // private Action endAction;
    private String type = null;

    private final ArrayList<Condition> conditions = new ArrayList<SMBySax.Condition>();
    public Condition currentCondition = null;

    public State() {
    }

    public State(Attributes attr) {
        parseAttribute(attr);
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
        currentCondition = condition;
    }

    public String getType() {
        return type;
    }

    @Override
    protected void parseAttribute(Attributes attr) {
        int attrLength = attr.getLength();
        System.out.println("{attributes count=" + attrLength + "}");
        String id = attr.getValue("id");
        String type = attr.getValue("type");
        setId(id);
        this.type = type;
        super.parseAttribute(attr);
    }
}
