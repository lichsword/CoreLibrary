package org.lichsword.android.util.log;

import java.util.ArrayList;
import java.util.List;

import org.lichsword.android.util.HttpClient;
import org.lichsword.android.util.ImageManager;

import android.util.Log;

public class LogHelper {

    private static String TAG = LogHelper.class.getSimpleName();

    // TODO 发布前必须手动关闭
    private static boolean isNeedLog = true;

    // TODO 发布前必须手动关闭
    private static boolean enableTag = true;

    // TODO
    private static List<String> tags = new ArrayList<String>();

    public static void disable() {
        isNeedLog = false;
        enableTag = false;
    }

    public static void addTags(String tag) {
        tags.add(tag);
    }

    static {
        tags.add(ImageManager.TAG);
        tags.add(HttpClient.TAG);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (enableTag) {
            if (isNeedLog && tags.contains(tag)) {
                Log.i(tag, msg);
            }
        } else {
            if (isNeedLog) {
                Log.i(tag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (enableTag) {
            if (isNeedLog && tags.contains(tag)) {
                Log.e(tag, msg);
            }
        } else {
            if (isNeedLog) {
                Log.e(tag, msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        if (enableTag) {
            if (isNeedLog && tags.contains(tag)) {
                Log.d(tag, msg);
            }
        } else {
            if (isNeedLog) {
                Log.d(tag, msg);
            }
        }
    }

    public static void w(String tag, String msg) {
        if (enableTag) {
            if (isNeedLog && tags.contains(tag)) {
                Log.w(tag, msg);
            }
        } else {
            if (isNeedLog) {
                Log.w(tag, msg);
            }
        }
    }

    public static void v(String tag, String msg) {
        if (enableTag) {

            if (isNeedLog && tags.contains(tag)) {
                Log.v(tag, msg);
            }
        } else {
            if (isNeedLog) {
                Log.v(tag, msg);
            }
        }
    }
}
