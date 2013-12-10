package com.sm.demo.turing.xml;

import java.io.File;
import java.util.ArrayList;

import org.lichsword.java.xml.dom.DOMParseHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sm.demo.turing.State;

public class StateMachineXMLHandler extends DOMParseHandler {

    /**
     * @param args
     */
    public static void main(String[] args) {
        StateMachineXMLHandler handler = new StateMachineXMLHandler();
        handler.init();
    }

    // manifest file name.
    private final String MANIFEST_FILE_NAME = "StateMachineManifest.xml";

    // tags
    private final String TAG_STATE_MACHINE = "StateMachine";
    private final String TAG_STATE = "State";
    private final String TAG_CONDITION = "Condition";
    private final String TAG_EVENT = "Event";
    private final String TAG_NEXT = "Next";

    // attributes
    private final String ATTR_ID = "id";
    private final String ATTR_NAME = "name";
    private final String ATTR_TYPE = "type";
    private final String ATTR_FLOW_TYPE = "flow_type";

    // values
    private final String TYPE_VALUE_START = "start";
    private final String TYPE_VALUE_PROCESS = "process";

    private final String FLOW_TYPE_VALUE_SELF = "self";
    private final String FLOW_TYPE_VALUE_OTHER = "other";

    private final ArrayList<State> stateList = new ArrayList<State>();

    public void init() {
        loadManifestAsync();
    }

    private void loadManifestAsync() {
        Thread thread = new LoadXMLThread();
        thread.run();
    }

    private void loadManifest() {

    }

    private boolean deleteStateById(int id) {
        return false;
    }

    private boolean deleteStateByName(String name) {
        return false;
    }

    private boolean insertState(Node parentNode, State state) {
        return false;
    }

    private class LoadXMLThread extends Thread {

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            System.out.println("load XML thread run...start");
            Document document = parseXMLFile(new File(MANIFEST_FILE_NAME));
            if (null == document) {
                System.out.println("parse xml file...failed.");
                return;
            }// end if

            System.out.println("parse xml file...successful.");

            // NodeList stateMachineList =
            // document.getElementsByTagName("state_machine");
            NodeList stateMachineList = document.getChildNodes();
            int stateMachineLength = stateMachineList.getLength();
            System.out.println("***state machine info***");
            System.out.println("state_machine tag count = " + stateMachineLength);
            Node stateMachineNode = null;
            for (int i = 0; i < stateMachineList.getLength(); i++) {
                stateMachineNode = stateMachineList.item(i);
                NodeList stateList = stateMachineNode.getChildNodes();
                int stateLength = stateList.getLength();
                System.out.println("has (" + stateLength + ") states");
                Node stateItem = null;
                for (int j = 0; j < stateList.getLength(); j++) {
                    stateItem = stateList.item(j);
                    System.out.println("***state info***");
                    System.out.print("{index=" + j + "}{number=" + (j + 1) + "}:");
                    // read all attributes of this state.
                    NamedNodeMap attributes = stateItem.getAttributes();
                    if (null == attributes) {
                        System.out.print("state's attribute is null==>");
                        System.out.println("data:" + stateItem.toString());
                        continue;
                    }
                    int attrLength = attributes.getLength();
                    System.out.println("{attributes count=" + attrLength + "}");
                    Node attributeItem = null;
                    for (int m = 0; m < attributes.getLength(); m++) {
                        System.out.print("attribute=" + (m + 1) + ", detail:");
                        attributeItem = attributes.item(m);
                        if (null == attributeItem) {
                            System.out.println("object is null, exception.");
                        }
                        String name = attributeItem.getNodeName();
                        System.out.print("{name=" + name + "}");
                        String value = attributeItem.getNodeValue();
                        System.out.print("{value=" + value + "}");
                        int type = attributeItem.getNodeType();
                        System.out.println("{type=" + type + "}");
                    }
                    // read all conditions of this state.
                    NodeList conditioNodeList = stateItem.getChildNodes();
                    if (null == conditioNodeList) {
                        System.out.println("No child nodes of this state, in XML level");
                        continue;
                    }// end if

                    int childNodeCount = conditioNodeList.getLength();
                    System.out.println("in XML level, state's child node count=" + childNodeCount);
                    Node conditionItem = null;
                    for (int n = 0; n < childNodeCount; n++) {
                        System.out.print("child index=" + (n + 1) + ", detail:");
                        conditionItem = conditioNodeList.item(n);
                        if (null == conditionItem) {
                            System.out.println("conditionItem is null");
                            continue;
                        }// end if

                        NodeList conditionInfos = conditionItem.getChildNodes();
                        Node infoItem = null;
                        for (int k = 0; k < conditionInfos.getLength(); k++) {
                            infoItem = conditionInfos.item(k);
                            String name = infoItem.getNodeName();
                            System.out.println("{name=" + name + "}");

                            String textContent = infoItem.getTextContent();
                            System.out.println("{textContent=" + textContent + "}");
                        }

                        // String value = childNodeItem.getNodeValue();
                        // System.out.print("{value=" + value + "}");
                        // int type = childNodeItem.getNodeType();
                        // System.out.println("{type=" + type + "}");
                    }
                    // read all condition tags
                }
            }
            // TODO Auto-generated method stub
            super.run();
        }

    }

    // TODO more method.
}