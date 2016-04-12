package com.caoligai.acms.activity;

import java.util.List;

import android.os.Message;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.caoligai.acms.Setting;
import com.caoligai.acms.avobject.CheckItem;
import com.caoligai.acms.avobject.Course;

public class StudentAbsentCheckActivity extends StudentCheckDetailsActivity {
	@Override
	protected void initData() {
		final String xuehao = getIntent().getStringExtra(Setting.XUEHAO);
		final String courseName = getIntent().getStringExtra(
				Setting.COURSE_NAME);

		new Thread(new Runnable() {

			@Override
			public void run() {
				String courseId;
				try {
					courseId = AVObject.getQuery(Course.class)
							.whereEqualTo("name", courseName).find().get(0)
							.getObjectId();

					List<CheckItem> result = AVObject.getQuery(CheckItem.class)
							.whereEqualTo("stu_xuehao", xuehao)
							.whereEqualTo("absent", true)
							.whereEqualTo("courseId", courseId).find();

					Message msg = mHandler.obtainMessage();
					msg.what = INIT_DATA;
					msg.obj = result;
					mHandler.sendMessage(msg);
				} catch (AVException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}
}
