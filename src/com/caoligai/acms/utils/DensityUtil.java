package com.caoligai.acms.utils;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

/**
 * @ClassName: DensityUtil
 * @Description: �ؼ�������
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��3��5�� ����8:25:23
 * 
 */
public class DensityUtil {

	private static String tag = "DensityUtil";

	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * ��ȡ�ؼ��Ŀ�͸�
	 * 
	 * @param widget
	 * @return
	 */
	public static List<Integer> getwidthAndheight(View widget) {

		final View view = widget;
		final List<Integer> value = new ArrayList<Integer>();
		final ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				int height = view.getHeight();
				int width = view.getWidth();
				value.add((int) width);
				value.add((int) height);
				LogUtils.Log_debug(tag, "�ؼ��Ŀ�߷ֱ�Ϊ�� " + width + " " + height);
			}
		});

		return value;
	}

	/**
	 * ���� TextView �������С��ʹ�����С��������� TextView
	 * 
	 * @param tv
	 */
	public static void adjustTextSize(TextView tv) {

		final TextView view = tv;
		final ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				int height = view.getHeight();
				int width = view.getWidth();
				LogUtils.Log_debug(tag, "�ؼ��Ŀ�߷ֱ�Ϊ�� " + width + " " + height);
				view.setTextSize(width < height ? (width / 2 - 10) : (height / 2 - 10));
			}
		});

	}

}
