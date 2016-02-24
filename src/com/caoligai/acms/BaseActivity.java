/**
 * 
 */
package com.caoligai.acms;

import com.caoligai.acms.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * @ClassName: BaseActivity
 * @Description: 所有 Activity 的基类
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年2月23日 下午11:08:59
 * 
 */
public abstract class BaseActivity extends Activity {

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

}
