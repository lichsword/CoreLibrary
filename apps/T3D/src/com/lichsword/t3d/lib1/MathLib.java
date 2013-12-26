package com.lichsword.t3d.lib1;

import java.util.Random;

/**
 * 数学引擎库
 * 
 * @author wangyue.wy
 * @data 2013-12-18
 */
public class MathLib {

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int t = 0;
        // error
        System.out.println(a);
        System.out.println(b);
        System.out.println(t);
        System.out.println("swap");
        swap(a, b, t);
        System.out.println(a);
        System.out.println(b);
        System.out.println(t);
        // good
        int[] pair = new int[] { 1, 2 };
        System.out.print(pair[0]);
        System.out.println(pair[1]);
        swap(pair);
        System.out.print(pair[0]);
        System.out.println(pair[1]);
    }

    private final Random random = new Random(System.currentTimeMillis());

    public static final int min(int a, int b) {
        return (a < b) ? a : b;
    }

    public static final float min(float a, float b) {
        return (a < b) ? a : b;
    }

    public static final int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static final float max(float a, float b) {
        return (a > b) ? a : b;
    }

    /**
     * java 中是传值，故无法实现高效的swap函数，只能传数组或对象
     * 
     * @param a
     * @param b
     * @param t
     */
    @Deprecated
    public static final void swap(int a, int b, int t) {
        t = a;
        a = b;
        b = t;
    }

    /**
     * swap 2 element
     * 
     * @param pair
     *            2 element
     */
    public static final void swap(int[] pair) {
        int t = pair[0];
        pair[0] = pair[1];
        pair[1] = t;
    }

    /**
     * swap 2 element
     * 
     * @param pair
     *            2 element
     */
    public static final void swap(float[] pair) {
        float t = pair[0];
        pair[0] = pair[1];
        pair[1] = t;
    }

    /**
     * degree to radian
     * 
     * @param angle
     * @return double radian
     */
    public static final double degToRadWithDouble(float angle) {
        return (angle * Constants.PI / 180.0);
    }

    /**
     * degree to radian
     * 
     * @param angle
     * @return float radian
     */
    public static final float degToRad(float angle) {
        return (float) (angle * Constants.PI / 180.0);
    }

    /**
     * random range.
     */
    public static final void randomRange() {
        // TODO
    }
}
