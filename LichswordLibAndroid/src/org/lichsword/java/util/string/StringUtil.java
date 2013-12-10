/******************************************************************************
 *
 * Copyright 2012 Lichsword Studio, All right reserved.
 *
 * File name   : StringUtil.java
 * Create time : 2012-10-11
 * Author      : lichsword
 * Description : TODO
 *
 *****************************************************************************/
package org.lichsword.java.util.string;

import java.util.Locale;

import org.lichsword.java.util.TextUtil;

public class StringUtil {

    /**
     * Replace "\t", "\r", " ", "\x0D","\x0A" to none, and return back.
     * 
     * @param string
     * @return
     */
    public static String filterEmptyChars(String string) {
        String result = string;
        String result1 = result.replaceAll(" ", "");
        String result2 = result1.replaceAll("\t", "");
        String result3 = result2.replaceAll("\n", "");
        return result3;
    }

    public static final String ENCODING_UTF8 = "utf-8";

    private static final CharSequence CHARS_BR = "<br />";

    /**
     * Filter html label like "br />" to ""
     * 
     * @param string
     * @return
     */
    public static String filterHtmlLabel(String string) {
        return filterChars(string, CHARS_BR);
    }

    public static String filterChars(String string, CharSequence chars) {
        return string.replace(chars, "");
    }

    public static final String EMPTY = "";

    public static final int DEFAULT_INTEGER = Integer.MAX_VALUE;

    /**
     * <p>
     * When error return default value which is Constant for the maximum
     * {@code int} value, 2<sup>31</sup>-1.
     * </p>
     * <p>
     * e.g. "1234" return 1234; "-86" return -86
     * </p>
     * 
     * @param integerString
     * @return
     */
    public static int parseInt(String integerString) {
        int result = DEFAULT_INTEGER;
        if (!TextUtil.isEmpty(integerString)) {
            try {
                result = Integer.valueOf(integerString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                result = DEFAULT_INTEGER;
            }
        }// end if
        return result;
    }

    public static final String CHARS_QUOT = "&quot;";

    public static String filterHTMLQuotChars(String string) {
        return string.replaceAll(CHARS_QUOT, "\"");
    }

    private static final String EXTEND_NAME_GIF = ".gif";

    /**
     * 
     * @param url
     * @return true if endsWith ".gif", ignore case.
     */
    public static boolean isGif(String url) {
        return endsWith(url, EXTEND_NAME_GIF);
    }

    /**
     * 
     * @param string
     * @param match
     * @return
     */
    public static boolean endsWith(String string, String match) {
        boolean result = true;
        if (!TextUtil.isEmpty(string)) {
            String lower = string.toLowerCase(Locale.US);
            return lower.endsWith(match);
        } else {
            result = false;
        }
        return result;
    }

}
