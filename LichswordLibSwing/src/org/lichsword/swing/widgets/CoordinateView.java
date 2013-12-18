package org.lichsword.swing.widgets;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import org.lichsword.swing.graphics.Colors;

public class CoordinateView extends JComponent {

    /**
     * 
     */
    private static final long serialVersionUID = -1921452864757350565L;

    private final int WIDTH = 80;
    private final int HEIGHT = 60;

    public CoordinateView() {
        setSize(WIDTH, HEIGHT);
    }

    @Override
    public void paint(Graphics g) {
        setAntiAliasing(g);

        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setBackground(Colors.YELLOW_JAVADOC);
        graphics2d.clearRect(0, 0, WIDTH, HEIGHT);
        graphics2d.setColor(Colors.BLUE);
        // Point aPoint = new Point(10, 10);
        // Point bPoint = new Point(10, 120);
        // Point mid = Math2D.mid(aPoint, bPoint);
        graphics2d.drawLine(10, 10, 500, 50);
        graphics2d.drawString("show me the money", 50, 50);

        graphics2d.drawRect(0, 0, WIDTH, HEIGHT);
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
