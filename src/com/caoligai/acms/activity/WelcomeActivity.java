package com.caoligai.acms.activity;

import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;
import com.caoligai.acms.utils.LogUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @ClassName: MainActivity
 * @Description: �� Activity
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��2��23�� ����11:06:04
 * 
 */
public class WelcomeActivity extends BaseActivity {

	private String tag = "WelcomeActivity";

	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_welcome;
	}

	@Override
	protected void initView() {
		overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
	}

	@Override
	protected void initData() {
		app = (MyApplication) getApplication();
		mHandler.sendEmptyMessageDelayed(0, 2000);
	}

	@Override
	protected void initListener() {

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (app.getmUserUtils().hasCurrentUser()) {
				// ���ڵ�ǰ�û�
				LogUtils.Log_debug(tag, "���ڵ�ǰ�û�");
				startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
			} else {
				// �����ڵ�ǰ�û�
				LogUtils.Log_debug(tag, "�����ڵ�ǰ�û�");
				startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
			}
			finish();
		};
	};
}
