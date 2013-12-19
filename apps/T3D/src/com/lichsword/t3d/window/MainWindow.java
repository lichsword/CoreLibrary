package com.lichsword.t3d.window;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.lichsword.swing.graphics.Colors;

public class MainWindow extends JPanel {

    private static final long serialVersionUID = 1L;

    private final int DEFAULT_WIDTH = 800;
    private final int DEFAULT_HEIGHT = 600;

    public MainWindow() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setBackground(Colors.GRAY_01);
        graphics2d.clearRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

    }

}
