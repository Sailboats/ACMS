/**
 * 
 */
package com.caoligai.acms.fragment;

import com.caoligai.acms.R;
import com.caoligai.acms.activity.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @ClassName: AdminMainFragment
 * @Description: 管理员其它页面
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月3日 下午9:55:01
 * 
 */
public class AdminOtherFragment extends Fragment {

	private TextView tv_exit_login;

	public AdminOtherFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admin_other, container, false);

		initVIew(rootView);
		return rootView;
	}

	private void initVIew(View rootView) {

		tv_exit_login = (TextView) rootView.findViewById(R.id.tv_exit_login);
		tv_exit_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), LoginActivity.class));
				getActivity().finish();
			}
		});

	}

}
