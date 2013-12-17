package org.lichsword.swing.widgets;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import org.lichsword.java.graphics.Point;
import org.lichsword.java.math.Math2D;
import org.lichsword.swing.graphics.Colors;

public class CoordinateView extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -1921452864757350565L;

    public CoordinateView() {
        setSize(800, 600);
    }

    @Override
    public void paint(Graphics g) {
        setAntiAliasing(g);
        g.setColor(Colors.BLUE);

        Point aPoint = new Point(10, 10);
        Point bPoint = new Point(10, 120);
        Point mid = Math2D.mid(aPoint, bPoint);
        g.drawLine(10, 10, 500, 50);

        g.drawChars(new char[] { 'a', 'b', 'c', 'd', 'e' }, 0, 5, 50, 50);
        // super.paint(g);
    }

    /**
     * 设置抗锯齿
     * 
     * @param g
     */
    private void setAntiAliasing(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            // for anti aliasing geometric shapes
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
            // for anti aliasing text
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // reset
            g = g2d;
        }
    }
}
