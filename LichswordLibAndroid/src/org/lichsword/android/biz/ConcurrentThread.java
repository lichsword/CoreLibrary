package org.lichsword.android.biz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentThread {

    private static final int MAX_THREAD_COUNT = 3;
    /**
     * 并发线程执行器(默认线程数为3)
     */
    public static ExecutorService sExecutorService = Executors.newFixedThreadPool(MAX_THREAD_COUNT);

}
