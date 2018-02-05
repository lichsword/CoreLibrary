package org.lichsword.android.util;

import java.io.File;

import org.lichsword.java.io.FileUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableUtil {

    public static final String TAG = DrawableUtil.class.getSimpleName();

    /**
     * 
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable createDrawableFromBitmap(Context context, Bitmap bitmap) {
        if (null != bitmap) {
            return new BitmapDrawable(context.getResources(), bitmap);
        } else {
            return null;
        }
    }

    /**
     * 
     * @param file
     * @return
     */
    public static Drawable createDrawableFromFile(File file) {
        if (file.exists()) {
            return Drawable.createFromPath(file.getAbsolutePath());
        } else {
            return null;
        }
    }

    /**
     * 
     * @param filePath
     * @return
     */
    public static Drawable createDrawableFromPath(String filePath) {
        if (FileUtil.isFileExist(filePath)) {
            return createDrawableFromFile(new File(filePath));
        } else {
            return null;
        }
    }

    /**
     * 
     * @param context
     * @param id
     * @return
     */
    public static Drawable createDrawableFromResId(Context context, int id) {
        return context.getResources().getDrawable(id);
    }
}
