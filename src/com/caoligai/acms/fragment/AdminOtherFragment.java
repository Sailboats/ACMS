/**
 * 
 */
package com.caoligai.acms.fragment;

import org.apache.http.client.UserTokenHandler;

import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;
import com.caoligai.acms.activity.LoginActivity;
import com.caoligai.acms.avobject.MyUser;
import com.caoligai.acms.utils.ImageUtils;
import com.caoligai.acms.utils.StringHelper;
import com.caoligai.acms.widget.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: AdminMainFragment
 * @Description: ����Ա����ҳ��
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��3��3�� ����9:55:01
 * 
 */
public class AdminOtherFragment extends Fragment {

	private TextView tv_exit_login;
	private CircleImageView iv_avatar;
	private TextView tv_usertype, tv_name;

	private MyUser user;

	public AdminOtherFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admin_other,
				container, false);

		initVIew(rootView);
		return rootView;
	}

	private void initVIew(View rootView) {

		iv_avatar = (CircleImageView) rootView.findViewById(R.id.iv_avatar);
		tv_usertype = (TextView) rootView.findViewById(R.id.tv_usertype);
		tv_name = (TextView) rootView.findViewById(R.id.tv_name);

		tv_exit_login = (TextView) rootView.findViewById(R.id.tv_exit_login);
		tv_exit_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), LoginActivity.class));
				getActivity().finish();
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

		// ͨ�� MyApplication �����ȡ��ǰ�û�
		user = (MyUser) ((MyApplication) getActivity().getApplication())
				.getmUserUtils().getmAVUser();

		if (user != null) {
			// ��ʾ�û�����
			tv_usertype.setText(StringHelper.getUserType(user.getUserType()
					.intValue()));
			// ��ʾ�û���
			tv_name.setText(user.getName());

			// �����û�ͷ��
			ImageUtils.displayImage(user.getAvatarUrl(), (ImageView) iv_avatar);
		}
	}

}
