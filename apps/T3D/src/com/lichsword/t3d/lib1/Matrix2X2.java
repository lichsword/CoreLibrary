package com.lichsword.t3d.lib1;

public class Matrix2X2 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public Matrix2X2() {
    }

    public Matrix2X2(float[] value) {
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                m[i][j] = value[index];
            }
        }
    }

    float m[][] = new float[2][2];

    public float get00() {
        return m[0][0];
    }

    public float get01() {
        return m[0][1];
    }

    //
    public float get10() {
        return m[1][0];
    }

    public float get11() {
        return m[1][1];
    }

}