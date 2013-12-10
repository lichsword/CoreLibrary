package org.lichsword.android.util.apk;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.lichsword.library.R;

/**
 * @author yuewang
 * 
 */
public class ApkUtil {

    /**
     * 判断目标promotion package name是否已经安装
     * 
     * @param context
     * @param packageName
     * @return true if has install, otherwise return false.
     */
    public static boolean isInstalled(Context context, String packageName) {
        boolean installed = false;

        if (null == context || TextUtils.isEmpty(packageName)) {
            return false;
        }

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            if (null != packageInfo) {
                installed = true;
            }// end if
        } catch (NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * Install APK.
     * 
     * @param context
     * @param apkFilePath
     */
    public static void install(Context context, String apkFilePath) {
        install(context, new File(apkFilePath));
    }

    /**
     * Install APK.
     * 
     * @param context
     * @param apkFile
     */
    public static void install(Context context, File apkFile) {
        if (null != apkFile) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            context.startActivity(intent);
        } else {
            // log
        }
    }

    /**
     * 
     * 添加快捷方式到桌面 要点：
     * 
     * 1.给Intent指定action="com.android.launcher.INSTALL_SHORTCUT"
     * 
     * 2.给定义为Intent.EXTRA_SHORTCUT_INENT的Intent设置与安装时一致的action(必须要有)
     * 
     * 3.添加权限:com.android.launcher.permission.INSTALL_SHORTCUT
     */
    public static void addShortcutToDesktop(Context context) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重建
        shortcut.putExtra("duplicate", false);
        // 设置名字
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        // 设置图标
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(context, context.getClass()).setAction(Intent.ACTION_MAIN));
        // 发送广播
        context.sendBroadcast(shortcut);
    }

    /**
     * 取得程序名称
     * 
     * @param context
     * @return string of application name
     */
    public static String getAppName(Context context) {
        // the parameter ask for simple construct and simple adsContent
        String appName = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            int id = applicationInfo.labelRes;
            if (id != 0) {
                appName = context.getResources().getString(id);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            appName = null;
        }

        return appName;
    }

    /**
     * 取得主程序包名
     * 
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 取得主程序version code
     * 
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getVersionCode(context, context.getPackageName());
    }

    /**
     * 取得指定package name程序的version code
     * 
     * @param context
     * @param packageName
     * @return
     */
    public static int getVersionCode(Context context, String packageName) {
        int versionCode = 0;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            if (null == packageInfo) {
                return 0;
            }
            versionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 取得主程序version name
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            if (null == packageInfo) {
                return null;
            }
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static final int UNKNOWN_RESOURCE_ID = 0;

    /**
     * 通过布局资源文件名称来查找布局文件资源ID
     * 
     * @param context
     * @param layoutName
     * @return resourceId which match layoutName. Returns 0 if no such resource
     *         was found.
     */
    public static int findLayoutIdByName(Context context, final String layoutName) {
        int resourceId = UNKNOWN_RESOURCE_ID;
        try {
            resourceId = context.getResources().getIdentifier(layoutName, "style", context.getPackageName());
        } catch (final Exception e) {
            e.printStackTrace();
            resourceId = UNKNOWN_RESOURCE_ID;
        }
        return resourceId;
    }

    /**
     * 通过Drawable资源文件名称来查找Drawable文件资源ID
     * 
     * @param context
     * @param drawableName
     * @return resourceId which match drawableName. Returns 0 if no such
     *         resource was found.
     */
    public static int findDrawableIdByName(Context context, final String drawableName) {
        int resourceId = UNKNOWN_RESOURCE_ID;
        try {
            resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        } catch (final Exception e) {
            e.printStackTrace();
            resourceId = UNKNOWN_RESOURCE_ID;
        }
        return resourceId;
    }

    /**
     * 通过 raw 资源文件名称来查找 raw 文件资源ID
     * <p>
     * 未通过单元测试
     * </p>
     * 
     * @param context
     * @param drawableName
     * @return resourceId which match drawableName. Returns 0 if no such
     *         resource was found.
     */
    @Deprecated
    public static int findRawIdByName(Context context, final String drawableName) {
        int resourceId = UNKNOWN_RESOURCE_ID;
        try {
            resourceId = context.getResources().getIdentifier(drawableName, "raw", context.getPackageName());
        } catch (final Exception e) {
            e.printStackTrace();
            resourceId = UNKNOWN_RESOURCE_ID;
        }
        return resourceId;
    }

    public static final char CHAR_MINUS_SIGN = '-';
    public static final char CHAR_HYPHEN = '_';

    /**
     * 如果首字符是'-'，则换成'_'
     * 
     * @return
     */
    public static String formatHashcodeNameToResName(String hashcodeName) {
        return hashcodeName.replace(CHAR_MINUS_SIGN, CHAR_HYPHEN);
    }

    /**
     * 如果首字符是'_'，则换成'-'
     * 
     * @return
     */
    public static String recoverResNameToHashcodeName(String resourceName) {
        return resourceName.replace(CHAR_HYPHEN, CHAR_MINUS_SIGN);
    }

    /**
     * 检测配置文件是否开启debug mode
     * 
     * @param context
     * @return
     */
    public static boolean isDebugModeEnable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * 杀死自身进程
     */
    public static void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 获取指定ApplicationInfo的图标
     * 
     * @param context
     * @param applicationInfo
     * @return
     */
    public static Drawable getAppIconDrawable(Context context, ApplicationInfo applicationInfo) {
        PackageManager pm = context.getPackageManager();
        return applicationInfo.loadIcon(pm);
    }

    /**
     * 获取指定ApplicationInfo的图标
     * 
     * @param context
     * @param packageInfo
     * @return
     */
    public static Drawable getAppIconDrawable(Context context, PackageInfo packageInfo) {
        return getAppIconDrawable(context, packageInfo.applicationInfo);
    }

    /**
     * 获取指定package name程序的图标
     * 
     * @param context
     * @param packageName
     * @return
     */
    public static Drawable getAppIconDrawable(Context context, String packageName) {
        Drawable result = null;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            if (null == packageInfo) {
                return null;
            }
            result = getAppIconDrawable(context, packageInfo);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取本程序的指定 mete data 参数信息
     * 
     * @param context
     * @param key
     * @return
     */
    public static String getMetedataValue(Context context, String key) {
        return getMetedataValue(context, context.getPackageName(), key);
    }

    /**
     * 获取指定包名程序的 mete data 参数信息
     * 
     * @param context
     * @param packageName
     * @param key
     * @return
     */
    public static String getMetedataValue(Context context, String packageName, String key) {
        String result = null;
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            result = null;
        }
        if (null != applicationInfo) {
            if (applicationInfo.metaData != null) {
                result = applicationInfo.metaData.getString(key);
            }// end if
        }// end if
        return result;
    }

    /**
     * 获取所有程序
     * 
     * @param context
     * @return
     */
    public static List<PackageInfo> getInstalledPackages(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
        return packages;
    }

    private static final String URI_SHARE = "mailto:";

    private static final String URI_TYPE = "text/plain";

    private static final String URI_EMAIL_CLIENT = "Choose Email Client";

    /**
     * Share application throw email/SMS/Blue tooth...
     * 
     * @param context
     * @param subjcet
     * @param body
     */
    public static void share(Context context, String subjcet, String body) {
        Uri mUri = Uri.parse(URI_SHARE);
        Intent intent = new Intent(Intent.ACTION_SEND, mUri);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
        intent.setType(URI_TYPE);
        intent.putExtra(Intent.EXTRA_SUBJECT, subjcet);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_CC, new String[] { "" });
        context.startActivity(Intent.createChooser(intent, URI_EMAIL_CLIENT));
    }

    /**
     * Minimize application(equal user press PAGE_HOME key).
     * 
     * @param activity
     * @return
     */
    public static boolean minimizeApplication(Activity activity) {
        boolean success = true;
        if (null == activity) {
            success = false;
        } else {
            success = true;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            activity.startActivity(intent);
        }
        return success;
    }

    /**
     * <p>
     * 在软键盘弹出之前，设置键盘显示为隐藏。
     * </p>
     * <p>
     * 注意：若是软键盘已经显示，则无法通过调用此方法来收起键盘。
     * </p>
     * 
     * @param activity
     * @return false if parameter activity is null.
     */
    public static boolean setSoftKeyboardHideState(Activity activity) {
        boolean success = true;
        if (null != activity) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } else {
            success = false;
        }
        return success;
    }

    /**
     * 关闭软键盘
     * 
     * @param activity
     * @return
     */
    public static boolean hideSoftKeyboard(Activity activity) {
        boolean success = true;
        if (null != activity) {
            InputMethodManager manager = ((InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE));
            if (null != manager) {
                try {
                    success = manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException e) {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        return success;
    }

    /**
     * 显示软键盘
     * 
     * @param activity
     * @param view
     * @return
     */
    public static boolean showSoftKeyboard(Activity activity, View view) {
        boolean success = true;
        InputMethodManager manager = ((InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE));
        if (null != manager) {
            success = manager.showSoftInput(view, 0);
        } else {
            success = false;
        }
        return success;
    }

    /**
     * Keep activity always weak up.
     * 
     * @param activity
     * @return
     */
    public static boolean keepDeviceWeakup(Activity activity) {
        if (null != activity) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            return true;
        } else {
            return false;
        }

    }

    /**
     * 隐藏状态栏
     * 
     * @param activity
     * @return
     */
    public static boolean hideStatebar(Activity activity) {
        if (null != activity) {
            Window window = activity.getWindow();
            // clear flag first.
            window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            // set new flag.
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 隐藏状态栏
     * 
     * @param activity
     * @return
     */
    public static boolean showStatebar(Activity activity) {
        if (null != activity) {
            Window window = activity.getWindow();
            // clear flag first.
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // set new flag.
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 启动三方应用
     * 
     * @param context
     * @param packageName
     *            三方应用的主包名
     * @param activityClassName
     *            Activity的绝对 URI
     * @return true if success, otherwise return false.
     */
    public static boolean invokeApp(Context context, String packageName, String activityClassName) {
        if (TextUtils.isEmpty(packageName) || TextUtils.isEmpty(activityClassName)) {
            return false;
        }// end if

        ComponentName componentName = new ComponentName(packageName, activityClassName);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(componentName);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
