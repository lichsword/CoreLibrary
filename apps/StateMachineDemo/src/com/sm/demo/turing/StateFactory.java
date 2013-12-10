package com.sm.demo.turing;

public class StateFactory {

    private static StateFactory sInstance;

    private StateFactory() {
        // do nothing
    }

    public static StateFactory getInstance() {
        if (null == sInstance) {
            sInstance = new StateFactory();
        }// end if
        return sInstance;
    }

}
