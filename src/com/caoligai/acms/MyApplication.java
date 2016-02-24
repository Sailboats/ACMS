/**
 * 
 */
package com.caoligai.acms;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.caoligai.acms.utils.LogUtils;
import com.caoligai.acms.utils.UserUtils;

import android.app.Application;
import android.util.Log;

/**
 * @ClassName: MyApplication
 * @Description:
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��2��24�� ����9:01:29
 * 
 */
public class MyApplication extends Application {

	MyApplication mContext;

	UserUtils mUserUtils;

	@Override
	public void onCreate() {
		super.onCreate();

		mContext = this;

		initLeanCloud();
	}

	public MyApplication getContext() {
		return mContext;
	}

	/**
	 * ��ʼ�� LeanCloud
	 */
	private void initLeanCloud() {
		// ��ʼ����������Ϊ this, AppId, AppKey
		AVOSCloud.initialize(this, "xFY1tb9f2039kf2VucpsRDva-gzGzoHsz", "f428q4rbkKRUsrhXHtXghQw6");

		/*
		 * // ���� SDK �Ƿ����������Ĵ��� AVObject testObject = new AVObject("TestObject");
		 * testObject.put("words", "Hello World!");
		 * testObject.saveInBackground(new SaveCallback() {
		 * 
		 * @Override public void done(AVException e) { if (e == null) {
		 * Log.d("saved", "success!"); } } });
		 */

		/*
		 * // ���������˺� AVUser user = new AVUser();
		 * user.setUsername("18824195991"); user.setPassword("520402"); //
		 * user.setEmail("hang@leancloud.rocks");
		 * 
		 * // �������Կ���������AVObject����һ��ʹ��put������� // user.put("phone",
		 * "186-1234-0000");
		 * 
		 * user.signUpInBackground(new SignUpCallback() { public void
		 * done(AVException e) { if (e == null) { // successfully Log.d(null,
		 * "���������˺ųɹ�"); } else { // failed } } });
		 */
	}

	public UserUtils getmUserUtils() {
		if (null == mUserUtils) {
			mUserUtils = new UserUtils();
		}
		return mUserUtils;
	}

}
