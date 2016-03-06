package com.caoligai.acms.fragment;

import java.util.Calendar;
import java.util.Date;

import com.avos.avoscloud.LogUtil;
import com.caoligai.acms.R;
import com.caoligai.acms.utils.DateUtils;
import com.caoligai.acms.utils.DensityUtil;
import com.caoligai.acms.utils.LogUtils;
import com.caoligai.acms.widget.AutoAjustSizeTextView;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: AdminMainFragment
 * @Description: 管理员主页
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月3日 下午9:55:01
 * 
 */
public class AdminMainFragment extends Fragment implements OnClickListener {

	private String tag = "AdminMainFragment";

	/**
	 * 迟到，正常，缺席
	 */
	private TextView tv_late, tv_normal, tv_absent;

	private TextView tv_date;

	public AdminMainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admin_main, container, false);

		initView(rootView);

		return rootView;
	}

	private void initView(View rootView) {
		tv_late = (TextView) rootView.findViewById(R.id.tv_percent_late);
		tv_normal = (TextView) rootView.findViewById(R.id.tv_percent_normal);
		tv_absent = (TextView) rootView.findViewById(R.id.tv_percent_absent);

		tv_date = (TextView) rootView.findViewById(R.id.tv_date);
		tv_date.setText(DateUtils.getDateString(Calendar.getInstance(), 0));

	}

	@Override
	public void onClick(View v) {

	}

}
