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
 * @Description: �û�������
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��2��24�� ����9:29:44
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

	public void login(final Context context, String tel, String password) {
		final String name = tel, pass = password;
		mAVUser = new AVUser();

		final ProgressDialog dialog = ProgressDialog.show(context, "", "���ڵ�½...", true);

		mAVUser.logInInBackground(tel, password, new LogInCallback() {
			public void done(AVUser user, AVException e) {
				dialog.dismiss();
				if (e == null) {
					// ��¼�ɹ�
					LogUtils.Log_debug(tag, "��¼�ɹ�");
					saveNameAndPassword(name, pass);
					context.startActivity(new Intent(context, HomeActivity.class));
					((LoginActivity) context).finish();
				} else {
					// ��¼ʧ��
					LogUtils.Log_debug(tag, "��¼ʧ��");
					Toast.makeText(context, "��¼ʧ��: " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * �����Ƿ����Ѿ���ס���˺ź�����
	 * 
	 * @return
	 */
	public boolean hasRememberUser() {

		return getPrefUtils().getBoolean(PRE_NAME, HAS_REMEMBER_USER);
	}

	/**
	 * ��ȡ���ر�����˺ź�����
	 * 
	 * @return
	 */
	public String[] getLocalNameAndPassword() {

		String[] value = getPrefUtils().getString(PRE_NAME, PARS);

		return value;
	}

	/**
	 * �����˺ź����뵽����
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
