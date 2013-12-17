/***********************************************************************
 * 
 *     Copyright: 2011, BAINA Technologies Co. Ltd.
 *     Classname: SheetView.java
 *     Author:    yuewang
 *     Description:    TODO
 *     History:
 *         1.  Date:   下午05:16:46
 *             Author:    yuewang
 *             Modifycation:    create the class.       
 *
 ***********************************************************************/

package org.lichsword.swing.widgets;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.lichsword.swing.graphics.Colors;

/**
 * 显示网格
 * 
 * @author wangyue.wy
 * 
 */
public class SheetView extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6077383886540913181L;

    /**
	 * 
	 */
    public SheetView() {
        super();
        setSize(800, 600);
    }

    private final int CELL_WIDTH = 10;
    private final int CELL_HEIHGT = 10;

    private final int CELL_START_X = 0;
    private final int CELL_START_Y = 0;

    @Override
    public void paint(Graphics g) {

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        final int startX = CELL_START_X;
        final int startY = CELL_START_Y;

        final int endX = panelWidth;
        final int endY = panelHeight;

        drawBg(g, panelWidth, panelHeight);

        drawSheet(g, startX, startY, endX, endY);

        // super.paint(g);
    }

    private void drawCoordinate(Graphics g) {
        // g.setColor(color)
    }

    /**
     * 
     * @param g
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void drawSheet(Graphics g, final int startX, final int startY, final int endX, final int endY) {
        // draw all vertical lines
        int curX = startX;
        while (curX <= endX) {
            g.setColor(Colors.GRAY_01);

            g.drawLine(curX, startY, curX, endY);

            curX += CELL_WIDTH;
        }

        // draw all horizontal lines
        int curY = startY;
        while (curY < endY) {
            g.setColor(Colors.GRAY_02);

            g.drawLine(startX, curY, endX, curY);

            curY += CELL_HEIHGT;
        }
    }

    /**
     * 
     * @param g
     * @param panelWidth
     * @param panelHeight
     */
    private void drawBg(Graphics g, int panelWidth, int panelHeight) {
        // clear bg
        g.setColor(new Color(CololrUtils.CANVAS_BG));
        g.drawRect(0, 0, panelWidth, panelHeight);
    }

    private class CololrUtils {
        public static final int CANVAS_BG = 0xffffff;
        public static final int CANVAS_VERTICAL_LINE = 0xbcbcbc;
        public static final int CANVAS_HORIZONTAL_LINE = 0xbebebe;
        public static final int CANVAS_CELL_FRAME = 0x353a3e;
    }

}
