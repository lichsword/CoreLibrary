package org.lichsword.android;

import java.io.File;

import org.lichsword.android.biz.ImageManager;
import org.lichsword.android.util.LogHelper;
import org.lichsword.android.util.PhoneUtil;
import org.lichsword.android.util.StorageUtil;

import android.app.Application;
import android.content.pm.ApplicationInfo;

/**
 * @author wangyue.wy
 */
public class BaseApplication extends Application {

    public static final String TAG = BaseApplication.class.getSimpleName();

    public static final File ROOT_DIR = new File(StorageUtil.getExternalStorageDirectory(), "app_name");

    public static BaseApplication sInstance;

    public static int[] sScreenSize;

    public static float sDensity;

    public static boolean sDebug = false;

    @Override
    public void onCreate() {
        /* enable/disable LogHelper according to debug flag in manifest */
        ApplicationInfo appInfo = getApplicationInfo();
        if (appInfo != null) {
            boolean debug = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            sDebug = debug;
            LogHelper.isNeedLog = debug;
            LogHelper.enableTag = debug;
        }

        super.onCreate();

        sInstance = this;
        sScreenSize = PhoneUtil.getScreenSize(this);
        LogHelper.d(TAG, String.format("screen size = %dx%d", sScreenSize[0], sScreenSize[1]));
        sDensity = PhoneUtil.getDensity(this);

        // do some VIP initial work
        ImageManager.sInit(this);
    }

    @Override
    public void onTerminate() {
        LogHelper.d(TAG, "xhh core application on ter");
        /* TODO: add unregister code here */
        super.onTerminate();
    }

}
