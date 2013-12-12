package com.demo.map;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JTextField;

import org.lichsword.swing.Application;
import org.lichsword.swing.MainFrame.ContainerInit;

public class CoreApplication extends Application implements ContainerInit {

    /**
     * @wbp.parser.entryPoint
     */
    public static void main(String[] args) {
        CoreApplication application = new CoreApplication();
        application.run(application);
    }

    public CoreApplication() {
        init();
    }

    private void init() {
        // TODO
    }

    @Override
    public void initContainerPanel(Container container) {
        // JButton btnNewButton = new JButton("New button");
        // container.add(btnNewButton, BorderLayout.CENTER);

        JTextField textField = new JTextField();
        container.add(textField, BorderLayout.WEST);
        textField.setColumns(10);

        MapPanel mapPanel = new MapPanel();
        container.add(mapPanel, BorderLayout.CENTER);
    }

}
