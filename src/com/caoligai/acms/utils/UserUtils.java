/**
 * 
 */
package com.caoligai.acms.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

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

	public UserUtils() {
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

	public void login(String tel, String password) {
		mAVUser = new AVUser();

		mAVUser.logInInBackground(tel, password, new LogInCallback() {
			public void done(AVUser user, AVException e) {
				if (e == null) {
					// 登录成功
					LogUtils.Log_debug(tag, "登录成功");
				} else {
					// 登录失败
					LogUtils.Log_debug(tag, "登录失败");
				}
			}
		});
	}

}
