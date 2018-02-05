package com.lichsword.t3d.lib1;

public class LogHelper {

    /**
     * @param args
     */
    public static void main(String[] args) {
        d("Lich", "show me the money");
    }

    public static void d(String msg) {
        d("", msg);
    }

    public static void d(String tag, String msg) {
        System.out.println(tag + '|' + msg);
    }

}
