package com.demo.map;

import java.awt.Graphics;

import javax.swing.JPanel;

import org.lichsword.java.graphics.Point;
import org.lichsword.java.math.Math2D;
import org.lichsword.swing.graphics.Colors;

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
        g.setColor(Colors.BLUE);

        Point aPoint = new Point(10, 10);
        Point bPoint = new Point(10, 120);
        Point mid = Math2D.mid(aPoint, bPoint);
        g.drawLine(10, 10, 500, 50);

        g.drawChars(new char[] { 'a', 'b', 'c', 'd', 'e' }, 0, 5, 50, 50);
        // super.paint(g);
    }
}
