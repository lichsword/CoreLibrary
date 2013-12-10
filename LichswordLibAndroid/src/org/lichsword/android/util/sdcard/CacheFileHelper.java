package org.lichsword.android.util.sdcard;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.lichsword.android.util.log.LogHelper;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.text.TextUtils;

public class CacheFileHelper {
    public final static String TAG = "CacheFileHelper";
    public final static String SUFFIX_SP = ".sp";
    public final static String ALBUM_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/album/";

    public static final boolean confirmDir() {
        File file = new File(ALBUM_PATH);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static String getSuffixByLink(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        int index = url.lastIndexOf(".");
        if (index == -1) {
            return null;
        }

        return url.substring(index);
    }

    public static String getCacheFileName(String url) {
        String suffix = getSuffixByLink(url);
        if (TextUtils.isEmpty(suffix)) {
            return null;
        }
        return url.hashCode() + suffix;
    }

    public static String getCacheFileAbsolutePath(String url) {
        return ALBUM_PATH + getCacheFileName(url);
    }

    public static File getCacheFile(String url) {
        File file = new File(getCacheFileAbsolutePath(url));
        return file;
    }

    public static boolean isCacheFileExist(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        String suffix = getSuffixByLink(url);
        if (TextUtils.isEmpty(suffix)) {
            return false;
        }

        File file = getCacheFile(url);
        if (null != file) {
            return file.exists();
        }
        return false;
    }

    /**
     * 解析图片资源，判断图片有效性&完整性
     * 
     * @param name
     * @return
     */
    public static Bitmap fetchImage(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }

        File file = new File(getCacheFileAbsolutePath(imageUrl));
        if (!file.exists()) {
            return null;
        }

        Bitmap result = null;
        Options options = new Options();
        options.inPurgeable = true;
        try {
            FileInputStream is = new FileInputStream(file);
            // result = BitmapFactory.decodeStream(is);
            result = BitmapFactory.decodeStream(is, null, options);
            is.close();
            // BitmapFactory.decodeStream(is);
            // result = BitmapFactory.decodeFile(path, options);

        } catch (Exception e) {
            // TODO Add exception handling here.
            e.printStackTrace();
            recyleBitmap(result);
            // LoadImagesManager.releaseImageThumbs();
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            e.printStackTrace();
            recyleBitmap(result);
        }
        return result;

    }

    public static InputStream fetchGifImage(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        File file = new File(ALBUM_PATH + name);
        if (!file.exists()) {
            return null;
        }
        try {
            return new FileInputStream(file);
            // result = BitmapFactory.decodeStream(is);

        } catch (Exception e) {
            // TODO Add exception handling here.
            e.printStackTrace();
            // LoadImagesManager.releaseImageThumbs();
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;

    }

    public static void recyleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static boolean delFile(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        File file = getCacheFile(url);
        return file.delete();
    }

    /**
     * 加载asset中的splash window默认图片
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap readBitmapFromAsset(Context context, String fileName) {
        if (null == context || TextUtils.isEmpty(fileName)) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            final AssetManager asset = context.getAssets();
            final InputStream is = asset.open(fileName);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getFilePath(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        return ALBUM_PATH + fileName;
    }

    public static String getFileRootPath() {
        return ALBUM_PATH;
    }

    public static String getSuffixFromURL(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String suffix = "";
        int index = url.lastIndexOf(".");
        if (index > 0) {
            suffix = url.substring(index);
        }
        return suffix;
    }

    public static boolean reNameFile(String filePath, String fromName,
            String toName) {
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fromName)
                || TextUtils.isEmpty(fromName)) {
            return false;
        }

        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            return false;
        }
        File fileFrom = new File(filePath + fromName);
        if (!fileFrom.exists()) {
            return false;
        }

        File fileTo = new File(filePath + toName);
        if (fileTo.exists()) {
            return false;
        }

        boolean successed = false;
        try {
            successed = fileFrom.renameTo(fileTo);
            LogHelper.i(TAG, "rename " + filePath + fromName + " to " + toName
                    + " , successed:" + successed);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        if (!successed) {
            fileFrom.delete();
        }

        return successed;
    }

    public static boolean hideFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            file = file.getParentFile();
        }

        // file.

        File hideFile = new File(file.getPath() + File.separator + ".nomedia");
        if (!hideFile.exists()) {
            try {
                SecurityManager securityManager = new SecurityManager();
                securityManager.checkWrite(file.getPath());
                return hideFile.createNewFile();
            } catch (Exception e) {
                // TODO Add exception handling here.
                e.printStackTrace();
            }
        }

        return false;
    }

}
