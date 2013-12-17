package com.demo.map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class FloatWindow extends javax.swing.JFrame {
    boolean movieTf = false;
    Point mainP = new Point();

    public FloatWindow() {// 構造方法
        this.setUndecorated(true);// 去掉JFrame的标题栏
        Image ico = Toolkit.getDefaultToolkit().getImage("g:/h2.jpg");
        this.setIconImage(ico);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");// UI设置
            // UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());//windows樣式
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "發生UI加載錯誤，您可能無法看到程式預想的UI效果，錯誤信息如下：\n" + ex.toString());
        }
        initComponents();
        this.setLocation(100, 100);
    }

    // <editor-fold defaultstate="collapsed" desc=" 生成的代码 ">
    private void initComponents() {
        jlb = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }

            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        jlb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 128, 128)));
        jlb.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlbMouseClicked(evt);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jlbMousePressed(evt);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jlbMouseReleased(evt);
            }
        });
        jlb.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jlbMouseDragged(evt);
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jlbMouseMoved(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                jlb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 289,
                Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jlb,
                javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE));
        pack();
    }// </editor-fold>

    private void jlbMousePressed(java.awt.event.MouseEvent evt) {
        // TODO 将在此处添加您的处理代码：
        mainP.x = evt.getX();// 当鼠标按下的时候获得窗口当前的位置
        mainP.y = evt.getY();
    }

    private void jlbMouseDragged(java.awt.event.MouseEvent evt) {
        // TODO 将在此处添加您的处理代码： //鼠标拖动
        this.jlb.setIcon(null); // 拖動時候將圖片去除
        Point p = this.getLocation();// 当鼠标拖动时获取窗口当前位置
        this.setLocation(p.x + evt.getX() - mainP.x, p.y + evt.getY() - mainP.y);
    }

    private void jlbMouseMoved(java.awt.event.MouseEvent evt) {
        // TODO 将在此处添加您的处理代码：//鼠標移動
    }

    private void jlbMouseReleased(java.awt.event.MouseEvent evt) {
        // TODO 将在此处添加您的处理代码：//鼠標放開
        this.selectBgimg();
    }

    private void jlbMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO 将在此处添加您的处理代码：//鼠標點擊
        // this.selectBgimg();

    }

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {
        // TODO 将在此处添加您的处理代码：//失去焦點

    }

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {
        // TODO 将在此处添加您的处理代码：//得到焦點
        this.selectBgimg();

    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        // TODO 将在此处添加您的处理代码：//儅窗口改變大小

        this.selectBgimg();
    }

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {
        // TODO 将在此处添加您的处理代码：//儅窗口移動
        // if(this.tf=false){
        // this.selectBgimg();
        // }
    }

    private void selectBgimg() {// 截屏代码
        // this.tf=true;
        try {
            Point p = this.getLocation();// 移動方式,用p
            this.setLocation(-2000, -2000);// 移動方式
            // this.setVisible(false);//隱藏方式，用this
            Robot rbt = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();

            // 截圖
            BufferedImage background = rbt.createScreenCapture(new Rectangle((int) p.getX() + 1, (int) p.getY() + 1,
                    this.getWidth() - 2, this.getHeight() - 2));
            background = rbt.createScreenCapture(new Rectangle((int) p.getX() + 1, (int) p.getY() + 1,
                    this.getWidth() - 2, this.getHeight() - 2));
            background = rbt.createScreenCapture(new Rectangle((int) p.getX() + 1, (int) p.getY() + 1,
                    this.getWidth() - 2, this.getHeight() - 2));
            background = rbt.createScreenCapture(new Rectangle((int) p.getX() + 1, (int) p.getY() + 1,
                    this.getWidth() - 2, this.getHeight() - 2));
            // 截圖

            float[] data = {
                    // 0.0625f,0.125f,0.0625f,
                    // 0.125f,0.125f,0.125f,
                    // 0.0625f,0.125f,0.0625f,

                    // 毛玻璃效果算子
                    0.0625f, 0.125f, 0.0625f, 0.125f, 0.125f, 0.125f, 0.0625f, 0.125f, 0.0625f,

            // 0.170f,0.06f,0.170f,
            // 0.06f,0.06f,0.06f,
            // 0.170f,0.06f,0.170f,
            };
            Kernel kernel = new Kernel(3, 3, data);
            ConvolveOp co = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            BufferedImage background2 = null;
            background2 = co.filter(background, background2);

            ImageIcon bg = new ImageIcon(background2);
            jlb.setIcon(bg);

            // this.setVisible(true);//隱藏方式
            this.setLocation(p);// 移動方式
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "透明效果發生錯誤，程序無法啓動，錯誤信息如下：\n" + ex.toString());
        }
        // this.tf=false;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FloatWindow().setVisible(true);
            }
        });
    }

    // 变量声明 - 不进行修改
    private javax.swing.JLabel jlb;
    // 变量声明结束

}
