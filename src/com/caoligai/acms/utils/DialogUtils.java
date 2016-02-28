/**
 * 
 */
package com.caoligai.acms.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @ClassName: DialogUtils
 * @Description: 提示框工具类
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年2月28日 下午11:45:03
 * 
 */
public class DialogUtils {

	public static void showPleaseWaitDialog(Context context, String text) {
		ProgressDialog dialog = ProgressDialog.show(context, "", "Loading. Please wait...", true);
	}

}
