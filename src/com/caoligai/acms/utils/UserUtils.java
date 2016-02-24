/**
 * 
 */
package com.caoligai.acms.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

/**
 * @ClassName: UserUtils
 * @Description: �û�������
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��2��24�� ����9:29:44
 * 
 */
public class UserUtils {

	private String tag = "UserUtils";

	AVUser mAVUser;

	public UserUtils() {
	}

	/**
	 * �Ƿ���ڵ�ǰ�û�
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
					// ��¼�ɹ�
					LogUtils.Log_debug(tag, "��¼�ɹ�");
				} else {
					// ��¼ʧ��
					LogUtils.Log_debug(tag, "��¼ʧ��");
				}
			}
		});
	}

}
