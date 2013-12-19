package com.lichsword.t3d;

import java.awt.Container;

import org.lichsword.swing.Application;
import org.lichsword.swing.MainFrame.OnMainFramePrepare;

import com.lichsword.t3d.window.MainWindow;

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
        container.add(new MainWindow());
    }

}
