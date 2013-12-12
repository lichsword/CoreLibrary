package org.lichsword.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * <p>
 * This 'MainFrame' not the JWindow, just package some main page logic, which
 * extends from JFrame.
 * </p>
 * <p>
 * invoke protected method run() to launch it.
 * </p>
 * 
 * @author wangyue.wy
 * @data 2013-12-12
 * 
 */
public class MainFrame extends JFrame {

    protected final int DEFAULT_WIDTH = 640;

    protected final int DEFAULT_HEIGHT = 480;

    protected final int DEFAULT_BG_COLOR = 0xffffff;

    protected final String DEFAULT_WINDOW_NAME = "MainFrame By lichsword &copy";

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

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainFrame() {
        initWindowBound();
    }

    public MainFrame(OnMainFramePrepare init) {
        this.mainFramePrepare = init;
        initWindowBound();
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

    protected int getDefaultBgColor() {
        return DEFAULT_BG_COLOR;
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
        if (null != mainFramePrepare) {
            mainFramePrepare.init(getContentPane());
        }// end if

        addWindowListener(mWindowListener);
    }

    private OnMainFramePrepare mainFramePrepare = null;

    public interface OnMainFramePrepare {
        public void init(Container container);
    }

}
