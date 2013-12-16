package com.lichsword.coordinate;

import java.awt.Container;

import org.lichsword.swing.Application;
import org.lichsword.swing.MainFrame.OnMainFramePrepare;
import org.lichsword.swing.widgets.SheetView;

public class CoreApplication extends Application implements OnMainFramePrepare {

    /**
     * @param args
     */
    public static void main(String[] args) {
        CoreApplication application = new CoreApplication();
        application.run(application);
    }

    @Override
    public void init(Container container) {
        container.add(new SheetView());
    }

}
