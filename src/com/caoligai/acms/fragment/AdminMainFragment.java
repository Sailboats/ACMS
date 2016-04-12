package com.caoligai.acms.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.LogUtil;
import com.caoligai.acms.R;
import com.caoligai.acms.avobject.CheckItem;
import com.caoligai.acms.avobject.Student;
import com.caoligai.acms.entity.CheckSummary;
import com.caoligai.acms.utils.DateUtils;
import com.caoligai.acms.utils.DensityUtil;
import com.caoligai.acms.utils.LogUtils;
import com.caoligai.acms.widget.AutoAjustSizeTextView;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: AdminMainFragment
 * @Description: 管理员主页
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月3日 下午9:55:01
 * 
 */
public class AdminMainFragment extends Fragment implements OnClickListener {

	private String tag = "AdminMainFragment";

	private boolean isRefreshing = false;

	/**
	 * 迟到，正常，缺席
	 */
	private TextView tv_late, tv_normal, tv_absent;

	/**
	 * 迟到率，准时率，缺席率
	 */
	private TextView percent_normal, percent_late, percent_absent;

	/**
	 * 前一天，后一天
	 */
	private ImageView iv_pre, iv_next;
	private int offset_day = 0;
	private String date_query;

	private TextView tv_date;

	public AdminMainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admin_main,
				container, false);

		initView(rootView);

		return rootView;
	}

	private void initView(View rootView) {
		tv_late = (TextView) rootView.findViewById(R.id.tv_late);
		tv_normal = (TextView) rootView.findViewById(R.id.tv_normal);
		tv_absent = (TextView) rootView.findViewById(R.id.tv_absent);

		percent_normal = (TextView) rootView
				.findViewById(R.id.tv_percent_normal);
		percent_late = (TextView) rootView.findViewById(R.id.tv_percent_late);
		percent_absent = (TextView) rootView
				.findViewById(R.id.tv_percent_absent);

		iv_pre = (ImageView) rootView.findViewById(R.id.iv_preday);
		iv_next = (ImageView) rootView.findViewById(R.id.iv_nextday);
		iv_pre.setOnClickListener(this);
		iv_next.setOnClickListener(this);

		tv_date = (TextView) rootView.findViewById(R.id.tv_date);
		tv_date.setText(DateUtils.getDateString(Calendar.getInstance(), 0));

	}

	@Override
	public void onResume() {
		super.onResume();

		if (isRefreshing) {
			return;
		}

		date_query = DateUtils.getDateString();

		new Thread(new Runnable() {

			@Override
			public void run() {
				CheckSummary summary = null;
				isRefreshing = true;

				while (isRefreshing) {
					try {
						int total_stu = AVObject.getQuery(CheckItem.class)
								.whereEqualTo("date", date_query).count();// 学生总人次

						int late_stu = AVObject.getQuery(CheckItem.class)
								.whereEqualTo("late", true)
								.whereEqualTo("date", date_query).count(); // 迟到人次
						int normal_stu = AVObject.getQuery(CheckItem.class)
								.whereEqualTo("normal", true)
								.whereEqualTo("date", date_query).count();// 准时人次
						int absent_stu = AVObject.getQuery(CheckItem.class)
								.whereEqualTo("absent", true)
								.whereEqualTo("date", date_query).count(); // 缺席人次 
						LogUtils.Log_debug(tag, "学生总人次：" + total_stu + " 迟到人次："
								+ late_stu + " 准时人次：" + normal_stu + " 缺席人次："
								+ absent_stu);

						summary = new CheckSummary(normal_stu, late_stu,
								absent_stu, total_stu);

					} catch (AVException e) {
						e.printStackTrace();
					}

					Message msg = mHandler.obtainMessage();
					msg.obj = summary;
					mHandler.sendMessage(msg);

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_preday:
			offset_day--;
			tv_date.setText(DateUtils.getDateString(Calendar.getInstance(),
					offset_day));
			date_query = DateUtils.getDateString(new SimpleDateFormat(
					"yyyy-MM-dd").format(new Date()), 0, offset_day);
			break;
		case R.id.iv_nextday:
			offset_day++;
			tv_date.setText(DateUtils.getDateString(Calendar.getInstance(),
					offset_day));
			date_query = DateUtils.getDateString(new SimpleDateFormat(
					"yyyy-MM-dd").format(new Date()), 0, offset_day);
			break;

		default:
			break;
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			CheckSummary summary = (CheckSummary) msg.obj;

			tv_normal.setText(summary.getNormal() + "");
			percent_normal.setText(summary.getNormal_rate() + "");

			tv_late.setText(summary.getLate() + "");
			percent_late.setText(summary.getLate_rate() + "");

			tv_absent.setText(summary.getAbsent() + "");
			percent_absent.setText(summary.getAbsent_rate() + "");
		};
	};
}
