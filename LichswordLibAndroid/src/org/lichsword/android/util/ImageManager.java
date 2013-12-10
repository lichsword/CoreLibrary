package org.lichsword.android.util;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.lichsword.android.util.log.LogHelper;
import org.lichsword.android.util.net.NetworkUtil;
import org.lichsword.android.util.sdcard.StorageUtil;

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

    /**
     * Listener used to watch download events.
     * 
     * @author chzhong
     * 
     */
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

    public static final String TAG = ImageManager.class.getSimpleName();

    private final class ImageDownloadTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUrl;
        private final File mCacheFile;

        ImageDownloadTask(final String url) {
            mUrl = url;
            mCacheFile = getImageCacheFile(url);
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(final Void... params) {
            final boolean result = downloadBitmap(mUrl, mCacheFile);
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Boolean result) {
            finishTask(mUrl, result);
        }

    }

    private static ImageManager sInstance;
    public static final String KEY_PROP_CACHE_DIR = "cache.dir";

    /**
     * Initialize the only instance of {@linkplain ImageManager}.
     * 
     * @param context
     *            the context of the application.
     * @return the only instance of instance
     */
    public static synchronized ImageManager sInit(final Context context) {
        if (null == sInstance) {
            sInstance = new ImageManager(context);
        }
        return sInstance;
    }

    public static void recycle() {
        sInstance = null;
    }

    public static String getCacheDir() {
        // String cacheDir = System.getProperty(KEY_PROP_CACHE_DIR);
        // if (TextUtils.isEmpty(cacheDir)) {
        // cacheDir = "yisou";
        // }
        return "yisou";// cacheDir;
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

    /**
     * Retrieve the cache file for the give url.
     * 
     * @param url
     *            the url of the image.
     * @return the cache {@link File}, or null if url is invalid.
     */
    public File getImageCacheFile(final String url) {
        if (!TextUtils.isEmpty(url)) {
            return new File(mCacheDir, String.format("%08x.cache", url.hashCode()));
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
        final File cacheFile = getImageCacheFile(url);
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

    /**
     * Remove a task from the download queue, and notify listeners.
     * 
     * @param url
     *            the url of the image.
     * @param result
     *            the result.
     */
    protected void finishTask(final String url, final boolean result) {
        synchronized (mTasks) {
            final int key = url.hashCode();
            mTasks.remove(key);
        }
        notifyDownloadCompleted(url, result);
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

    /**
     * Ensure a task to download image.
     * 
     * @param url
     *            to url of the image to download.
     */
    protected void ensureTask(final String url) {
        synchronized (mTasks) {
            final int key = url.hashCode();
            ImageDownloadTask task = mTasks.get(key);
            if (null == task) {
                task = new ImageDownloadTask(url);
                mTasks.put(key, task);
                task.execute();
            }
        }
    }

    /**
     * Retrieve the only instance of {@linkplain ImageManager}.
     * 
     * @return the only instance of instance
     */
    public static synchronized ImageManager getInstance() {
        if (null == sInstance) {
            throw new IllegalStateException("Call ImageManager.init() first.");
        }
        return sInstance;
    }

    private final Context mContext;
    private final SparseArray<WeakReference<Bitmap>> mImagesRefs = new SparseArray<WeakReference<Bitmap>>();
    private final SparseArray<ImageDownloadTask> mTasks = new SparseArray<ImageDownloadTask>();
    private final ArrayList<ImageDownloadListener> mListeners = new ArrayList<ImageDownloadListener>();
    private final int mDensityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
    private File mCacheDir;

    private ImageManager(final Context context) {
        mContext = context;
        ensureCacheDir();
    }

    /**
     * Ensure the cache directory.
     */
    public boolean ensureCacheDir() {
        boolean result = true;

        final File storageDir = StorageUtil.getExternalStorageDirectory();
        if (storageDir != null) {
            final File cacheDir = new File(storageDir, getCacheDir());
            if (!cacheDir.exists()) {
                result = cacheDir.mkdirs();
            }
            mCacheDir = cacheDir;
        }

        return result;
    }

    /**
     * Clean the cache directory.
     */
    public void cleanCache() {
        FileSystemUtil.cleanDir(mCacheDir);
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
        Bitmap cachedImage = null;
        final File cacheFile = getImageCacheFile(url);
        if (null != cacheFile) {
            cachedImage = loadBitmap(cacheFile.getPath());
        }// end if
        return cachedImage;
    }

    /**
     * Loads the bitmap from web at background.
     * 
     * @param url
     *            the URL of the image.
     */
    public void downloadBitmapAsync(final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ensureTask(url);
    }

    /**
     * Loads the bitmap from web.
     * 
     * @return true if the bitmap is downloaded, false otherwise.
     */
    public boolean downloadBitmap(final String url, File saveToFile) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (null == saveToFile) {
            saveToFile = getImageCacheFile(url);
        }
        LogHelper.d(TAG, String.format("Downloading image from %s to %s.", url, saveToFile.toString()));
        Bitmap bitmap = HttpClient.downloadImage(url, saveToFile);
        boolean success = false;
        if (null != bitmap) {
            success = true;
            WeakReference<Bitmap> imageReference = new WeakReference<Bitmap>(bitmap);
            mImagesRefs.put(saveToFile.getPath().hashCode(), imageReference);
        }
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
        return downloadBitmap(url, null);
    }

    private Bitmap decodeBitmapInternal(final String path, final int key, final SparseArray<WeakReference<Bitmap>> refs) {
        WeakReference<Bitmap> bitmapRef;
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            bitmap.setDensity(mDensityDpi);
            bitmapRef = new WeakReference<Bitmap>(bitmap);
            refs.put(key, bitmapRef);
        }
        return bitmap;
    }

    /**
     * Bind URL & default drawable to ImageView, which can auto download image
     * file from URL, and also set default image at necessary time.
     * 
     * @param imageView
     * @param url
     * @param defaultDrawable
     */
    public void bindUrlToImageView(ImageView imageView, String url, Drawable defaultDrawable) {

        Bitmap bitmap = ImageManager.getInstance().loadBitmapByUrl(url);
        if (null != bitmap) {
            LogHelper.d(TAG, "has file cache");
            imageView.setImageBitmap(bitmap);
        } else {
            LogHelper.e(TAG, "no memory cache");
            imageView.setImageDrawable(defaultDrawable);
            if (NetworkUtil.isNetworkAvailable(mContext)) {
                ImageManager.getInstance().downloadBitmapAsync(url);
            } else {
                LogHelper.v(TAG, "network is miss");
            }
        }
    }
}
