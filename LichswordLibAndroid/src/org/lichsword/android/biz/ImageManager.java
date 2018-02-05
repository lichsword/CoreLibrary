package org.lichsword.android.biz;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.lichsword.android.AppConstants;
import org.lichsword.android.util.ImageUtil;
import org.lichsword.android.util.LogHelper;
import org.lichsword.java.io.FileUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

public final class ImageManager {

    public static final String TAG = ImageManager.class.getSimpleName();

    private static final String DEFAULT_IMG_CACHE_DIR = "adi_pic";

    private static ImageManager sInstance;

    private final Context mContext;
    private final SparseArray<WeakReference<Bitmap>> mImagesRefs = new SparseArray<WeakReference<Bitmap>>();
    private final SparseArray<ImageDownloadTask> mTasks = new SparseArray<ImageDownloadTask>();
    private final SparseArray<ImageDownloadRunnable> mThreadsArray = new SparseArray<ImageDownloadRunnable>();
    private final ArrayList<ImageDownloadListener> mListeners = new ArrayList<ImageDownloadListener>();
    private final int mDensityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
    private File mCacheDir = null;
    private File mRootDir = null;
    private final Map<String, File> mCacheDirMap;

    public static interface ImageDownloadListener {
        /**
         * Download of an image is completed.
         * 
         * @param url
         *            the url of the downloaded image.
         * @param success
         *            true if success, false otherwise.
         */
        void onDownloadCompleted(String url, boolean success);
    }

    private final class ImageDownloadTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUrl;
        private final String mSubDir;
        private final File mCacheFile;
        private final Bitmap.CompressFormat mCompressFormat;

        ImageDownloadTask(final String url, String subDir, Bitmap.CompressFormat compressFormat) {
            mUrl = url;
            mSubDir = subDir;
            mCacheFile = getImageCacheFile(url, subDir);
            mCompressFormat = compressFormat;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(final Void... params) {
            final boolean result = downloadBitmap(mUrl, mCacheFile, mCompressFormat);
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Boolean result) {
            finishTask(mUrl, mSubDir, result);
        }

    }

    private final class ImageDownloadRunnable implements Runnable {

        private final String mUrl;
        private final String mSubDir;
        private final File mCacheFile;
        private final Bitmap.CompressFormat mCompressFormat;

        ImageDownloadRunnable(final String url, String subDir, Bitmap.CompressFormat format) {
            mUrl = url;
            mSubDir = subDir;
            mCacheFile = getImageCacheFile(url, subDir);
            mCompressFormat = format;
        }

        @Override
        public void run() {
            final boolean result = downloadBitmap(mUrl, mCacheFile, mCompressFormat);
            if (result) {
                LogHelper.i(TAG, "download bitmap...Success");
            } else {
                LogHelper.w(TAG, "download bitmap...Failed");
            }
            LogHelper.d("url=" + mUrl);
            finishThread(mUrl, mSubDir);
        }
    }

    private ImageManager(final Context context) {
        mContext = context;
        mCacheDirMap = new HashMap<String, File>();
        ensureCacheDir();
    }

    /**
     * Initialize the only instance of {@linkplain ImageManager}.
     * 
     * @param context
     *            the context of the application.
     * @return the only instance of instance
     */
    public static ImageManager sInit(final Context context) {
        if (null == sInstance) {
            synchronized (ImageManager.class) {
                if (null == sInstance) {
                    sInstance = new ImageManager(context);
                }// end if
            }
        }// end if
        return sInstance;
    }

    public static void recycle() {
        sInstance = null;
    }

    public static String getCacheDir() {
        return DEFAULT_IMG_CACHE_DIR;// default image cache director;
    }

    /**
     * Free the image currently used by {@code ImageView}, and sets a drawable
     * as the adsContent of the {@code ImageView}.
     * 
     * <p>
     * The ImageView must have an application specified ID.
     * </p>
     * 
     * @param imageView
     *            the {@linkplain ImageView} to change the adsContent.
     * @param resId
     *            the resource identifier of the the drawable
     */
    public static final void setImageViewImageResouce(final ImageView imageView, final int resId) {
        if (null == imageView) {
            return;
        }
        if (View.NO_ID == imageView.getId() || 0 == imageView.getId()) {
            throw new IllegalArgumentException("The ImageView must have and application specified ID.");
        }
        freeImageViewImage(imageView);
        imageView.setImageResource(resId);
        markImageViewFreeable(imageView, false);
    }

