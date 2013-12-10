/******************************************************************************
 *
 * Copyright 2012 Lichsword Studio, All right reserved.
 *
 * File name   : NetworkUtil.java
 * Create time : 2012-10-14
 * Author      : lichsword
 * Description : TODO
 *
 *****************************************************************************/
package org.lichsword.android.util.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检测是否连接到WIFI
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectedWIFI(Context context) {
		return (ConnectivityManager.TYPE_WIFI == retrieveConnectedType(context));
	}

	/**
	 * 
	 * @hide
	 */
	private static final int CONNECTED_TYPE_NONE = -1;

	/**
	 * 
	 * @param context
	 * @return -1 if network connected type is NONE.
	 */
	public static int retrieveConnectedType(Context context) {
		int result = CONNECTED_TYPE_NONE;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
			result = activeNetwork.getType();
		} else {
			result = CONNECTED_TYPE_NONE;
		}
		return result;
	}
}
