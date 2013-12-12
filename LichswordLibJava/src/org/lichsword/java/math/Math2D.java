package org.lichsword.java.math;

import org.lichsword.java.graphics.Point;

public class Math2D {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * 求两点的中间点
     * 
     * @param a
     * @param b
     * @return
     */
    public static Point mid(Point a, Point b) {
        return new Point((a.x + b.x) / 2, (a.y + b.y));
    }

    public static Point mid(Point... points) {

        return null;
    }

}