package org.lichsword.swing.widgets;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import org.lichsword.swing.graphics.Colors;

public class TipDialog extends JComponent {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public static final int WIDTH = 320;
    public static final int HEIGHT = 160;

    public TipDialog() {
        setSize(WIDTH, HEIGHT);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setBackground(Colors.YELLOW_JAVADOC);
        graphics2d.clearRect(0, 0, WIDTH, HEIGHT);

        graphics2d.setColor(Colors.BLUE);
        graphics2d.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        super.paint(g);
    }

}
