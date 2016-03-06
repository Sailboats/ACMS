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
 * @Description: ��ʾ�򹤾���
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��2��28�� ����11:45:03
 * 
 */
public class DialogUtils {

	public static void showPleaseWaitDialog(Context context, String text) {
		ProgressDialog dialog = ProgressDialog.show(context, "", "Loading. Please wait...", true);
	}

	public static void showListDialog(Context context, OnClickListener listener, String... str_items) {

		String[] items = str_items;
		// new AlertDialog.Builder(context).setTitle("�б��").setItems(new
		// String[] { "Item1", "Item2" }, null)
		// .setNegativeButton("ȷ��", null).show();

		AlertDialog.Builder builder = new Builder(context);
		builder.setItems(items, listener).show();
	};

}
