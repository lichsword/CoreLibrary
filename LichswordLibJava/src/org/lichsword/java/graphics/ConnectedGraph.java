package org.lichsword.java.graphics;

import java.util.Random;

/**
 * 连通图：无向图中，任意2点之间都有路径相连通，则称无向图为连通图。
 * 
 * @author wangyue.wy
 * @data 2013-12-16
 */
public class ConnectedGraph extends MGraph {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // create object
        int numVertexs = 10;
        ConnectedGraph connectedGraph = new ConnectedGraph(numVertexs);
        Matrix matrix = connectedGraph.getEdges();

        // fill data
        Random random = new Random(System.currentTimeMillis());
        int[][] data = matrix.getData();
        for (int i = 0; i < data.length; i++) {
            data[i][i] = 0;
            for (int j = i + 1; j < data.length; j++) {
                data[j][i] = data[i][j] = random.nextInt(100) + 1;
            }
        }

        // print result.
        connectedGraph.printEdges();

        connectedGraph.findMinTree();

    }

    /**
     * 
     * @param numVertexs
     */
    public ConnectedGraph(int numVertexs) {
        super(numVertexs);
    }

    /**
     * 查找最小生成树，用于网络布线等需要通过全部顶点的最短路径。
     */
    public void findMinTree() {

        /**
         * 顶点下标
         */
        int adjvex[] = new int[numVertexs];
        /**
         * 边的权值
         */
        int lowcost[] = new int[numVertexs];

        // 我们从第0个开始找最小生成树，故将第0个顶点视为已经添加至生成树。第0个的权值为0
        lowcost[0] = 0;

        // 接上，第一个项点坐标为0
        adjvex[0] = 0;

        // 初始化第0个顶点的接邻边权值
        int[][] data = edges.getData();
        for (int i = 1; i < numVertexs; i++) {
            // 将第0个顶点的接邻边的权值入数组
            lowcost[i] = data[0][i];
        }

        int min = INFINITY;
        int tempMinIndex = 0;
        int currentMinIndex = 0;

        System.out.println(">>>>>>>>>>>>>>>>");
        System.out.println("查找最小生成树：");
        System.out.println(">>>>>>>>>>>>>>>>");
        System.out.println("(0)");
        // 循环下标除0以外的全部顶点
        for (int i = 1; i < numVertexs; i++) {
            /* 初始化最小权值为无穷大 */
            min = INFINITY;
            tempMinIndex = 1;
            currentMinIndex = 0;

            while (tempMinIndex < numVertexs) {
                if (lowcost[tempMinIndex] != 0 && lowcost[tempMinIndex] < min) {
                    // 如果权值不为0（说明未入生成树），且小于当前最小的权值。
                    min = lowcost[tempMinIndex];
                    currentMinIndex = tempMinIndex;
                }
                tempMinIndex++;
            }

            // 打印当前最小权值
            System.out.print("->(" + currentMinIndex + "); edge = " + lowcost[currentMinIndex] + "\n");
            // 标记为已经添加至生成树
            lowcost[currentMinIndex] = 0;

            // 扫描新的点的边，并更新边的权值数组.
            for (int j = 1; j < numVertexs; j++) {
                // 若当前顶点的边的权值有小于其它未添加至生成树的顶点的相应的接邻边的权值。
                if (lowcost[j] != 0 && data[currentMinIndex][j] < lowcost[j]) {
                    lowcost[j] = data[currentMinIndex][j];
                    adjvex[j] = currentMinIndex;
                }
            }// end for
        }// end for

    }
}
