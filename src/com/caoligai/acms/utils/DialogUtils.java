/**
 * 
 */
package com.caoligai.acms.utils;

import android.app.ProgressDialog;
import android.content.Context;

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

}
