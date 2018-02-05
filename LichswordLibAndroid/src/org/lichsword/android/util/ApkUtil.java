package org.lichsword.android.util;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

public class ApkUtil {

    public static int INSTALLED = 0; // 表示已经安装，且跟现在这个apk文件是一个版本
    public static int UNINSTALLED = 1; // 表示未安装
    public static int INSTALLED_UPDATE = 2; // 表示已经安装，版本比现在这个版本要低，可以点击按钮更新

    /**
     * 取得主程序version name
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            String packageName = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            if (null == packageInfo) {
                return null;
            }
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 判断设备是否存在外部存储设备如: SDCard
     * 
     * @return 设备已经挂载为true, 否则为false
     */
    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断包名所对应的应用是否安装在SD卡上
     * 
     * @param packageName
     * @return, true if install on SD card
     */
    public static boolean isInstallOnSDCard(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo appInfo;
        try {
            appInfo = pm.getApplicationInfo(packageName, 0);

            if ((appInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                return true;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断该应用在手机中的安装情况
     * 
     * @param context
     * @param packageName
     *            要判断应用的包名
     * @param versionCode
     *            要判断应用的版本号
     * @return 应用是否在手机上已经安装过，有以下集中情况出现 1.未安装，这个时候按钮应该是“安装”点击按钮进行安装
     *         2.已安装，按钮显示“已安装” 可以卸载该应用 3.已安装，但是版本有更新，按钮显示“更新” 点击按钮就安装应用
     */
    public static int installedType(Context context, String packageName, int versionCode) {
        final PackageManager pm = context.getPackageManager();
        final List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (final PackageInfo pi : pakageinfos) {
            String pi_packageName = pi.packageName;
            int pi_versionCode = pi.versionCode;
            // 如果这个包名在系统已经安装过的应用中存在
            if (packageName.endsWith(pi_packageName)) {
                if (versionCode == pi_versionCode) {
                    return INSTALLED;
                } else if (versionCode > pi_versionCode) {
                    return INSTALLED_UPDATE;
                }
            }
        }

        return UNINSTALLED;
    }

    public static PackageInfo checkPackage(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(packageName, 0);
        } catch (Exception e) {
        }
        if (pInfo == null) {
            return null;
        }
        return pInfo;
    }

}
