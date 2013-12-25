package com.lichsword.t3d.lib1;

public class Constants {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * 圆周率
     */
    public static final float PI = 3.141592654f;
    /**
     * 圆周率2倍
     */
    public static final float PI2 = 6.283185307f;
    /**
     * 圆周率1/2
     */
    public static final float PI_DIV_2 = 1.570796327f;
    /**
     * 圆周率的1/4
     */
    public static final float PI_DIV_4 = 0.785398163f;

    /**
     * 定点数的偏移
     */
    public static final int FIXP16_SHIFT = 16;
    /**
     * 定点数的最大值
     */
    public static final int FIXP16_MAG = 65526;
    /**
     * 定点数小数部分的掩码
     */
    public static final int FIXP16_DP_MASK = 0x0000ffff;
    /**
     * 定点数整数部分的掩码
     */
    public static final int FIXP16_WP_MASK = 0xffff0000;
    /**
     * 非常小的数
     */
    public static final float EPSILON_E4 = 1E-4f;
    /**
     * 非常小的数
     */
    public static final float EPSILON_E5 = 1E-5f;
    /**
     * 非常小的数
     */
    public static final float EPSILON_E6 = 1E-6f;
    /**
     * 参数化直线无交点
     */
    public static final int PARM_LINE_NO_INTERESECT = 0;
    /**
     * 参数化直线与目标线段内部相交
     */
    public static final int PARM_LINE_INTERESECT_IN_SEGMENT = 1;
    /**
     * 参数化直线与目标线段的沿长线相关
     */
    public static final int PARM_LINE_INTERESECT_OUT_SEGMENT = 2;
    /**
     * 参数化直线与目标线段全部重合（及线段位于参数化直线上）
     */
    public static final int PARM_LINE_INTERESECT_EVERYWHERE = 3;
    /**
     * 4X4 单元矩阵
     */
    public static final Matrix4X4 IDENTITYM_MATRIX4X4 = new Matrix4X4(new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0,
            0, 0, 0, 1 });
    /**
     * 4X3 单元矩阵（数学上说，这是不正确的），假设最后一列为{0,0,0,1}
     */
    public static final Matrix4X3 IDENTITY_MATRIX4X3 = new Matrix4X3(new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0 });
    /**
     * 3X3
     */
    public static final Matrix3X3 IDENTITY_MATRIX3X3 = new Matrix3X3(new float[] { 1, 0, 0, 0, 1, 0, 0, 0, 1 });
    /**
     * 2X2
     */
    public static final Matrix2X2 IDENTITY_MATRIX2X2 = new Matrix2X2(new float[] { 1, 0, 0, 1 });
}
