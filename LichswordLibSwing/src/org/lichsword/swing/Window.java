package org.lichsword.swing;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * invoke protected method run() to launch window.
 * 
 * @author wangyue.wy
 * 
 */
public class Window extends JFrame {

    protected final int DEFAULT_WIDTH = 640;

    protected final int DEFAULT_HEIGHT = 480;

    protected final String DEFAULT_WINDOW_NAME = "Window By lichsword &copy";

    /**
     * 
     */
    private static final long serialVersionUID = 3681214771336904570L;

    private final WindowListener mWindowListener = new WindowListener() {
    
        @Override
        public void windowOpened(WindowEvent e) {
    
        }
    
        @Override
        public void windowIconified(WindowEvent e) {
    
        }
    
        @Override
        public void windowDeiconified(WindowEvent e) {
    
        }
    
        @Override
        public void windowDeactivated(WindowEvent e) {
    
        }
    
        @Override
        public void windowClosing(WindowEvent e) {
            // writeWindowConfigParam();
        }
    
        @Override
        public void windowClosed(WindowEvent e) {
    
        }
    
        @Override
        public void windowActivated(WindowEvent e) {
    
        }
    };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        Window window = new Window();
        window.run();
    }

    public Window() {
        initWindowBound();
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

    protected String getWindowName() {
        return DEFAULT_WINDOW_NAME;
    }

    protected int getDefaultWidth() {
        return DEFAULT_WIDTH;
    }

    protected int getDefaultHeight() {
        return DEFAULT_HEIGHT;
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
    
        addWindowListener(mWindowListener);
    }

}
