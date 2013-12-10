package org.lichsword.android.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lichsword.android.util.log.LogHelper;
import org.lichsword.android.util.sdcard.CacheFileHelper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

/**
 * TaskManager of Image BE.
 *
 * @author yzhwei
 *
 */
public class LoadImagesManager {

	private static final String TAG_LOG = "LoadImagesManager";
	private static final int MAX_THUMBS_COUNT = 500;
	private static final int DEFAULT_START_LOADIMAGE_INDEX = 0;
	private static final int DEFAULT_LOADIMAGE_COUNT = 30;

	private static LoadImageListener mListener;
	private static boolean isLoad = true;

	private ThumbDownloadAsy mAsy;

	private static List<String> mFolderRecords = new ArrayList<String>();
	private static List<String> mImageRecords = new ArrayList<String>();
	private static LinkedHashMap<Integer, String> tasks = new LinkedHashMap<Integer, String>();
	private static Map<String, ThumbDownloadAsy> runningTask = new HashMap<String, ThumbDownloadAsy>();
	// private TaskInfo currentTaskInfo = null;
	private static boolean isBusy = false;
	private static int maxTask = 1;
	private static int taskCount = 0;
	private static boolean clearing = false;

	private static Context mContext;
	private static LoadImagesManager manager;

	/**
	 * Set the value of mListener.
	 *
	 * @param mListener
	 *            the mListener to set
	 */
	public void setLoadImageListener(LoadImageListener listener) {
		mListener = listener;
	}

	public void restartLoad(int start, int end) {
		LogHelper.i(TAG_LOG, "restart load, start:" + start + ",end:" + end);
		clearTasks();
	}

	public static LoadImagesManager getInstance(Context context) {
		if (null == context) {
			throw new IllegalArgumentException("param context can not be null.");
		}
		if (null == manager) {
			mContext = context;
			manager = new LoadImagesManager();
		}
		return manager;
	}

	public void addTask(String url) {
		if (!TextUtils.isEmpty(url)) {
			addTaskToMap(url);
		}
	}

	private void addTaskToMap(String url) {

		if (clearing || !isLoad) {
			return;
		}

		if (TextUtils.isEmpty(url)) {
			LogHelper.i(TAG_LOG, "load info is null");
			return;
		}

		String suffix = CacheFileHelper.getSuffixFromURL(url);
		CacheFileHelper.isCacheFileExist(url.hashCode() + suffix);

		// synchronized (tasks) {
		LogHelper.i(TAG_LOG, "add load task:" + url);
		if (!runningTask.containsKey(url.hashCode())) {
			tasks.put(url.hashCode(), url);
		}
		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Implement this method.
				runNextTask();
			}
		});
		// tasks.notifyAll();
		// }
		// LogHelper.i(TAG_LOG, "tasks size:" + tasks.size());
	}

	public void doNothing() {
	}

	public void runNextTask() {

		LogHelper.i(TAG_LOG, "run next task , is busy:" + isBusy);
		LogHelper.i(TAG_LOG, "tasks size:" + tasks.size());
		if (isBusy || clearing || !isLoad) {
			return;
		}

		String url = null;
		synchronized (tasks) {
			Set<Integer> keys = tasks.keySet();
			Iterator<Integer> iterator = keys.iterator();

			if (iterator.hasNext()) {
				int key = iterator.next();
				url = tasks.remove(key);
			}

			tasks.notifyAll();
		}
		if (!TextUtils.isEmpty(url)) {
			taskCount++;
			if (taskCount >= maxTask) {
				isBusy = true;
			}
			ThumbDownloadAsy asy = new ThumbDownloadAsy();
			runningTask.put(url, asy);
			asy.execute(url);
		}
		LogHelper.i(TAG_LOG, "running task size:" + runningTask.size());
	}

	class ThumbDownloadAsy extends AsyncTask<String, Void, Void> {

		public ThumbDownloadAsy() {
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(String... params) {
			// TODO Implement this method.
			LogHelper.i(TAG_LOG, "do in background...");
			String taskInfo = params[0];
			// download thumb bitmap
			if (TextUtils.isEmpty(taskInfo)) {
				LogHelper.i(TAG_LOG,
						"do in background , task info is null or unavailable");
				return null;
			}
			if (mListener != null) {
				mListener.onBegin(taskInfo);
			}
			ImageManager imageManager = ImageManager.sInit(mContext);
			String suffix = CacheFileHelper.getSuffixFromURL(taskInfo);
			File file = new File(CacheFileHelper.ALBUM_PATH, taskInfo.hashCode()
					+ suffix);
			// if (CacheFileHelper.FileType) {
			//
			// }
			boolean successed = imageManager.downloadBitmap(taskInfo, file);
			if (successed) {
				if (mListener != null) {
					mListener.onCompleted(taskInfo, null);
				}
			} else {
				if (mListener != null) {
					mListener.onFailed(taskInfo, null);
				}
			}

			if (!clearing) {
				runningTask.remove(taskInfo);
				taskCount--;
				if (taskCount < maxTask) {
					isBusy = false;
					runNextTask();
				}
			}
			return null;
		}
	}

	/**
	 *
	 */
	public static void clearTasks() {
		// TODO Implement this method.
		clearing = true;
		if (runningTask != null) {
			Set<String> keys = runningTask.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				ThumbDownloadAsy asy = runningTask.get(key);
				if (asy != null) {
					asy.cancel(true);
				}
				iterator.remove();
			}
		}
		if (tasks != null) {
			// Iterator<TaskInfo> iterator = tasks.iterator();
			// while (iterator.hasNext()) {
			// iterator.remove();
			// }
			tasks.clear();
		}
		// System.gc();
		// System.gc();
		clearing = false;
	}

}
