package com.caoligai.acms.fragment;

import com.caoligai.acms.R;
import com.caoligai.acms.avobject.Course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StudentIndexFragment extends Fragment {

	private Course course = null;

	private TextView tv_tips, tv_courseName;

	public StudentIndexFragment() {
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_index,
				container, false);

		initView(rootView);
		return rootView;
	}

	private void initView(View view) {
		tv_tips = (TextView) view.findViewById(R.id.tv_tips);
		tv_courseName = (TextView) view.findViewById(R.id.tv_course_name);

		// tv_tips.setVisibility(View.GONE);
		tv_courseName.setVisibility(View.GONE);
	}

	@Override
	public void onResume() {
		super.onResume();

		initData();
	}

	private void initData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {

					course = Course.getNowCanCheckCourse("201210409429");

					if (course != null) {
						Message msg = mHandler.obtainMessage();
						msg.obj = course;
						mHandler.sendMessage(msg);
					}

					try {
						Thread.sleep(8000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		}).start();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tv_tips.setVisibility(View.GONE);
			tv_courseName.setVisibility(View.VISIBLE);

			tv_courseName.setText(course.getName());
		};
	};

}
