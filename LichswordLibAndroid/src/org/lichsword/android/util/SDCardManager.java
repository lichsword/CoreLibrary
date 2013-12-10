package org.lichsword.android.util;

import java.io.File;

import org.lichsword.android.util.log.LogHelper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;

/**
 * SDCardManager of First Aid.
 *
 * @author yzhwei
 *
 */
public class SDCardManager {

	private static final String TAG = "SDCardManager";
	private static final int BASE_AVAILABLE_SIZE = 10;
	public static final int TIP_NO_SDCARD = 0;
	public static final int TIP_APK_NOT_INSTALLED_IN_SDCARD = 1;
	public static final int TIP_SDCARD_LACK_SPACE = 2;

	public static int check(Context context) {
		if (null == context) {
			throw new IllegalArgumentException("context mustn't be null.");
		}
		if (!isSDCardPrepared()) {
			return TIP_NO_SDCARD;
		}
		if (isAPKInstalledInSDCard(context)) {
			return -1;
		}
		LogHelper.i(TAG, "available size:" + getSDCardAvailableSize());
		return getSDCardAvailableSize() > BASE_AVAILABLE_SIZE ? TIP_APK_NOT_INSTALLED_IN_SDCARD
				: TIP_SDCARD_LACK_SPACE;

	}

	public static boolean isSDCardPrepared() {
		return Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment
				.getExternalStorageState());
	}

	public static boolean isAPKInstalledInSDCard(Context context) {
		if (null == context) {
			return false;
		}
		PackageManager pManager = context.getPackageManager();
		try {
			ApplicationInfo aInfo = pManager.getApplicationInfo(
					context.getPackageName(),
					PackageManager.GET_UNINSTALLED_PACKAGES);
			String sourceDir = aInfo.sourceDir;
			File file = Environment.getExternalStorageDirectory();
			if (file.exists()) {
				String fileParent = file.getParent();
				LogHelper.i(TAG, "sourceDir : " + sourceDir);
				LogHelper.i(TAG, "fileParent : " + fileParent);
				if (sourceDir.contains(fileParent)) {
					return true;
				}
			}
		} catch (NameNotFoundException e) {
			// TODO Add exception handling here.
			e.printStackTrace();
		}
		return false;
	}

	public static int getSDCardCapacity() {

		if (!isSDCardPrepared()) {
			return -1;
		}
		File file = Environment.getExternalStorageDirectory();
		if (null == file || !file.exists()) {
			return -1;
		}
		StatFs statFs = new StatFs(file.getPath());
		int sdCardBlockSize = statFs.getBlockSize();
		int sdCardTotalBlocks = statFs.getBlockCount();
		int sdCardSize = sdCardTotalBlocks * sdCardBlockSize;
		sdCardSize = sdCardSize / (1024 * 1024);
		return sdCardSize;

	}

	public static int getSDCardAvailableSize() {
		if (!isSDCardPrepared()) {
			return -1;
		}
		File file = Environment.getExternalStorageDirectory();
		if (null == file || !file.exists()) {
			return -1;
		}
		StatFs statFs = new StatFs(file.getPath());
		LogHelper.i(TAG, "MAX INT : " + Integer.MAX_VALUE);
		LogHelper.i(TAG, "MAX LONG : " + Long.MAX_VALUE);
		long sdCardBlockSize = statFs.getBlockSize();
		LogHelper.i(TAG, "sdCardBlockSize: " + sdCardBlockSize);
		long sdCardAvailableBlocks = statFs.getAvailableBlocks();
		LogHelper.i(TAG, "sdCardAvailableBlocks: " + sdCardAvailableBlocks);
		long sdCardAvailableSize = sdCardAvailableBlocks * sdCardBlockSize;
		LogHelper.i(TAG, "sdCardAvailableSize: " + sdCardAvailableSize);
		sdCardAvailableSize = sdCardAvailableSize / (1024 * 1024);
		LogHelper.i(TAG, "sdCardAvailableSize: " + sdCardAvailableSize);
		return (int) sdCardAvailableSize;
	}

}
