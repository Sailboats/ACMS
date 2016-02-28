/**
 * 
 */
package com.caoligai.acms.utils;

import java.util.HashMap;
import java.util.Map;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.activity.HomeActivity;
import com.caoligai.acms.activity.LoginActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @ClassName: UserUtils
 * @Description: 用户工具类
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年2月24日 下午9:29:44
 * 
 */
public class UserUtils {

	private String tag = "UserUtils";

	AVUser mAVUser;

	private Context context;

	private SharePreferenceUtils spfUtil;

	private static final String PRE_NAME = "userPre";
	private static final String HAS_REMEMBER_USER = "has_remember_user";
	private static final String[] PARS = { "name", "password" };

	public UserUtils(Context context) {
		this.context = context;
	}

	/**
	 * 是否存在当前用户
	 * 
	 * @return
	 */
	public boolean hasCurrentUser() {

		mAVUser = AVUser.getCurrentUser();

		if (null == mAVUser) {
			return false;
		} else {
			return true;
		}

	}

	public void login(final Context context, String tel, String password) {
		final String name = tel, pass = password;
		mAVUser = new AVUser();

		final ProgressDialog dialog = ProgressDialog.show(context, "", "正在登陆...", true);

		mAVUser.logInInBackground(tel, password, new LogInCallback() {
			public void done(AVUser user, AVException e) {
				dialog.dismiss();
				if (e == null) {
					// 登录成功
					LogUtils.Log_debug(tag, "登录成功");
					saveNameAndPassword(name, pass);
					context.startActivity(new Intent(context, HomeActivity.class));
					((LoginActivity) context).finish();
				} else {
					// 登录失败
					LogUtils.Log_debug(tag, "登录失败");
					Toast.makeText(context, "登录失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * 本地是否有已经记住的账号和密码
	 * 
	 * @return
	 */
	public boolean hasRememberUser() {

		return getPrefUtils().getBoolean(PRE_NAME, HAS_REMEMBER_USER);
	}

	/**
	 * 获取本地保存的账号和密码
	 * 
	 * @return
	 */
	public String[] getLocalNameAndPassword() {

		String[] value = getPrefUtils().getString(PRE_NAME, PARS);

		return value;
	}

	/**
	 * 保存账号和密码到本地
	 * 
	 * @param name
	 * @param password
	 */
	public void saveNameAndPassword(String name, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(PARS[0], name);
		map.put(PARS[1], password);

		getPrefUtils().putString(PRE_NAME, map);
		getPrefUtils().setBoolean(HAS_REMEMBER_USER, true);
	}

	private SharePreferenceUtils getPrefUtils() {
		if (null == spfUtil) {
			spfUtil = new SharePreferenceUtils(context);
		}
		return spfUtil;
	}

}
