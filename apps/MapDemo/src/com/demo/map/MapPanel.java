package com.demo.map;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MapPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -1921452864757350565L;

    public MapPanel() {
        setSize(800, 600);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(0xff0000));
        g.drawLine(10, 10, 500, 50);

        g.drawChars(new char[] { 'a', 'b', 'c', 'd', 'e' }, 0, 5, 50, 50);
        // super.paint(g);
    }
}
