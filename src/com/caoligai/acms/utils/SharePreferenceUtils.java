/**
 * 
 */
package com.caoligai.acms.utils;

import java.util.List;
import java.util.Map;

import com.avos.avoscloud.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @ClassName: SharePreferenceUtils
 * @Description: SharePreference 工具类
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年2月27日 上午10:57:55
 * 
 */
public class SharePreferenceUtils {
	private String tag = "SharePreferenceUtils";

	private Context context;
	private SharedPreferences spf;
	private Editor editor;
	private int MODE = Activity.MODE_PRIVATE;

	public SharePreferenceUtils(Context context) {

		this.context = context;

	}

	private void initPreference(String name) {

		if (null == spf && null != context) {
			spf = context.getSharedPreferences(name, MODE);
			editor = spf.edit();
			LogUtils.Log_debug(tag, "SharePreferenceUtils 初始化成功");
			return;
			// return editor;
		}
		LogUtils.Log_debug(tag, "SharePreferenceUtils 已经被初始化");
		// return editor;
	}

	private SharedPreferences getPreference(String preferenceName) {
		if (null == spf) {
			initPreference(preferenceName);
		}
		return spf;
	}

	public String[] getString(String preferenceName, String... values) {

		int length = values.length;
		String[] value = new String[length];

		for (int i = 0; i < length; i++) {
			value[i] = getPreference(preferenceName).getString(values[i], "");
		}

		LogUtils.Log_debug(tag, "返回的数据为： " + value);
		return value;
	}

	public void putString(String preFerenceName, Map<String, String> map) {

		initPreference(preFerenceName);

		for (Map.Entry<String, String> entry : map.entrySet()) {
			editor.putString(entry.getKey(), entry.getValue());
			editor.commit();
			LogUtils.Log_debug(tag, "成功将 " + entry.getKey() + "  " + entry.getValue() + " 存储到 SharePreference");
		}

	}

	public boolean getBoolean(String preFerenceName, String param) {
		return getPreference(preFerenceName).getBoolean(param, false);
	}

	public void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

}
