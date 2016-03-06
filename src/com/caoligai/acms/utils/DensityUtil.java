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
 * @Description: 控件工具类
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月5日 下午8:25:23
 * 
 */
public class DensityUtil {

	private static String tag = "DensityUtil";

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取控件的宽和高
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
				LogUtils.Log_debug(tag, "控件的宽高分别为： " + width + " " + height);
			}
		});

		return value;
	}

	/**
	 * 调整 TextView 的字体大小，使字体大小填充满整个 TextView
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
				LogUtils.Log_debug(tag, "控件的宽高分别为： " + width + " " + height);
				view.setTextSize(width < height ? (width / 2 - 10) : (height / 2 - 10));
			}
		});

	}

}
