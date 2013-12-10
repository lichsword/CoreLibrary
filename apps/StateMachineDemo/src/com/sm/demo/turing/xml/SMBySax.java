package com.sm.demo.turing.xml;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sm.demo.turing.Element;
import com.sm.demo.turing.Event;
import com.sm.demo.turing.State;
import com.sm.demo.turing.StateMachine;

public class SMBySax extends DefaultHandler {

    public static void main(String[] args) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (SAXException e1) {
            e1.printStackTrace();
        }

        if (null == parser) {
            throw new IllegalArgumentException("parser is null");
        }// end if

        SMBySax handler = new SMBySax();
        try {
            parser.parse(new File("StateMachineManifest.xml"), handler);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final Stack<Element> stackElements = new Stack<Element>();

    @Override
    public void startDocument() throws SAXException {
        System.out.println("start document...");
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("end document...");
        if (null != stateMachine) {
            System.out.println("retrieve a state machine.");
        }
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("start element...");
        System.out.print("{uri=" + uri + "}");
        System.out.print("{localName=" + localName + "}");
        System.out.println("{qName=" + qName + "}");
        handleElementStart(qName, attributes);
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("end element...");
        System.out.println("{qName=" + qName + "}");
        handleElementEnd(qName);
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String origin = String.valueOf(ch, start, length);
        String trim = origin.trim();
        System.out.println("process characters:" + trim);
        handleElementProcess(trim);
        super.characters(ch, start, length);
    }

    public class Condition extends Element {

        protected String flowType = null;

        private Event event;
        private Next next;

        public void addEvent(Event event) {
            this.event = event;
        }

        public void addNext(Next next) {
            this.next = next;
        }

        public Condition(Attributes attr) {
            parseAttribute(attr);
        }

        @Override
        protected void parseAttribute(Attributes attr) {
            String flowType = attr.getValue("flow_type");
            this.flowType = flowType;
            super.parseAttribute(attr);
        }

        public String getFlowType() {
            return flowType;
        }
    }

    public class Next extends Element {

        public Next(Attributes attr) {
            parseAttribute(attr);
        }

        @Override
        protected void parseAttribute(Attributes attr) {
            String id = attr.getValue("id");
            setId(id);
            super.parseAttribute(attr);
        }
    }

    private StateMachine stateMachine = null;

    private void handleElementStart(String qName, Attributes attributes) {
        Element element = null;
        if (qName.equals("state_machine")) {
            stateMachine = new StateMachine();
            element = stateMachine;
        } else if (qName.equals("state")) {
            State state = new State(attributes);
            stateMachine.addState(state);
            element = state;
        } else if (qName.equals("condition")) {
            Condition condition = new Condition(attributes);
            stateMachine.currentState.addCondition(condition);
            element = condition;
        } else if (qName.equals("event")) {
            Event event = new Event(attributes);
            stateMachine.currentState.currentCondition.addEvent(event);
            element = event;
        } else if (qName.equals("next")) {
            Next next = new Next(attributes);
            stateMachine.currentState.currentCondition.addNext(next);
            element = next;
        } else {
            throw new IllegalArgumentException("undefined element:" + qName);
        }

        if (null != element) {
            element.setTag(qName);
            stackElements.push(element);
        }// end if
    }

    /**
     * data是指起止标签之间的内容<br>
     * 示例: <br>
     * <code>&lttag&gtdata&lt/tag&gt</code>
     * 
     * @param data
     */
    private void handleElementProcess(String data) {
        Element element = stackElements.peek();
        if (null != element) {
        } else {
            // / Logger.
        }
    }

    private void handleElementEnd(String qName) {
        Element element = stackElements.peek();
        if (null != element) {
            String local = element.getTag();
            if (local.equals(qName)) {
                stackElements.pop();
            } else {
                throw new IllegalArgumentException("xml " + local + " is not match:" + qName);
            }
        } else {
            throw new IllegalArgumentException("xml is not match:" + qName);
        }
    }
}