    /**
     * Free the image currently used by {@code ImageView}, and sets a
     * {@code Bitmap} as the adsContent of {@code ImageView}.
     * <p>
     * The ImageView must have an application specified ID.
     * </p>
     * 
     * @param imageView
     *            the {@linkplain ImageView} to change the adsContent.
     * @param bitmap
     *            The bitmap to set
     */
    public static final void setImageViewImageBitmap(final ImageView imageView, final Bitmap bitmap) {
        if (null == imageView) {
            return;
        }
        if (View.NO_ID == imageView.getId() || 0 == imageView.getId()) {
            throw new IllegalArgumentException("The ImageView must have and application specified ID.");
        }
        freeImageViewImage(imageView);
        imageView.setImageBitmap(bitmap);
        markImageViewFreeable(imageView, true);
    }

    /**
     * Free the image currently used by {@code ImageView}, and sets a drawable
     * as the adsContent of {@code ImageView}.
     * <p>
     * The ImageView must have an application specified ID.
     * </p>
     * <p>
     * The Drawable cannot be an Resource drawable.
     * </p>
     * 
     * @param imageView
     *            the {@linkplain ImageView} to change the adsContent.
     * @param drawable
     *            The drawable to set
     */
    public static final void setImageViewImageDrawable(final ImageView imageView, final Drawable drawable) {
        if (null == imageView) {
            return;
        }
        if (View.NO_ID == imageView.getId() || 0 == imageView.getId()) {
            throw new IllegalArgumentException("The ImageView must have and application specified ID.");
        }
        freeImageViewImage(imageView);
        imageView.setImageDrawable(drawable);
        markImageViewFreeable(imageView, true);
    }

    /**
     * Free the image currently used by {@code ImageView}, and sets the
     * adsContent of this {@code ImageView} to the specified Uri.
     * <p>
     * The ImageView must have an application specified ID.
     * </p>
     * 
     * @param imageView
     *            the {@linkplain ImageView} to change the adsContent.
     * @param uri
     *            The Uri of an image
     */
    public static final void setImageViewImageURI(final ImageView imageView, final String uri) {
        setImageViewImageURI(imageView, Uri.parse(uri));
    }

    /**
     * Free the image currently used by {@code ImageView}, and sets the
     * adsContent of this {@code ImageView} to the specified Uri.
     * 
     * @param imageView
     *            the {@linkplain ImageView} to change the adsContent.
     * @param uri
     *            The Uri of an image
     */
    public static final void setImageViewImageURI(final ImageView imageView, final Uri uri) {
        if (null == imageView) {
            return;
        }
        if (View.NO_ID == imageView.getId() || 0 == imageView.getId()) {
            throw new IllegalArgumentException("The ImageView must have and application specified ID.");
        }
        freeImageViewImage(imageView);
        imageView.setImageURI(uri);
        markImageViewFreeable(imageView, true);
    }

    /**
     * Retrieve the cache file for the give url.
     * 
     * @param url
     *            the url of the image.
     * @return the cache {@link File}, or null if url is invalid.
     */
    public File getImageCacheFile(final String url) {
        return getImageCacheFile(url, getCacheDir());
    }

    public File getImageCacheFile(final String url, String subDir) {
        if (!TextUtils.isEmpty(url)) {
            return new File(getSubDir(subDir), String.format("%08x.cache", url.hashCode()));
        }
        return null;
    }

