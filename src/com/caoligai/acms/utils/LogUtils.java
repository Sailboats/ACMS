/**
 * 
 */
package com.caoligai.acms.utils;

import android.util.Log;

/**
 * @ClassName: LogUtils
 * @Description:
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年2月24日 下午10:21:10
 * 
 */
public class LogUtils {

	private static boolean is_debug = true;

	public static void Log_debug(String tag, String message) {
		if (is_debug) {
			Log.d(tag, message);
		}
	}

	public static void Log_err(String tag, String message) {
		if (is_debug) {
			Log.d(tag, message);
		}
	}

}
