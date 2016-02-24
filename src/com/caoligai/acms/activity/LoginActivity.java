/**
 * 
 */
package com.caoligai.acms.activity;

import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @ClassName: LoginActivity
 * @Description:登录 Activity
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年2月24日 下午10:17:10
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	MyApplication mApp;

	private EditText mEdtTelphone;
	private EditText mEdtPwd;
	private Button mBtnLogin;
	private TextView mTvForgetPwd;
	private TextView mTvRegist;
	private TextView mTvBrand_Dis;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_login;
	}

	@Override
	protected void initView() {

		mEdtTelphone = (EditText) findViewById(R.id.yitong_edt_telphone);
		mEdtPwd = (EditText) findViewById(R.id.yitong_edt_pwd);
		mBtnLogin = (Button) findViewById(R.id.yitong_btn_login);
		mTvForgetPwd = (TextView) findViewById(R.id.yitong_tv_forgetpwd);
		mTvRegist = (TextView) findViewById(R.id.yitong_tv_register);
		mTvBrand_Dis = (TextView) findViewById(R.id.yitong_brand_distributor);
	}

	@Override
	protected void initData() {
		mApp = (MyApplication) getApplication();
	}

	@Override
	protected void initListener() {
		mBtnLogin.setOnClickListener(this);
		mTvForgetPwd.setOnClickListener(this);
		mTvRegist.setOnClickListener(this);
		mTvBrand_Dis.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {

		case R.id.yitong_btn_login:
			String tel = mEdtTelphone.getText().toString().trim();
			String password = mEdtPwd.getText().toString().trim();

			// TODO 输入合法性检查
			mApp.getmUserUtils().login(tel, password);
//			startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//			finish();
			break;

		case R.id.yitong_tv_forgetpwd:
			// TODO
			// intent.setClass(this, YiTong_FindPwd_MainActivity.class);
			// this.startActivity(intent);

			break;

		case R.id.yitong_tv_register:
			// TODO
			// intent.setClass(this, YiTong_regist_MainActivity.class);
			// this.startActivity(intent);
			break;

		case R.id.yitong_brand_distributor:
			// TODO
			// intent.setClass(this, YiTong_FindPwd_MainActivity.class);
			// this.startActivity(intent);
			break;

		}
	}

}
