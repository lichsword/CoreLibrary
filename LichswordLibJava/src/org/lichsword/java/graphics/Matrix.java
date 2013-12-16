package org.lichsword.java.graphics;

import java.util.Random;

/**
 * 矩阵
 * 
 * @author wangyue.wy
 * @data 2013-12-16
 */
public class Matrix {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int n = 5;
        Matrix matrix = new Matrix(n);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix.data[i][j] = random.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            matrix.data[i][i] = 0;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%d\t", matrix.data[i][j]);
            }
            System.out.println();
        }

    }

    private final int data[][];

    /**
     * square matrix
     * 
     * @param n
     *            set n*n dimensionality
     */
    public Matrix(int n) {
        data = new int[n][n];
    }

    /**
     * 
     * @param n
     *            square dim of matrix.
     * @param value
     *            initial value.
     */
    public Matrix(int n, int value) {
        data = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = value;
            }
        }
    }

    public int[][] getData() {
        return data;
    }

}
