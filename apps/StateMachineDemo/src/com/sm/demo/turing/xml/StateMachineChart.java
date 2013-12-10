package com.sm.demo.turing.xml;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class StateMachineChart extends JFrame {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    StateMachineChart frame = new StateMachineChart();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
