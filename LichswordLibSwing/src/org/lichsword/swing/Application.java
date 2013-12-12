package org.lichsword.swing;

import java.awt.EventQueue;

import org.lichsword.swing.MainFrame.ContainerInit;

public class Application {

    private MainFrame mainFrame;

    /**
     * @param args
     */
    public static void main(String[] args) {
        Application application = new Application();
        application.run();
    }

    protected void run(final ContainerInit init) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    mainFrame = new MainFrame(init);
                    mainFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void run() {
        run(null);
    }

}
