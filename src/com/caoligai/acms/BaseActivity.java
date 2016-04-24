package com.caoligai.acms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * @ClassName: BaseActivity
 * @Description: ���� Activity �Ļ���
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��2��23�� ����11:08:59
 * 
 */
public abstract class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);

		initCreate();
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.alpha_out, R.anim.alpha_in);
	}

	private void initCreate() {
		this.setContentView(getLayoutId());

		initView();

		initData();

		initListener();
	}

	protected abstract int getLayoutId();

	protected abstract void initView();

	protected abstract void initData();

	protected abstract void initListener();

	protected void showToast(String text) {

		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

	}

	protected void openActivity(Activity activity) {
		startActivity(new Intent(this, activity.getClass()));
	}

	private long exitTime = 0;
	/**
	 * �Ƿ񶥲� Activity �ǲŻ�֧�֡��ٰ�һ���˳�����Ĭ�ϣ���
	 */
	private boolean isTopActivity = true;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!isTopActivity && keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				// Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
				// Toast.LENGTH_SHORT).show();
				showToast("�ٰ�һ���˳�����");
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * �����Ƿ�֧�֡��ٰ�һ���˳���
	 * 
	 * @param isTopActivity
	 */
	protected void setIsTopActivity(boolean isTopActivity) {
		this.isTopActivity = isTopActivity;
	}

}
