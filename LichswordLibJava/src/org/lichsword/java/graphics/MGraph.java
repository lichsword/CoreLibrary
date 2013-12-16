package org.lichsword.java.graphics;

import java.util.Random;

/**
 * <p>
 * <b>定义：</b><br>
 * 图：顶点集合和边的集合，其中顶点集合为非空集合，即边的集合可以为空。英语：graph.G
 * </p>
 * <p>
 * <b>实现：</b><br>
 * 使用一维数组来定义顶点，用邻接矩阵(方阵)来定义边(弧)。
 * </p>
 * 
 * @author wangyue.wy
 * @data 2013-12-13
 * 
 */
public class MGraph {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // create object.
        MGraph mGraph = new MGraph(4);
        Matrix matrix = mGraph.getEdges();

        // fill data
        Random random = new Random(System.currentTimeMillis());
        int[][] data = matrix.getData();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                data[i][j] = random.nextInt(100) + 1;
            }
        }

        // print result.
        if (null != matrix) {
            System.out.println("vertexs:");
            int[] vertexs = mGraph.getVertexs();
            for (int i = 0; i < vertexs.length; i++) {
                System.out.print("" + vertexs[i] + '\t');
            }// end for

            System.out.println();
            mGraph.printEdges();
        }
    }

    public static final int INFINITY = 65536;

    // 顶点
    protected int[] vertexs;

    // 边
    protected Matrix edges;

    /**
     * 顶点数目
     */
    protected int numVertexs;
    /**
     * 边的数目
     */
    protected int numEdges;

    /**
     * 
     * @param numVertexs
     *            顶点数
     */
    public MGraph(int numVertexs) {
        this.numVertexs = numVertexs;
        init();
    }

    private void init() {
        vertexs = new int[numVertexs];
        for (int i = 0; i < numVertexs; i++) {
            vertexs[i] = i;
        }// end for

        edges = new Matrix(numVertexs, INFINITY);
    }

    public int[] getVertexs() {
        return vertexs;
    }

    public Matrix getEdges() {
        return edges;
    }

    public void printEdges() {
        int[][] data = edges.getData();
        System.out.println(">>>>>>>>>>>>>>>>");
        System.out.println("edges:");
        System.out.println(">>>>>>>>>>>>>>>>");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                System.out.print("" + data[i][j] + '\t');
            }
            System.out.println();
        }
    }
}
