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
		// 获取查询页面传递过来的查询参数
		final String xuehao = getIntent().getStringExtra(Setting.XUEHAO);
		final String courseName = getIntent().getStringExtra(
				Setting.COURSE_NAME);
		int user_type = getIntent().getIntExtra(Setting.USER_TYPE, 3);
		// 检查用户权限，是否允许用户修改考勤信息
		if (user_type == 2) {
			showOrHideEditLayout(false);
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				String courseId;
				try {
					// 根据课程名查询课程记录
					courseId = AVObject.getQuery(Course.class)
							.whereEqualTo("name", courseName).find().get(0)
							.getObjectId();
					// 根据课程id和学生学号查询考勤记录
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
