/**
 * 
 */
package com.caoligai.acms.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

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

	public static void showListDialog(Context context, OnClickListener listener, String... str_items) {

		String[] items = str_items;
		// new AlertDialog.Builder(context).setTitle("列表框").setItems(new
		// String[] { "Item1", "Item2" }, null)
		// .setNegativeButton("确定", null).show();

		AlertDialog.Builder builder = new Builder(context);
		builder.setItems(items, listener).show();
	};

}
