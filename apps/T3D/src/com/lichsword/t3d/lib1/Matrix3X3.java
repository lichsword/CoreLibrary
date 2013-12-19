package com.lichsword.t3d.lib1;

public class Matrix3X3 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    float m[][] = new float[3][3];

    public float get00() {
        return m[0][0];
    }

    public float get01() {
        return m[0][1];
    }

    public float get02() {
        return m[0][2];
    }

    //
    public float get10() {
        return m[1][0];
    }

    public float get11() {
        return m[1][1];
    }

    public float get12() {
        return m[1][2];
    }

    //
    public float get20() {
        return m[2][0];
    }

    public float get21() {
        return m[2][1];
    }

    public float get22() {
        return m[2][2];
    }

}