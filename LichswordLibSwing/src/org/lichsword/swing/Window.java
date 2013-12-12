package org.lichsword.swing;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * invoke protected method run() to launch window.
 * 
 * @author wangyue.wy
 * 
 */
public class Window extends JFrame {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        Window window = new Window();
        window.run();
    }

    protected void run() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Window frame = new Window();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Window() {
        initWindowBound();
    }

    private void initWindowBound() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        int width = screenSize.width;
        int height = screenSize.height;

        int defaultWidth = getDefaultWidth();
        int defaultHeight = getDefaultHeight();

        setBounds(0, 0, defaultWidth, defaultHeight);

        int defaultX = (width - getSize().width) / 2;
        int defaultY = (height - getSize().height) / 2;

        // ConfigManager manager = ConfigManager.getInstance();
        // setBounds(manager.getWindowX(defaultX), manager.getWindowY(defaultY),
        // manager.getWindowWidth(ConfigManager.DEFAULT_WINDOW_WIDTH),
        // manager.getWindowHeight(ConfigManager.DEFAULT_WINDOW_HEIGHT));
        setBounds(defaultX, defaultY, defaultWidth, defaultHeight);

        setResizable(true);
    }

    protected final int DEFAULT_WIDTH = 640;
    protected final int DEFAULT_HEIGHT = 480;

    protected final String DEFAULT_WINDOW_NAME = "Window By lichsword &copy";

    protected String getWindowName() {
        return DEFAULT_WINDOW_NAME;
    }

    protected int getDefaultWidth() {
        return DEFAULT_WIDTH;
    }

    protected int getDefaultHeight() {
        return DEFAULT_HEIGHT;
    }

}
