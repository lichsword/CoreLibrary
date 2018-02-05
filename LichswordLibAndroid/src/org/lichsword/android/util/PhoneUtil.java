package org.lichsword.android.util;

import java.io.IOException;

import org.lichsword.android.BaseApplication;
import org.lichsword.java.io.FileUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.lichswordlibandroid.R;

public class PhoneUtil {

    /**
     * 取得手机屏幕的显示度量(宽/高/...)
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 取得手机的屏幕宽度
     * 
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        return dm.widthPixels;
    }

    /**
     * 取得手机的屏幕高度
     * 
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        return dm.heightPixels;
    }

    /**
     * 以数组返回手机屏幕的大小
     * 
     * @param activity
     * @return int[width, height]
     */
    public static int[] getScreenSize(Activity activity) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        int[] size = new int[2];
        size[0] = dm.widthPixels;
        size[1] = dm.heightPixels;
        return size;
    }

    /**
     * 以数组返回手机屏幕的大小
     * 
     * @param context
     * @return int[width, height]
     */
    public static int[] getScreenSize(Context context) {
        int[] screenSize = new int[2];
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            screenSize[0] = dm.widthPixels;
            screenSize[1] = dm.heightPixels;
        }
        return screenSize;
    }

    /**
     * 取得手机屏幕的方向
     * 
     * @return Configuration.ORIENTATION_LANDSCAPE,
     *         Configuration.ORIENTATION_PORTRAIT,
     *         Configuration.ORIENTATION_SQUARE,
     *         Configuration.ORIENTATION_UNDEFINED 之一
     */
    public static int getOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    /**
     * 默认分辨率为1.5f
     */
    private static final float DEFAULT_DENSITY = 1.5f;

    /**
     * 默认 dpi 为 160
     */
    private static final int DEFAULT_DPI = 160;

    /**
     * 取得手机的分辨率(density)
     * 
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        float resultDensity = DEFAULT_DENSITY;
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            resultDensity = dm.density;
        }
        return resultDensity;
    }

    /**
     * 取得手机的 dpi(densityDpi)
     * 
     * @param context
     * @return
     */
    public static int getDensityDpi(Context context) {
        int ret = DEFAULT_DPI;
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            ret = dm.densityDpi;
        }
        return ret;
    }

    /**
     * Get phone model name.
     * 
     * @return string of phone model, e.g."Nexus S", "MI 2"(小米2)...
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 手机代号"小米2"
     */
    public static final String MODEL_NAME_MI_2 = "MI 2";

    public static Bitmap retrieveWallpaperBitmap(Context context) {
        Bitmap result = null;

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        Drawable drawable = wallpaperManager.getDrawable();
        if (null == drawable) {
            return null;
        }// end if

        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap wallpaperBitmap = bitmapDrawable.getBitmap();

        int[] screen = getScreenSize(context);
        final int screenWidth = screen[0];
        final int screenHeight = screen[1];
        final int wallpaperWidth = wallpaperBitmap.getWidth();
        final int wallpaperHeight = wallpaperBitmap.getHeight();

        if (wallpaperWidth > screenWidth) {// 壁纸的宽度大于屏幕宽度
            if (wallpaperHeight >= screen[1]) {
                // 壁纸的高度不小于屏幕
                result = Bitmap.createBitmap(wallpaperBitmap, (wallpaperWidth - screenWidth) / 2, 0, screenWidth,
                        screenHeight);
            } else {
                // 壁纸的高度小于屏幕
                // TODO
            }
        } else if (wallpaperWidth == screenWidth) {// 壁纸的宽度等于屏幕宽度
            if (wallpaperHeight >= screen[1]) {
                // 壁纸的高度不小于屏幕
                result = Bitmap.createBitmap(wallpaperBitmap, 0, 0, screenWidth, screenHeight);
            } else {
                // 壁纸的高度小于屏幕
                // TODO
            }
        } else {// 壁纸的宽度比屏幕小
            if (wallpaperHeight >= screen[1]) {
                // 壁纸的高度不小于屏幕
                int clipHeight = wallpaperWidth * screenHeight / screenWidth;
                Bitmap clipBitmap = Bitmap.createBitmap(wallpaperBitmap, 0, 0, wallpaperWidth, clipHeight);
                result = Bitmap.createScaledBitmap(clipBitmap, screenWidth, screenHeight, false);
                clipBitmap.recycle();
                clipBitmap = null;
            } else {
                result = Bitmap.createScaledBitmap(wallpaperBitmap, screenWidth, screenHeight, false);
            }
        }

        // still can't recicle.
        // if (null != wallpaperBitmap && !wallpaperBitmap.isRecycled()) {
        // wallpaperBitmap.recycle();
        // wallpaperBitmap = null;
        // }// end if

        return result;
    }

    public static Drawable retrieveWallpaperDrawale(Context context) {
        Drawable result = null;
        Bitmap bitmap = retrieveWallpaperBitmap(context);
        if (null != bitmap) {
            result = new BitmapDrawable(null, bitmap);
        }// end if
        return result;
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
    public static void addShortcutToDesktop(Context context, Intent shortcutIntent) {
        Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // duplicate extra is ineffective in some api version
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        context.sendBroadcast(addIntent);
    }

    /**
     * ruibo: 本函数需要 com.android.launcher.permission.UNINSTALL_SHORTCUT 权限
     */
    public static void removeShortcutDesktop(Context context, Intent shortcutIntent) {
        Intent removeIntent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // duplicate extra is ineffective in some api version
        removeIntent.putExtra("duplicate", false);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        context.sendBroadcast(removeIntent);
    }

    /**
     * 判断是否已有快捷方式
     * 
     * @return
     */
    public static boolean shortcutExists(Context context) {
        boolean result = false;
        String title = context.getString(R.string.app_name);

        final String uriStr;
        if (android.os.Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        final Uri CONTENT_URI = Uri.parse(uriStr);
        final Cursor c = context.getContentResolver().query(CONTENT_URI, null, "title=?", new String[] { title }, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        }
        return result;
    }

    public static String getMacAddress() {
        String[] pathes = new String[] { "/sys/class/net/wlan0/address", "/sys/class/net/eth0/address" };

        for (String path : pathes) {
            try {
                String mac = FileUtil.readFile(path);
                if (null != mac) {
                    return mac.replaceAll("[\n\r]", "");
                } else {
                    return null;
                }
            } catch (IOException e) {
                continue;
            }
        }
        return null;
    }

    public static boolean packageExists(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }// end if

        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) BaseApplication.sInstance
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (null != telephonyManager) {
            String imei = telephonyManager.getDeviceId();
            if (null != imei && imei.length() > 0) {
                return imei;
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void disableHardwareAcceleration(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
}