package com.demo.map;

import java.awt.BorderLayout;
import java.awt.Container;

import org.lichsword.swing.Application;
import org.lichsword.swing.MainFrame.OnMainFramePrepare;

public class CoreApplication extends Application implements OnMainFramePrepare {

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
    public void init(Container container) {

        MapPanel mapPanel = new MapPanel();
        container.add(mapPanel, BorderLayout.CENTER);
    }

}
