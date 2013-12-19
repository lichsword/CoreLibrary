package com.lichsword.colorpicker;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ColorPickerView extends JFrame {

    // 是否开始取色
    private boolean isStart = true;
    JLabel jLabel = new JLabel();

    public void init() {
        JFrame frame = new JFrame("取色器");
        frame.setSize(280, 100);

        // 设置其布局为BorderLayout
        frame.setLayout(new BorderLayout());

        // 设置始终在最前显示
        frame.setAlwaysOnTop(true);

        // 设置不可重新设置大小
        frame.setResizable(false);

        // 设置由操作系统决定其初始位置
        frame.setLocationByPlatform(true);
        // frame.setUndecorated(true);

        // 添加一个点击关闭按钮的时间监听器
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // 设置不透明，JLabel默认是透明的，不能设置背景色
        jLabel.setOpaque(true);

        jLabel.setText("[r=null,g=null.b=null] RGB:(#000000)");

        final JButton beginButton = new JButton("开始取色");
        final JButton endButton = new JButton("结束");

        endButton.setEnabled(false);

        beginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        isStart = true;
                        beginButton.setEnabled(false);
                        endButton.setEnabled(true);
                        Robot robot = null;
                        try {
                            robot = new Robot();
                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                        while (isStart) {

                            // 取鼠标所在点的Color对象
                            Color color = robot.getPixelColor(MouseInfo.getPointerInfo().getLocation().x, MouseInfo
                                    .getPointerInfo().getLocation().y);

                            String rgbString = String.valueOf(color).substring("java.awt.Color".length());

                            String RGB = " RGB:(#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase()
                                    + ")";

                            /*如果底色比较深，则设置字体颜色为白色，此处采用的是html的形式
                            ，Swing支持采用html来改变组件样式*/
                            if (color.getBlue() + color.getRed() + color.getGreen() < 170) {
                                jLabel.setText("<html><body><font color=white>" + rgbString + RGB
                                        + "</font></body></html>");
                            } else {
                                jLabel.setText(rgbString + RGB);
                            }
                            jLabel.setBackground(color);
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });

        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isStart = false;
                beginButton.setEnabled(true);
                endButton.setEnabled(false);
            }
        });
        frame.add(jLabel, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.add(beginButton);
        jPanel.add(endButton);
        frame.add(jPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            // 设置其外观为系统外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new ColorPickerView().init();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
