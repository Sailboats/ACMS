package com.caoligai.acms.fragment;

import java.util.Calendar;

import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;
import com.caoligai.acms.avobject.CheckItem;
import com.caoligai.acms.avobject.Course;
import com.caoligai.acms.avobject.MyUser;
import com.caoligai.acms.utils.LogUtils;
import com.caoligai.acms.widget.MonPickerDialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class StudentIndexFragment extends Fragment {

	private Course course = null;

	private TextView tv_tips, tv_courseName;
	private Button btn_check;
	private MyUser mUser;
	/**
	 * ǩ���ɹ�
	 */
	private static final int CHECK_SUCCESS = 1;

	/**
	 * ��һ�ſ���ǩ���Ŀγ�
	 */
	private static final int GOT_ONE_COURSE = 2;

	private static final int HAS_CHECKED = 3;

	private String tag = getClass().getSimpleName();

	public StudentIndexFragment() {
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_index, container, false);

		initView(rootView);
		return rootView;
	}

	private void initView(View view) {
		tv_tips = (TextView) view.findViewById(R.id.tv_tips);
		tv_courseName = (TextView) view.findViewById(R.id.tv_course_name);
		btn_check = (Button) view.findViewById(R.id.btn_check);

		// tv_tips.setVisibility(View.GONE);
		tv_courseName.setVisibility(View.GONE);
		btn_check.setVisibility(View.GONE);
		btn_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (CheckItem.Check(course, mUser) != null) {
							// TODO ǩ���ɹ�
							Message msg = mHandler.obtainMessage();
							msg.what = CHECK_SUCCESS;
							mHandler.sendMessage(msg);
						}
					}
				}).start();

			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		initData();
	}

	private void initData() {

		mUser = (MyUser) ((MyApplication) getActivity().getApplication()).getmUserUtils().getmAVUser();
		LogUtils.Log_debug(tag, "��ǰ�û���ѧ�ţ� " + mUser.getStudentXueHao());

		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {

					course = Course.getNowCanCheckCourse(mUser.getStudentXueHao());

					if (course != null) {
						// ����Ƿ��Ѿ�����ǩ��
						if (CheckItem.hasChecked(course)) {
							// �Ѿ�ǩ����
							Message msg = mHandler.obtainMessage();
							msg.what = HAS_CHECKED;
							msg.obj = course;
							mHandler.sendMessage(msg);
						} else {
							// δǩ��
							Message msg = mHandler.obtainMessage();
							msg.what = GOT_ONE_COURSE;
							msg.obj = course;
							mHandler.sendMessage(msg);
						}

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

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == GOT_ONE_COURSE) {
				tv_tips.setVisibility(View.GONE);
				tv_courseName.setVisibility(View.VISIBLE);
				btn_check.setVisibility(View.VISIBLE);
				btn_check.setClickable(true);

				tv_courseName.setText(course.getName());
			}

			if (msg.what == CHECK_SUCCESS) {
				Toast.makeText(StudentIndexFragment.this.getActivity(), "ǩ���ɹ�", Toast.LENGTH_SHORT).show();
			}

			if (msg.what == HAS_CHECKED) {
				btn_check.setVisibility(View.VISIBLE);
				btn_check.setText("��ǩ��");
				btn_check.setClickable(false);
			}

		};
	};

}
