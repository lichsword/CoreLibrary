package org.lichsword.android.util.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.lichsword.java.util.string.StringUtil;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class URLUtil {

    public static final String AMPERSAND = "&";
    public static final String AMPERSEQUAL = "=";

    /**
     * 判断Url字符串是否合法
     * 
     * @return true 如果url是合法的，否则返回false.
     */
    public static boolean isUrlLegal(String url) {
        boolean result = true;

        if (!TextUtils.isEmpty(url)) {
            result = url.startsWith("http");
        } else {
            result = false;
        }

        return result;
    }

    /**
     * <p>
     * 从 url 串中取出文件名
     * </p>
     * <p>
     * e.g: "http://google.com/logo.png"返回"logo"
     * </p>
     * 
     * @param url
     * @return name
     */
    public static String getNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String imageName = null;

        int index = url.lastIndexOf("/");
        if (index != -1) {
            imageName = url.substring(index + 1);
        }
        return imageName;
    }

    /**
     * 使用浏览器打开指定网页
     * 
     * @param context
     * @param webPageUrl
     * @return
     */
    public static boolean openWebPage(Context context, String webPageUrl) {
        boolean openSuccess = true;
        if (null != context && !TextUtils.isEmpty(webPageUrl)) {
            Uri uri = Uri.parse(webPageUrl);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
                openSuccess = true;
            } catch (ActivityNotFoundException e) {
                openSuccess = false;
            }
        } else {
            openSuccess = false;
        }
        return openSuccess;
    }

    /**
     * 从url的尾部取出文件名称 eg:"http://com.google.com/image/logo.png" 将得到"logo.png"
     * 
     * @param url
     * @return filename from url string
     */
    public static String getFileNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String filename = null;

        int index = url.lastIndexOf("/");
        if (index != -1) {
            filename = url.substring(index + 1);
        }// end if
        return filename;
    }

    /**
     * 将本地字符串转换为网络字符串，默认目标编码为UTF-8
     * 
     * @param originalString
     * @param format
     * @return null if UnsupportedEncodingException otherwise return formated
     *         network string.
     */
    public static String getTranslateString(String originalString) {
        return getTranslateString(originalString, StringUtil.ENCODING_UTF8);
    }

    /**
     * 将本地字符串转换为网络字符串
     * 
     * @param originalString
     * @param format
     * @return null if UnsupportedEncodingException otherwise return formated
     *         network string.
     */
    private static String getTranslateString(String originalString,
            String format) {
        try {
            return URLEncoder.encode(originalString, format);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