    /**
     * Loads an remote image into a {@link ImageView}.
     * 
     * @param url
     *            the url to load.
     * @param imageView
     *            the {@link ImageView} to load into.
     * @param placeHolderResId
     *            the resource image when the URL is invalid or image is not
     *            valid.
     */
    public void loadRemoteImageForImageView(final String url, final ImageView imageView, final int placeHolderResId) {
        if (null == imageView) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            // For invalid URL, just use the place holder.
            setImageViewImageResouce(imageView, placeHolderResId);
        } else {
            final File cacheFile = getImageCacheFile(url);
            if (cacheFile.exists() && isImageFileValidInternal(cacheFile)) {
                // Image is cached and valid, use the cached image.
                setImageViewImageURI(imageView, Uri.parse(cacheFile.toString()));
            } else {
                // Image is not cached or invalid, download it.
                downloadBitmapAsync(url);
                setImageViewImageResouce(imageView, placeHolderResId);
            }
        }
    }

    /**
     * Loads an remote image into a {@link ImageView}.
     * 
     * @param url
     *            the url to load.
     * @param imageView
     *            the {@link ImageView} to load into.
     * @param placeHolderResId
     *            the resource image when the URL is invalid or image is not
     *            valid.
     */
    public void loadRemoteImageForImageView2(final String url, final ImageView imageView, final int placeHolderResId) {
        if (null == imageView) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            // For invalid URL, just use the place holder.
            imageView.setImageResource(placeHolderResId);
        } else {
            final File cacheFile = getImageCacheFile(url);
            if (cacheFile.exists() && isImageFileValidInternal(cacheFile)) {
                // Image is cached and valid, use the cached image.
                imageView.setImageURI(Uri.parse(cacheFile.toString()));
            } else {
                // Image is not cached or invalid, download it.
                downloadBitmapAsync(url);
                imageView.setImageResource(placeHolderResId);
            }
        }
    }

    /**
     * Retrieve the cache file for the give url.
     * 
     * @param url
     *            the url of the image.
     * @return the cache {@link File}, or null if url is invalid.
     */
    public boolean isImageCacheValid(final String url) {
        boolean isImageCached = false;
        boolean isImageFileExist = false;
        final File cacheFile = getImageCacheFile(url);
        final SparseArray<WeakReference<Bitmap>> refs = mImagesRefs;
        if (null != cacheFile) {
            final int key = cacheFile.getPath().hashCode();
            final WeakReference<Bitmap> bitmapRef = refs.get(key);
            if (null != bitmapRef) {
                final Bitmap cacheImage = bitmapRef.get();
                if (null != cacheImage) {
                    isImageCached = true;
                }
            }
            isImageFileExist = cacheFile.exists() && cacheFile.length() != 0;
        }
        return isImageFileExist || isImageCached;
    }

    /**
     * Retrieve the cache file for the give url.
     * 
     * @param url
     *            the url of the image.
     * @return the cache {@link File}, or null if url is invalid.
     */
    public boolean isImageCached(final String url) {
        return isImageCached(url, getCacheDir());
    }

    public boolean isImageCached(final String url, String subDir) {
        final File cacheFile = getImageCacheFile(url, subDir);
        if (null != cacheFile) {
            return isImageFileValid(cacheFile);
        }
        return false;
    }

    /**
     * Determine whether the image file is valid.
     * 
     * @param file
     *            the image file.
     * @return true if the file exits and can be decoded, false otherwise.
     */
    public boolean isImageFileValid(final File file) {
        if (file != null && file.exists()) {
            return isImageFileValidInternal(file);
        }
        return false;
    }

    /**
     * Retrieve the only instance of {@linkplain ImageManager}.
     * 
     * @return the only instance of instance
     */
    public static ImageManager getInstance() {
        if (null == sInstance) {
            throw new IllegalStateException("Call ImageManager.init() first.");
        }
        return sInstance;
    }

    /**
     * Ensure the cache directory.
     */
    public boolean ensureCacheDir() {
        mRootDir = AppConstants.ROOT_DIR;
        if (mRootDir != null) {
            mCacheDir = getSubDir(getCacheDir());
            mCacheDirMap.put(getCacheDir(), mCacheDir);
        }// end if
        return mCacheDir != null;
    }

    /**
     * Add a download frameListener
     * 
     * @param frameListener
     *            the frameListener to receive download events.
     */
    public final void addListener(final ImageDownloadListener listener) {
        synchronized (mListeners) {
            if (!mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }
    }

    /**
     * Remove a download frameListener
     * 
     * @param frameListener
     *            the frameListener to remove.
     */
    public final void removeListener(final ImageDownloadListener listener) {
        synchronized (mListeners) {
            mListeners.remove(listener);
        }
    }

    /**
     * Remove all download listeners.
     */
    public final void clearListeners() {
        synchronized (mListeners) {
            mListeners.clear();
        }
    }

    public File getSubDir(String subDir) {
        if (null == subDir || "".equals(subDir)) {
            subDir = getCacheDir();
        }
        if (mCacheDirMap.containsKey(subDir)) {
            return mCacheDirMap.get(subDir);
        }
        if (mRootDir != null) {
            final File cacheDir = new File(mRootDir, subDir);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mCacheDirMap.put(subDir, cacheDir);
            return cacheDir;
        }
        return null;
    }

    /**
     * Clean the cache directory.
     */
    public void cleanCache() {
        FileUtil.cleanDir(mCacheDir);
    }

    public void cleanCache(String subDir, long delta) {
        LogHelper.d(TAG, "clean cache " + subDir + "...");
        long timesTamp = new Date().getTime() - delta * 24 * 60 * 60 * 1000;
        File dir = getSubDir(subDir);
        if (null != dir) {
            File[] files = dir.listFiles();
            if (null == files) {
                return;
            }
            for (File file : files) {
                long lastModified = file.lastModified();
                if (lastModified < timesTamp) {
                    boolean ret = file.delete();
                    LogHelper.d(TAG, subDir + " clean delete " + ret + "," + file.getAbsolutePath());
                }
            }
        }

    }

    /**
     * 内部会先按修改时间排序，先删除旧文件，后删除新文件
     * 
     * @param subDir
     *            指定缓存子目录
     * @param saveLimit
     *            至少保留残留文件大小
     */
    public void cleanCache(String subDir, int saveLimit) {
        LogHelper.d(TAG, "clean cache " + subDir + "...");
        File dir = getSubDir(subDir);
        if (null != dir) {
            File[] files = dir.listFiles();
            if (null == files) {
                return;
            }
            int size = files.length;
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    return (int) (f1.lastModified() - f2.lastModified());
                }
            });
            int deleteSize = size - saveLimit;
            for (int i = 0; i < deleteSize; i++) {
                boolean ret = files[i].delete();
                LogHelper.d(TAG, subDir + " clean delete " + ret + "," + files[i].getAbsolutePath());
            }
        }
    }

    /**
     * Retrieve the context.
     * 
     * @return the value of context
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Loads the bitmap from local file.
     * 
     * @param path
     *            the path to the image file.
     * @return an {@linkplain Bitmap} object.
     */
    public Bitmap loadBitmap(final String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        final int key = path.hashCode();
        final SparseArray<WeakReference<Bitmap>> refs = mImagesRefs;
        final WeakReference<Bitmap> bitmapRef = refs.get(key);
        Bitmap bitmap = null;
        if (null != bitmapRef) {
            bitmap = bitmapRef.get();
        }
        if (null == bitmap) {
            bitmap = decodeBitmapInternal(path, key, refs);
        }
        return bitmap;
    }

    /**
     * Load the bitmap from memory && cache file by the given image URL
     * 
     * @param url
     * @return the bitmap object from cache array or file
     */
    public Bitmap loadBitmapByUrl(final String url) {
        return loadBitmapByUrl(url, getCacheDir());
    }

    public Bitmap loadBitmapByUrl(final String url, String subDir) {
        Bitmap cachedImage = null;
        final File cacheFile = getImageCacheFile(url, subDir);
        if (null != cacheFile) {
            cachedImage = loadBitmap(cacheFile.getPath());
        }// end if
        return cachedImage;
    }

    /**
     * 通用Android的AsyncTask类进行UI主线程发起的下载任务<br/>
     * 适用于UI可视化进度&结果的下载，在非UI线程的下载请参考{@link downloadBitmapThread}<br/>
     * 比如：下载图标完成后立即显示
     * <p>
     * 注意：在非UI线程中调用，会引发初始化AsyncTask失败的异常，无法try-catch
     * </p>
     * 
     * @param url
     *            the URL of the image.
     */
    public void downloadBitmapAsync(final String url) {
        downloadBitmapAsync(url, getCacheDir(), ImageUtil.DEFAULT_COMPRESS_FORMAT);
    }

    /**
     * 通用Android的AsyncTask类进行UI主线程发起的下载任务<br/>
     * 适用于UI可视化进度&结果的下载，在非UI线程的下载请参考{@link downloadBitmapThread}<br/>
     * 比如：下载图标完成后立即显示
     * <p>
     * 注意：在非UI线程中调用，会引发初始化AsyncTask失败的异常，无法try-catch
     * </p>
     * 
     * @param url
     * @param subDir
     * @param compressFormat
     */
    public void downloadBitmapAsync(final String url, String subDir, Bitmap.CompressFormat compressFormat) {
        if (TextUtils.isEmpty(url)) {
            return;
        }// end if
        ensureTask(url, subDir, compressFormat);
    }

    /**
     * 通过java原生线程下载（内含线程数目上限安全处理）bitmap<br/>
     * 适用于后台（非UI线程）下载，在UI线程的下载请参考{@link downloadBitmapAsync}
     * 
     * @see downloadBitmapAsync
     * 
     * @param url
     * @param subDir
     * @param compressFormat
     */
    public void downloadBitmapThread(final String url, String subDir, Bitmap.CompressFormat compressFormat) {
        if (TextUtils.isEmpty(url)) {
            return;
        }// end if
        enqueueThread(url, subDir, compressFormat);
    }

    /**
     * Loads the bitmap from web.
     * 
     * @return true if the bitmap has download success, false otherwise.
     */
    public boolean downloadBitmap(final String url, File saveToFile, Bitmap.CompressFormat compressFormat) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }// end if
        if (null == saveToFile) {
            saveToFile = getImageCacheFile(url);
        }// end if
        LogHelper.d(TAG, String.format("Downloading image from %s to %s.", url, saveToFile.toString()));
        // Bitmap bitmap = HttpClient.downloadImage(url, saveToFile);
        Bitmap bitmap = ImageUtil.downloadImage(url, saveToFile, compressFormat);
        boolean success = false;
        if (null != bitmap) {
            success = true;
            WeakReference<Bitmap> imageReference = new WeakReference<Bitmap>(bitmap);
            mImagesRefs.put(saveToFile.getPath().hashCode(), imageReference);
        }// end if
        LogHelper.d(
                TAG,
                String.format("Downloaded image from %s to %s, RESULT = %s.", url, saveToFile.toString(),
                        String.valueOf(success)));
        return success;
    }

    /**
     * download bitmap from web and save to default file
     * 
     * @param url
     * @return true if the bitmap is downloaded, false otherwise.
     */
    public boolean downloadBitmap(final String url) {
        return downloadBitmap(url, null, ImageUtil.DEFAULT_COMPRESS_FORMAT);
    }

    /**
     * Bind URL & default drawable to ImageView, which can auto download image
     * file from URL, and also set default image at necessary time.
     * 
     * @param imageView
     *            视图上的图片控件
     * @param url
     *            远程服务器上的图片 URL
     * @param defaultDrawable
     *            指定默认图片，若为null，则不会调用setImageDrawable()方法。
     */
    public void bindUrlToImageView(ImageView imageView, String url, Drawable defaultDrawable) {
        bindUrlToImageView(imageView, url, defaultDrawable, getCacheDir(), ImageUtil.DEFAULT_COMPRESS_FORMAT);
    }

    /**
     * Bind URL & default drawable to ImageView, which can auto download image
     * file from URL, and also set default image at necessary time.
     * 
     * @param imageView
     *            视图上的图片控件
     * @param url
     *            远程服务器上的图片 URL
     * @param defaultDrawable
     *            指定默认图片，若为null，则不会调用setImageDrawable()方法。
     * @param subDir
     *            指定图片缓存的子目录
     */

    public void bindUrlToImageView(ImageView imageView, String url, Drawable defaultDrawable, String subDir) {
        bindUrlToImageView(imageView, url, defaultDrawable, subDir, ImageUtil.DEFAULT_COMPRESS_FORMAT);
    }

    /**
     * Bind URL & default drawable to ImageView, which can auto download image
     * file from URL, and also set default image at necessary time.
     * 
     * @param imageView
     *            视图上的图片控件
     * @param url
     *            远程服务器上的图片 URL
     * @param defaultDrawable
     *            指定默认图片，若为null，则不会调用setImageDrawable()方法。
     * @param subDir
     *            指定图片缓存的子目录
     * @param format
     *            保存图片时的压缩方式
     */
    public void bindUrlToImageView(ImageView imageView, String url, Drawable defaultDrawable, String subDir,
            Bitmap.CompressFormat format) {
        Bitmap bitmap = ImageManager.getInstance().loadBitmapByUrl(url, subDir);
        if (null != bitmap) {
            // LogHelper.d(TAG, "has file cache");
            imageView.setImageBitmap(bitmap);
        } else {
            // LogHelper.e(TAG, "no memory cache");
            if (null != defaultDrawable) {
                imageView.setImageDrawable(defaultDrawable);
            }
            ImageManager.getInstance().downloadBitmapAsync(url, subDir, format);
        }
    }

    /**
     * Remove a task from the download queue, and notify listeners.
     * 
     * @param url
     *            the url of the image.
     * @param result
     *            the result.
     */
    protected void finishTask(final String url, final String subDir, final boolean result) {
        synchronized (mTasks) {
            final int key = url.hashCode() + subDir.hashCode();
            mTasks.remove(key);
        }
        notifyDownloadCompleted(url, result);
    }

    protected void finishThread(final String url, final String subDir) {
        synchronized (mThreadsArray) {
            final int key = url.hashCode() + subDir.hashCode();
            mThreadsArray.remove(key);
        }
    }

    /**
     * Notify listeners that download is completed.
     * 
     * @param url
     *            the url of the image.
     * @param result
     *            true if success, false otherwise.
     */
    protected final void notifyDownloadCompleted(final String url, final boolean result) {
        synchronized (mListeners) {
            final ArrayList<ImageDownloadListener> listeners = mListeners;
            for (final ImageDownloadListener listener : listeners) {
                if (null != listener) {
                    listener.onDownloadCompleted(url, result);
                }
            }
        }
    }

    /**
     * Ensure a task to download image.
     * 
     * @param url
     *            to url of the image to download.
     */
    protected void ensureTask(final String url) {
        ensureTask(url, getCacheDir(), ImageUtil.DEFAULT_COMPRESS_FORMAT);
    }

    protected void ensureTask(final String url, String subDir, Bitmap.CompressFormat compressFormat) {
        synchronized (mTasks) {
            final int key = url.hashCode() + subDir.hashCode();
            ImageDownloadTask task = mTasks.get(key);
            if (null == task) {
                task = new ImageDownloadTask(url, subDir, compressFormat);
                mTasks.put(key, task);
                task.execute();
            }
        }
    }

    protected void enqueueThread(final String url, String subDir, Bitmap.CompressFormat compressFormat) {
        synchronized (mTasks) {
            final int key = url.hashCode() + subDir.hashCode();
            ImageDownloadRunnable runnable = mThreadsArray.get(key);
            if (null == runnable) {
                LogHelper.i(TAG, "not cache in threads map");
                LogHelper.i(TAG, "Create new runnable...START");
                runnable = new ImageDownloadRunnable(url, subDir, compressFormat);
                mThreadsArray.put(key, runnable);
                ConcurrentThread.sExecutorService.submit(runnable);
            } else {
                LogHelper.w(TAG, "has in thread cache map.");
            }
        }
    }

    private Bitmap decodeBitmapInternal(final String path, final int key, final SparseArray<WeakReference<Bitmap>> refs) {
        WeakReference<Bitmap> bitmapRef;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path);
        } catch (OutOfMemoryError e) {
            System.gc();
        }

        if (bitmap != null) {
            bitmap.setDensity(mDensityDpi);
            bitmapRef = new WeakReference<Bitmap>(bitmap);
            refs.put(key, bitmapRef);
        }
        return bitmap;
    }

    private static void freeImageViewImage(final ImageView imageView) {
        if (isImageViewFreeable(imageView)) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                imageView.setImageDrawable(null);
                final BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null) {
                    bitmap.recycle();
                }
                bitmap = null;
            }
        }
    }

    private static void markImageViewFreeable(final ImageView imageView, final boolean freeable) {
        imageView.setTag(imageView.getId(), freeable);
    }

    private static boolean isImageViewFreeable(final ImageView imageView) {
        final Object tag = imageView.getTag(imageView.getId());
        if (tag != null) {
            return (Boolean) tag;
        } else {
            return false;
        }
    }

    private boolean isImageFileValidInternal(final File cacheFile) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(cacheFile.toString(), options);
        if (options.outWidth > 0 && options.outHeight > 0 && !TextUtils.isEmpty(options.outMimeType)) {
            return true;
        } else {
            cacheFile.delete();
        }
        return false;
    }
}
