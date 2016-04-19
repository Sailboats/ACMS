/**
 * 
 */
package com.caoligai.acms.fragment;

import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;
import com.caoligai.acms.Setting;
import com.caoligai.acms.activity.IndexListViewActivity;
import com.caoligai.acms.activity.SearchActivity;
import com.caoligai.acms.utils.DialogUtils;
import com.caoligai.acms.utils.LogUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @ClassName: AdminMainFragment
 * @Description: 管理员考勤管理页面
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月3日 下午9:55:01
 * 
 */
public class AdminCheckFragment extends Fragment implements OnClickListener {

	private String tag = "AdminCheckFragment";

	/**
	 * 主菜单：录入/修改、查访
	 */
	private LinearLayout ll_insertandupdate, ll_search;

	public AdminCheckFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admin_check,
				container, false);

		initView(rootView);
		initListener();
		return rootView;
	}

	private void initView(View rootView) {

		ll_insertandupdate = (LinearLayout) rootView
				.findViewById(R.id.ll_insertandupdate);
		ll_search = (LinearLayout) rootView.findViewById(R.id.ll_search);

	}

	private void initListener() {

		ll_insertandupdate.setOnClickListener(this);
		ll_search.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_insertandupdate:
			// new AlertDialog.Builder(getActivity()).setItems(new String[] {
			// "Item1", "Item2" }, null).show();
			// DialogUtils.showListDialog(getActivity(), new
			// DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// if (0 == which) {
			// startActivity(new Intent(getActivity(),
			// IndexListViewActivity.class));
			// }
			// if (1 == which) {
			// Toast.makeText(getActivity(), "正在开发", Toast.LENGTH_SHORT).show();
			// }
			// }
			// }, "按课程", "按学生");

			LogUtils.Log_debug(tag, "当前用户类型为："
					+ ((MyApplication) getActivity().getApplication())
							.getmUserUtils().getUserType());

			// 管理员
			if (((MyApplication) getActivity().getApplication())
					.getmUserUtils().getUserType() == 0) {
				startActivity(new Intent(getActivity(),
						IndexListViewActivity.class).putExtra(
						Setting.USER_TYPE, ((MyApplication) getActivity()
								.getApplication()).getmUserUtils()
								.getUserType()));
			}
			// 教师
			if (((MyApplication) getActivity().getApplication())
					.getmUserUtils().getUserType() == 1) {
				startActivity(new Intent(getActivity(),
						IndexListViewActivity.class).putExtra(
						Setting.USER_TYPE, ((MyApplication) getActivity()
								.getApplication()).getmUserUtils()
								.getUserType()));
			}
			// 学生
			if (((MyApplication) getActivity().getApplication())
					.getmUserUtils().getUserType() == 2) {
				Toast.makeText(getActivity(), "你没有该权限", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.ll_search:
			startActivity(new Intent(getActivity(), SearchActivity.class));
			break;

		default:
			break;
		}
	}

}
