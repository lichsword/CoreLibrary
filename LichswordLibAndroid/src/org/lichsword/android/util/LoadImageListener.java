package org.lichsword.android.util;

/**
 * LoadImageCompleteListener of Image BE.
 *
 * @author yzhwei
 *
 */
public interface LoadImageListener {

	public void onBegin(String url);

	public void onCompleted(String url, String name);

	public void onFailed(String url, String name);
}
