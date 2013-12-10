package org.lichsword.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkHelper of Hotels Finder.
 * 
 * @author yzhwei
 * 
 */
public class NetworkHelper {

	public static final String TAG = "NetworkHelper";

	public static final int BAD_NETWORK = -1;

	public static boolean isNetworkAvailable(Context context) {
		boolean result = false;
		final ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			result = false;
		} else {
			final NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	public static void startWirelessSettings(final Context context) {
		if (context != null) {
			Intent intent = new Intent();
			intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				((Activity) context).startActivity(intent);
				// Intent intent2 = new Intent(context,NetworkHelper.class);
				// ((Activity)context).startActivityForResult(intent,
				// WIRELESS_SETTING);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
