package org.lichsword.android.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.lichsword.java.io.FileUtil;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

/**
 * Created by vliux on 8/7/13.
 */
public class ImageUtil {

    public static final String TAG = ImageUtil.class.getSimpleName();

    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;

    public static void recycleBitmap(Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = null;
    }

    public static BitmapFactory.Options optionSave() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return options;
    }

    public static BitmapFactory.Options optionDefault() {
        return new BitmapFactory.Options();
    }

    /**
     * <p>
     * Use default compress is Bitmap.ComoressFormat.JPEG to save file size.
     * </p>
     * <p>
     * Default will lost alpha channel.
     * </p>
     * 
     * @param url
     * @param saveFile
     * @return
     */
    public static Bitmap downloadImage(final String url, File saveFile) {
        return downloadImage(url, saveFile, DEFAULT_COMPRESS_FORMAT);
    }

    /**
     * 
     * @param url
     * @param saveFile
     * @param compressFormat
     *            JPEG/PNG
     *            <p>
     *            JPEG will lost alpha channel but got litter file in disk).
     *            </p>
     *            <p>
     *            PNG will keep alpha.
     *            </p>
     * @return
     */
    public static Bitmap downloadImage(final String url, File saveFile, Bitmap.CompressFormat compressFormat) {
        if (null == url || url.length() <= 0) {
            throw new IllegalArgumentException();
        }// end if

        Bitmap bmp = HttpClient.downloadImage(url, saveFile);
        try {
            if (null != saveFile) {
                saveBmp(bmp, saveFile.getParentFile(), FileUtil.getFileNameWithExtension(saveFile.getAbsolutePath()),
                        ImageUtil.QUALITY_GOOD, compressFormat);
            }// end if
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap decodeSampledBitmap(String filePath, int reqWidth, int reqHeight, BitmapFactory.Options options) {
        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap decodeSampledBitmap(AssetManager am, String assetFile, int reqWidth, int reqHeight,
            BitmapFactory.Options options) {
        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        try {
            InputStream is = am.open(assetFile);
            BitmapFactory.decodeStream(is, null, options);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        try {
            InputStream is = am.open(assetFile);
            Bitmap bmp = BitmapFactory.decodeStream(am.open(assetFile), null, options);
            is.close();
            return bmp;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createScaledBitmap(Bitmap src, int width, int height) {
        LogHelper.d("msg", "ruibo: createScaledBitmap " + width + "x" + height);
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        float scaleX = (float) width / srcWidth;
        float scaleY = (float) height / srcHeight;
        float scale = Math.max(scaleX, scaleY);
        float scaledWidth = scale * srcWidth;
        float scaledHeight = scale * srcHeight;
        float dx = (width - scaledWidth) / 2;
        float dy = (height - scaledHeight) / 2;
        Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
        Canvas canvas = new Canvas(ret);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        matrix.postTranslate(dx, dy);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(src, matrix, paint);
        return ret;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static final int QUALITY_GOOD = 90;
    public static final int QUALITY_OK = 60;

    /**
     * 
     * @param bmp
     * @param dir
     * @param fileName
     * @param quality
     *            jpeg quality, 0-100
     * @return
     * @throws IOException
     */
    public static String saveBmp(Bitmap bmp, File dir, String fileName, int quality) throws IOException {
        return saveBmp(bmp, dir, fileName, quality, DEFAULT_COMPRESS_FORMAT);
    }

    /**
     * 
     * @param bmp
     * @param dir
     * @param fileName
     * @param quality
     *            jpeg quality, 0-100
     * @param compressFormat
     * @return
     * @throws IOException
     */
    public static String saveBmp(Bitmap bmp, File dir, String fileName, int quality,
            Bitmap.CompressFormat compressFormat) throws IOException {
        if (null == fileName || fileName.length() <= 0) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
        }
        if (null == compressFormat) {
            compressFormat = DEFAULT_COMPRESS_FORMAT;
        }

        File saveFile = new File(dir.getAbsolutePath(), fileName);
        String path = saveFile.getCanonicalPath();
        LogHelper.i("Adi", "saving cropped bitmap file to " + path);
        OutputStream fOut = null;
        BufferedOutputStream bfOut = null;
        if (quality < 0) {
            quality = 90; // default
        }

        try {
            fOut = new FileOutputStream(saveFile);
            bfOut = new BufferedOutputStream(fOut);
            bmp.compress(compressFormat, quality, bfOut);
            bfOut.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogHelper.e("Adi", "unable to open file " + path);
            throw new IOException();
        } catch (IOException e) {
            e.printStackTrace();
            LogHelper.e("Adi", "failed to save image to file " + path);
            throw e;
        } finally {
            if (null != bfOut) {
                bfOut.close();
            }
        }
        return path;

    }

    /**
     * ruibo: this function will blur and dark input bitmap
     * 
     * @param bitmap
     *            input bitmap MUST be mutable
     * @param radius
     *            radius can not be lesser than width or height of input bitmap
     * @param darkLevel
     *            0..100
     */
    public static void optimizedBoxBlur(Bitmap bitmap, int radius, int darkLevel) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap tmp = Bitmap.createBitmap(width, height, bitmap.getConfig());
        MyPixel total = new MyPixel();
        int t;
        // blur horizontal
        for (int y = 0; y < height; ++y) {
            total.reset();
            for (int kx = -radius; kx <= radius; ++kx) {
                t = kx < 0 ? 0 : kx;
                total.plus(bitmap.getPixel(t, y));
            }
            tmp.setPixel(0, y, total.toDivInt(radius * 2 + 1));
            for (int x = 1; x < width; ++x) {
                t = x - radius - 1;
                t = t < 0 ? 0 : t;
                total.minus(bitmap.getPixel(t, y));
                t = x + radius;
                t = t >= width ? width - 1 : t;
                total.plus(bitmap.getPixel(t, y));
                tmp.setPixel(x, y, total.toDivInt(radius * 2 + 1));
            }
        }

        // blur vertical
        for (int x = 0; x < width; ++x) {
            total.reset();
            for (int ky = -radius; ky <= radius; ++ky) {
                t = ky < 0 ? 0 : ky;
                total.plus(tmp.getPixel(x, t));
            }
            bitmap.setPixel(x, 0, total.toDivInt(radius * 2 + 1));
            for (int y = 1; y < height; ++y) {
                t = y - radius - 1;
                t = t < 0 ? 0 : t;
                total.minus(tmp.getPixel(x, t));
                t = y + radius;
                t = t >= height ? height - 1 : t;
                total.plus(tmp.getPixel(x, t));
                bitmap.setPixel(x, y, total.toDivInt(radius * 2 + 1));
            }
        }
        // memory recycle
        recycleBitmap(tmp);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(255 * darkLevel / 100, 0, 0, 0);
    }

    static class MyPixel {
        int mR = 0;
        int mG = 0;
        int mB = 0;

        public void reset() {
            mR = 0;
            mG = 0;
            mB = 0;
        }

        public void plus(int color) {
            mR += Color.red(color);
            mG += Color.green(color);
            mB += Color.blue(color);
        }

        public void minus(int color) {
            mR -= Color.red(color);
            mG -= Color.green(color);
            mB -= Color.blue(color);
        }

        public int toDivInt(int x) {
            return Color.argb(255, mR / x, mG / x, mB / x);
        }
    }

    /**
     * ruibo: this function will blur and dark input bitmap
     * 
     * @param bitmap
     *            input bitmap MUST be mutable
     * @param radius
     *            radius can not be lesser than width or height of input bitmap
     * @param darkLevel
     *            0..100
     */
    public static void gaussianBlur(Bitmap bitmap, int radius, int darkLevel) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap tmp = Bitmap.createBitmap(width, height, bitmap.getConfig());
        GaussianSlideWindow total = new GaussianSlideWindow(radius);
        int t;
        // blur horizontal
        for (int y = 0; y < height; ++y) {
            total.reset();
            for (int kx = -radius; kx <= radius; ++kx) {
                t = kx < 0 ? 0 : kx;
                total.add(bitmap.getPixel(t, y));
            }
            tmp.setPixel(0, y, total.toColor());
            for (int x = 1; x < width; ++x) {
                total.remove();
                t = x + radius;
                t = t >= width ? width - 1 : t;
                total.add(bitmap.getPixel(t, y));
                tmp.setPixel(x, y, total.toColor());
            }
        }
        // blur vertical
        for (int x = 0; x < width; ++x) {
            total.reset();
            for (int ky = -radius; ky <= radius; ++ky) {
                t = ky < 0 ? 0 : ky;
                total.add(tmp.getPixel(x, t));
            }
            bitmap.setPixel(x, 0, total.toColor());
            for (int y = 1; y < height; ++y) {
                total.remove();
                t = y + radius;
                t = t >= height ? height - 1 : t;
                total.add(tmp.getPixel(x, t));
                bitmap.setPixel(x, y, total.toColor());
            }
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(255 * darkLevel / 100, 0, 0, 0);
    }

    static class GaussianSlideWindow {

        float[] matrix;
        int size = 0;

        class FloatRGB {
            float r, g, b;

            FloatRGB(float r, float g, float b) {
                this.r = r;
                this.g = g;
                this.b = b;
            }
        }

        LinkedList<FloatRGB> slideWindow;

        public GaussianSlideWindow(int radius) {
            size = radius * 2 + 1;
            // init rgb array
            slideWindow = new LinkedList<FloatRGB>();

            // init matrix
            matrix = new float[size];
            float sigma = radius / 3;
            float sigma22 = 2 * sigma * sigma;
            float sqrtSigmaPi2 = (float) Math.sqrt(2 * Math.PI * sigma);
            float total = 0;
            int index = 0;
            for (int i = -radius; i <= radius; i++) {
                float distance = i * i;
                matrix[index] = (float) Math.exp(-(distance) / sigma22) / sqrtSigmaPi2;
                total += matrix[index];
                index++;
            }
            for (int i = 0; i < size; i++)
                matrix[i] /= total;
        }

        public void reset() {
            slideWindow.clear();
        }

        public void add(int color) {
            float r = Color.red(color);
            float g = Color.green(color);
            float b = Color.blue(color);
            slideWindow.addLast(new FloatRGB(r, g, b));
        }

        public void remove() {
            slideWindow.removeFirst();
        }

        public int toColor() {
            float r = 0;
            float g = 0;
            float b = 0;
            int i = 0;
            FloatRGB p;
            Iterator iterator = slideWindow.iterator();
            while (iterator.hasNext()) {
                p = (FloatRGB) iterator.next();
                r += p.r * matrix[i];
                g += p.g * matrix[i];
                b += p.b * matrix[i];
                i++;
            }
            return Color.argb(255, ((int) r) & 0xff, ((int) g) & 0xff, ((int) b) & 0xff);
        }
    }

    public static Bitmap screenShot(View srcView) {
        if (null == srcView) {
            return null;
        }// end if

        int srcWidth = srcView.getWidth();
        int srcHeight = srcView.getHeight();
        Bitmap srcBitmap = null;
        try {
            srcBitmap = Bitmap.createBitmap(srcWidth, srcHeight, Config.ARGB_8888);
        } catch (Exception e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
        Canvas srcCanvas = new Canvas(srcBitmap);
        srcCanvas.drawColor(0xffffffff);
        srcView.draw(srcCanvas);
        return srcBitmap;
    }
}
