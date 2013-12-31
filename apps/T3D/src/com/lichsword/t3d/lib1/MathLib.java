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

    /* math bigger and smaller */
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

    /* swap */
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

    /* degree and radian */

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
     * TODO 这个没写完，实现时，要看书中 5.4.1节，236页 random range.
     */
    public static final void randomRange() {
        // TODO
    }

    /* clear vectors */

    /**
     * 2D向量清零
     * 
     * @param v
     */
    public static final void vector2DZero(Vector2D v) {
        v.x = 0;
        v.y = 0;
    }

    /**
     * 3D向量清零
     * 
     * @param v
     */
    public static final void vector3DZero(Vector3D v) {
        v.x = 0;
        v.y = 0;
        v.z = 0;
    }

    /**
     * 4D向量清零
     * 
     * @param v
     */
    public static final void vector4DZero(Vector4D v) {
        v.x = 0;
        v.y = 0;
        v.z = 0;
        v.w = 0;
    }

    /* initial vectors */
    public static final void vector2DInitXY(Vector2D v, float x, float y) {
        v.x = x;
        v.y = y;
    }

    public static final void vector3DInitXYZ(Vector3D v, float x, float y, float z) {
        v.x = x;
        v.y = y;
        v.z = z;
    }

    /**
     * v.w = 1.0 (default auto)
     * 
     * @param v
     * @param x
     * @param y
     * @param z
     */
    public static final void vector4DInitXYZ(Vector4D v, float x, float y, float z) {
        v.x = x;
        v.y = y;
        v.z = z;
        v.w = 1.0f;
    }

    /* vector copy */
    public static final void vector2DCopy(Vector2D vdest, Vector2D vsrc) {
        vdest.x = vsrc.x;
        vdest.y = vsrc.y;
    }

    public static final void vector3DCopy(Vector3D vdest, Vector3D vsrc) {
        vdest.x = vsrc.x;
        vdest.y = vsrc.y;
        vdest.z = vsrc.z;
    }

    public static final void vector3DCopy(Vector4D vdest, Vector4D vsrc) {
        vdest.x = vsrc.x;
        vdest.y = vsrc.y;
        vdest.z = vsrc.z;
        vdest.w = vsrc.w;
    }

    /* point initial */
    public static final void point2DFInit(Point2DF pdest, Point2DF psrc) {
        pdest.x = psrc.x;
        pdest.y = psrc.y;
    }

    public static final void point2DIInit(Point2DI pdest, Point2DI psrc) {
        pdest.x = psrc.x;
        pdest.y = psrc.y;
    }

    public static final void point3DFInit(Point3DF pdest, Point3DF psrc) {
        pdest.x = psrc.x;
        pdest.y = psrc.y;
        pdest.z = psrc.z;
    }

    public static final void point3DIInit(Point3DI pdest, Point3DI psrc) {
        pdest.x = psrc.x;
        pdest.y = psrc.y;
        pdest.z = psrc.z;
    }

    public static final void point4DFInit(Point4DF pdest, Point4DF psrc) {
        pdest.x = psrc.x;
        pdest.y = psrc.y;
        pdest.z = psrc.z;
        pdest.w = psrc.w;
    }

    public static final void point4DIInit(Point4DI pdest, Point4DI psrc) {
        pdest.x = psrc.x;
        pdest.y = psrc.y;
        pdest.z = psrc.z;
        pdest.w = psrc.w;
    }

    /* matrix clear */
    public static final void matrixZero2X2(Matrix2X2 matrix2x2) {
        matrix2x2.m[0][0] = 0;
        matrix2x2.m[0][1] = 0;
        matrix2x2.m[1][0] = 0;
        matrix2x2.m[1][1] = 0;
    }

    public static final void matrixZero3X3(Matrix3X3 matrix3x3) {
        matrix3x3.m[0][0] = 0;
        matrix3x3.m[0][1] = 0;
        matrix3x3.m[0][2] = 0;
        matrix3x3.m[1][0] = 0;
        matrix3x3.m[1][1] = 0;
        matrix3x3.m[1][2] = 0;
        matrix3x3.m[2][0] = 0;
        matrix3x3.m[2][1] = 0;
        matrix3x3.m[2][2] = 0;
    }

    public static final void matrixZero4X4(Matrix4X4 matrix4x4) {
        matrix4x4.m[0][0] = 0;
        matrix4x4.m[0][1] = 0;
        matrix4x4.m[0][2] = 0;
        matrix4x4.m[1][0] = 0;
        matrix4x4.m[1][1] = 0;
        matrix4x4.m[1][2] = 0;
        matrix4x4.m[2][0] = 0;
        matrix4x4.m[2][1] = 0;
        matrix4x4.m[2][2] = 0;
        matrix4x4.m[3][0] = 0;
        matrix4x4.m[3][1] = 0;
        matrix4x4.m[3][2] = 0;
    }

    public static final void matrixZero4X3(Matrix4X3 matrix4x3) {
        matrix4x3.m[0][0] = 0;
        matrix4x3.m[0][1] = 0;
        matrix4x3.m[0][2] = 0;
        matrix4x3.m[1][0] = 0;
        matrix4x3.m[1][1] = 0;
        matrix4x3.m[1][2] = 0;
        matrix4x3.m[2][0] = 0;
        matrix4x3.m[2][1] = 0;
        matrix4x3.m[2][2] = 0;
    }

    public static final void matrixIdentity2X2(Matrix2X2 matrix2x2) {
        matrix2x2.m[0][0] = 1;
        matrix2x2.m[0][1] = 0;
        matrix2x2.m[1][0] = 0;
        matrix2x2.m[1][1] = 1;
    }

    public static final void matrixIdentity3X3(Matrix3X3 matrix3x3) {
        matrix3x3.m[0][0] = 1;
        matrix3x3.m[0][1] = 0;
        matrix3x3.m[0][2] = 0;
        matrix3x3.m[1][0] = 0;
        matrix3x3.m[1][1] = 1;
        matrix3x3.m[1][2] = 0;
        matrix3x3.m[2][0] = 0;
        matrix3x3.m[2][1] = 0;
        matrix3x3.m[2][2] = 1;
    }

    public static final void matrixIdentity4X3(Matrix4X3 matrix4x3) {
        matrix4x3.m[0][0] = 1;
        matrix4x3.m[0][1] = 0;
        matrix4x3.m[0][2] = 0;
        matrix4x3.m[0][3] = 0;

        matrix4x3.m[1][0] = 0;
        matrix4x3.m[1][1] = 1;
        matrix4x3.m[1][2] = 0;
        matrix4x3.m[1][3] = 0;

        matrix4x3.m[2][0] = 0;
        matrix4x3.m[2][1] = 0;
        matrix4x3.m[2][2] = 1;
        matrix4x3.m[2][3] = 0;

        matrix4x3.m[3][0] = 0;
        matrix4x3.m[3][1] = 0;
        matrix4x3.m[3][2] = 0;
        matrix4x3.m[3][3] = 1;
    }

    public static final void matrixIdentity4X4(Matrix4X4 matrix4x4) {
        matrix4x4.m[0][0] = 1;
        matrix4x4.m[0][1] = 0;
        matrix4x4.m[0][2] = 0;
        matrix4x4.m[0][3] = 0;

        matrix4x4.m[1][0] = 0;
        matrix4x4.m[1][1] = 1;
        matrix4x4.m[1][2] = 0;
        matrix4x4.m[1][3] = 0;

        matrix4x4.m[2][0] = 0;
        matrix4x4.m[2][1] = 0;
        matrix4x4.m[2][2] = 1;
        matrix4x4.m[2][3] = 0;

        matrix4x4.m[3][0] = 0;
        matrix4x4.m[3][1] = 0;
        matrix4x4.m[3][2] = 0;
        matrix4x4.m[3][3] = 1;
    }

    // TODO
}
