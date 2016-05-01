package com.caoligai.acms.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;
import com.caoligai.acms.Setting;
import com.caoligai.acms.activity.SingleCourseCheckDetailsActivity;
import com.caoligai.acms.activity.StudentCheckDetailsActivity;
import com.caoligai.acms.avobject.CheckItem;
import com.caoligai.acms.avobject.Course;
import com.caoligai.acms.avobject.MyUser;
import com.caoligai.acms.entity.CheckSummary;
import com.caoligai.acms.utils.DateUtils;
import com.caoligai.acms.utils.LogUtils;

/**
 * @ClassName: AdminMainFragment
 * @Description: ����Ա��ҳ
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��3��3�� ����9:55:01
 * 
 */
public class AdminMainFragment extends Fragment implements OnClickListener {

	private String tag = "AdminMainFragment";

	private static final int ADMIN = 0;
	private static final int TEACHER_COURSE = 1;
	private static final int TEACHER_CHECK = 2;

	private boolean isRefreshing = false;
	private String courseId = null;

	/**
	 * �ٵ���������ȱϯ
	 */
	private TextView tv_late, tv_normal, tv_absent;

	private TextView tv_tips_no_course;

	/**
	 * �ٵ��ʣ�׼ʱ�ʣ�ȱϯ��
	 */
	private TextView percent_normal, percent_late, percent_absent;

	/**
	 * ǰһ�죬��һ��
	 */
	private ImageView iv_pre, iv_next, iv_details;
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
		tv_tips_no_course = (TextView) rootView
				.findViewById(R.id.tv_tips_no_course);

		percent_normal = (TextView) rootView
				.findViewById(R.id.tv_percent_normal);
		percent_late = (TextView) rootView.findViewById(R.id.tv_percent_late);
		percent_absent = (TextView) rootView
				.findViewById(R.id.tv_percent_absent);

		iv_pre = (ImageView) rootView.findViewById(R.id.iv_preday);
		iv_next = (ImageView) rootView.findViewById(R.id.iv_nextday);
		iv_details = (ImageView) rootView.findViewById(R.id.iv_details);
		iv_details.setOnClickListener(this);
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

		if (((MyUser) ((MyApplication) getActivity().getApplication())
				.getmUserUtils().getmAVUser()).getUserType().intValue() == 0) {
			// ����Ա
			date_query = DateUtils.getDateString();

			new Thread(new Runnable() {

				@Override
				public void run() {
					CheckSummary summary = null;
					isRefreshing = true;

					while (isRefreshing) {
						try {
							// ��ѯ��������
							int total_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("date", date_query).count();// ѧ�����˴�

							int late_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("late", true)
									.whereEqualTo("date", date_query).count(); // �ٵ��˴�
							int normal_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("normal", true)
									.whereEqualTo("date", date_query).count();// ׼ʱ�˴�
							int absent_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("absent", true)
									.whereEqualTo("date", date_query).count(); // ȱϯ�˴�
							LogUtils.Log_debug(tag, "ѧ�����˴Σ�" + total_stu
									+ " �ٵ��˴Σ�" + late_stu + " ׼ʱ�˴Σ�"
									+ normal_stu + " ȱϯ�˴Σ�" + absent_stu);

							summary = new CheckSummary(normal_stu, late_stu,
									absent_stu, total_stu);

						} catch (AVException e) {
							e.printStackTrace();
						}

						Message msg = mHandler.obtainMessage();
						msg.what = ADMIN;
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

		if (((MyUser) ((MyApplication) getActivity().getApplication())
				.getmUserUtils().getmAVUser()).getUserType().intValue() == 1) {
			// ��ʦ

			iv_pre.setVisibility(View.GONE);
			iv_next.setVisibility(View.GONE);
			tv_tips_no_course.setVisibility(View.VISIBLE);
			tv_date.setText("");

			new Thread(new Runnable() {

				@Override
				public void run() {
					isRefreshing = true;
					String teacherId = ((MyUser) ((MyApplication) getActivity()
							.getApplication()).getmUserUtils().getmAVUser())
							.getTeacherId();
					while (isRefreshing) {
						// ���ݽ�ʦid��ѯ��ǰʱ�����ڽ��п��ڵ��Լ�����Ŀγ�
						Course course = Course
								.getNowCanCheckCourseByTeacherId(teacherId);
						if (course != null) {
							Message msg = mHandler.obtainMessage();
							msg.what = TEACHER_COURSE;
							msg.obj = course;
							mHandler.sendMessage(msg);

							courseId = course.getObjectId();

							CheckSummary summary = null;

							// ��ѯ������Ϣ
							int total_stu = CheckItem
									.getStudentCountByCourseId(courseId);
							// ѧ�����˴�

							int late_stu = CheckItem
									.getLateCountByCourseId(courseId); // �ٵ��˴�
							int normal_stu = CheckItem
									.getNormalCountByCourseId(courseId);// ׼ʱ�˴�
							int absent_stu = CheckItem
									.getAbsentCountByCourseId(courseId); // ȱϯ�˴�
							LogUtils.Log_debug(tag, "ѧ�����˴Σ�" + total_stu
									+ " �ٵ��˴Σ�" + late_stu + " ׼ʱ�˴Σ�"
									+ normal_stu + " ȱϯ�˴Σ�" + absent_stu);

							summary = new CheckSummary(normal_stu, late_stu,
									absent_stu, total_stu);

							summary = new CheckSummary(normal_stu, late_stu,
									absent_stu, total_stu);

							Message message = mHandler.obtainMessage();
							message.what = TEACHER_CHECK;
							message.obj = summary;
							mHandler.sendMessage(message);
						}

						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}).start();

		}

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

		case R.id.iv_details:
			startActivity(new Intent(getActivity(),
					SingleCourseCheckDetailsActivity.class).putExtra(
					Setting.COURSE_ID, courseId));
			break;

		default:
			break;
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == ADMIN) {
				CheckSummary summary = (CheckSummary) msg.obj;

				tv_normal.setText(summary.getNormal() + "");
				percent_normal.setText(summary.getNormal_rate() + "");

				tv_late.setText(summary.getLate() + "");
				percent_late.setText(summary.getLate_rate() + "");

				tv_absent.setText(summary.getAbsent() + "");
				percent_absent.setText(summary.getAbsent_rate() + "");
			}

			if (msg.what == TEACHER_COURSE) {
				Course course = (Course) msg.obj;
				tv_date.setText(course.getName());
				tv_tips_no_course.setVisibility(View.GONE);
				iv_details.setVisibility(View.VISIBLE);
			}

			if (msg.what == TEACHER_CHECK) {
				CheckSummary summary = (CheckSummary) msg.obj;

				tv_normal.setText(summary.getNormal() + "");
				percent_normal.setText(summary.getNormal_rate() + "");

				tv_late.setText(summary.getLate() + "");
				percent_late.setText(summary.getLate_rate() + "");

				tv_absent.setText(summary.getAbsent() + "");
				percent_absent.setText(summary.getAbsent_rate() + "");

			}

		};
	};
}
