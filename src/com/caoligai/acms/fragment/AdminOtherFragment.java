/**
 * 
 */
package com.caoligai.acms.fragment;

import com.caoligai.acms.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName: AdminMainFragment
 * @Description: 管理员其它页面
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月3日 下午9:55:01
 * 
 */
public class AdminOtherFragment extends Fragment {

	public AdminOtherFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admin_other, container, false);
		return rootView;
	}

}
