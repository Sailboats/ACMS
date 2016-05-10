/**
 * 
 */
package com.caoligai.acms;

import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.caoligai.acms.avobject.CheckItem;
import com.caoligai.acms.avobject.CheckItemPreview;
import com.caoligai.acms.avobject.Course;
import com.caoligai.acms.avobject.CourseDetialTime;
import com.caoligai.acms.avobject.MyUser;
import com.caoligai.acms.avobject.Student;
import com.caoligai.acms.avobject.StudentToCourse;
import com.caoligai.acms.utils.DateUtils;
import com.caoligai.acms.utils.LogUtils;
import com.caoligai.acms.utils.UserUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @ClassName: MyApplication
 * @Description:
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年2月24日 下午9:01:29
 * 
 */
public class MyApplication extends Application {

	private String tag = "MyApplication";

	MyApplication mContext;

	UserUtils mUserUtils;

	@Override
	public void onCreate() {
		super.onCreate();

		mContext = this;
		if (getCurProcessName(this).equals("com.caoligai.acms")) {
			initLeanCloud();

			createTestData();

			initFresco();
		}

		LogUtils.Log_debug(tag, "MyApplication 成功初始化,进程名为："
				+ getCurProcessName(this));
	}

	/**
	 * 初始化图片加载构架 Fresco
	 */
	private void initFresco() {
		// Fresco.initialize(this);
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
		LogUtils.Log_debug(tag, "ImageLoader 初始化成功");
	}

	/**
	 * 创建测试数据
	 */
	private void createTestData() {

		// createCheckItemPreviewdata("应用密码学基础");
		// createCheckItemPreviewdata("信息安全");
		// createCheckItemPreviewdata("java语言程序设计");
		// createCheckItemPreviewdata("C语言编程入门");
		//
		// createAllCheckItemDataByCourseName("应用密码学基础");
		// createAllCheckItemDataByCourseName("信息安全");
		// createAllCheckItemDataByCourseName("java语言程序设计");
		// createAllCheckItemDataByCourseName("C语言编程入门");

		// countCheckItem();

		// createAllStudentToCourseData();
	}

	/**
	 * 打印服务器端共有几条签到记录
	 */
	private void countCheckItem() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int count = AVObject.getQuery(CheckItem.class).count();

					LogUtils.Log_debug(tag, "服务器端共有 " + count + " 条签到记录");
				} catch (AVException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 创建 29 个同学选课的记录
	 */
	private void createAllStudentToCourseData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 1; i < 30; i++) {

					StudentToCourse stc1 = new StudentToCourse();
					stc1.setCourseName("应用密码学基础");
					stc1.setCourseId("56e0148fefa631005cd5dd85");
					stc1.setXuehao("2012104094" + (i >= 10 ? i : "0" + i));

					StudentToCourse stc2 = new StudentToCourse();
					stc2.setCourseName("信息安全");
					stc2.setCourseId("56e01487128fe10059f455aa");
					stc2.setXuehao("2012104094" + (i >= 10 ? i : "0" + i));

					StudentToCourse stc3 = new StudentToCourse();
					stc3.setCourseName("C语言编程入门");
					stc3.setCourseId("56dee7e7816dfa0052ed297e");
					stc3.setXuehao("2012104094" + (i >= 10 ? i : "0" + i));

					StudentToCourse stc4 = new StudentToCourse();
					stc4.setCourseName("java语言程序设计");
					stc4.setCourseId("56deed8b7db2a20059492547");
					stc4.setXuehao("2012104094" + (i >= 10 ? i : "0" + i));

					try {
						stc1.save();
						stc2.save();
						stc3.save();
						stc4.save();
					} catch (AVException e) {
						e.printStackTrace();
					}

					LogUtils.Log_debug(tag, "成功创建第 " + i + " 个同学的选课记录");

				}

			}
		}).start();
	}

	/**
	 * 创建课程名为 courseName 的考勤记录预览表
	 * 
	 * @param string
	 */
	private void createCheckItemPreviewdata(final String courseName) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				AVQuery<Course> query = AVObject.getQuery(Course.class);
				query.whereEqualTo("name", courseName);

				try {
					int create_record = 1;
					Course course = query.find().get(0);

					// 获取星期几第几节课
					AVRelation<CourseDetialTime> relation = course
							.getDetailTime();
					List<CourseDetialTime> times = (List<CourseDetialTime>) relation
							.getQuery().find();

					// 获取总周数
					int totalWeeks = (Integer) course.getTotalWeeks();
					course.setInitDate("2016-02-29");
					course.save();
					LogUtils.Log_debug(tag, "初始化上课日期：" + course.getInitDate());

					for (int i = 1; i <= totalWeeks; i++) {
						for (CourseDetialTime courseDetialTime : times) {

							CheckItemPreview item = new CheckItemPreview();
							item.setCourseId(course.getObjectId());
							item.setWeek(i);
							item.setDayOfWeek(courseDetialTime.getDayOfWeek());
							item.setCourseIndexOfDay(courseDetialTime
									.getIndexOfDay());
							item.setDate(DateUtils.getDateString(
									course.getInitDate(),
									i - 1,
									(Integer) courseDetialTime.getDayOfWeek() - 2));

							item.save();
							LogUtils.Log_debug(tag, "成功创建第 " + create_record
									+ " 条考勤记录预览项");
							create_record++;

						}

					}

				} catch (AVException e) {
					e.printStackTrace();
				}

				/*
				 * AVQuery<Course> query = AVObject.getQuery(Course.class); try
				 * { Course course = query.get("56dee7e7816dfa0052ed297e");
				 * 
				 * StudentToCourse stc = new StudentToCourse();
				 * stc.setCourse(course); stc.setXuehao("201210409429");
				 * stc.save(); } catch (AVException e) { e.printStackTrace(); }
				 */}
		}).start();

	}

	/**
	 * 创建一门课程的所有学生考勤记录
	 * 
	 * @param courseName
	 */
	public void createAllCheckItemDataByCourseName(final String courseName) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				AVQuery<Course> query = AVObject.getQuery(Course.class);
				query.whereEqualTo("name", courseName);

				try {
					int create_record = 1;
					Course course = query.find().get(0);

					// 获取星期几第几节课
					AVRelation<CourseDetialTime> relation = course
							.getDetailTime();
					List<CourseDetialTime> times = (List<CourseDetialTime>) relation
							.getQuery().find();

					// 获取总周数
					int totalWeeks = (Integer) course.getTotalWeeks();
					course.setInitDate("2016-02-29");
					course.save();
					LogUtils.Log_debug(tag, "初始化上课日期：" + course.getInitDate());

					for (int i = 1; i <= totalWeeks; i++) {
						for (CourseDetialTime courseDetialTime : times) {

							// CheckItemPreview item = new CheckItemPreview();
							// item.setCourseId(course.getObjectId());
							// item.setWeek(i);
							// item.setDayOfWeek(courseDetialTime.getDayOfWeek());
							// item.setCourseIndexOfDay(courseDetialTime.getIndexOfDay());
							// item.setDate(DateUtils.getDateString(course.getInitDate(),
							// i - 1,
							// (Integer) courseDetialTime.getDayOfWeek() - 2));
							//
							// item.save();
							// LogUtils.Log_debug(tag, "成功创建第 " + create_record
							// + " 条考勤记录预览项");
							// create_record++;

							// 创建对应的学生考勤记录 CheckItem

							// 查询选修了该门课程的所有学生
							List<StudentToCourse> stuToCor = AVObject
									.getQuery(StudentToCourse.class)
									.whereEqualTo("courseId",
											course.getObjectId()).find();
							for (StudentToCourse studentToCourse : stuToCor) {
								// Student stu =
								// AVObject.getQuery(Student.class)
								// .whereEqualTo("xuehao",
								// studentToCourse.getXuehao()).find().get(0);
								AVQuery<Student> query2 = AVObject
										.getQuery(Student.class);
								query2.whereEqualTo("xuehao",
										studentToCourse.getXuehao());
								Student stu = query2.find().get(0);
								CheckItem check = new CheckItem();
								check.setStuXueHao(studentToCourse.getXuehao());
								check.setStuName(stu.getName());
								check.setCourseId(course.getObjectId());
								check.setCourseName(course.getName());
								check.setWeek(i);
								check.setDayOfWeek(courseDetialTime
										.getDayOfWeek());
								check.setIndexOfDay(courseDetialTime
										.getIndexOfDay());
								check.setDate(DateUtils.getDateString(course
										.getInitDate(), i - 1, courseDetialTime
										.getDayOfWeek().intValue() - 2));

								// 课程 id 和 日期 和 第几节 唯一确定一条 CheckItemPreview 记录
								CheckItemPreview item = AVObject
										.getQuery(CheckItemPreview.class)
										.whereEqualTo("courseId",
												course.getObjectId())
										.whereEqualTo(
												"date",
												DateUtils.getDateString(course
														.getInitDate(), i - 1,
														courseDetialTime
																.getDayOfWeek()
																.intValue() - 2))
										.whereEqualTo(
												"course_index_of_day",
												courseDetialTime
														.getIndexOfDay())
										.find().get(0);
								check.setCheckItemPreviewId(item.getObjectId());
								check.save();
								LogUtils.Log_debug(tag, "成功创建第 "
										+ create_record++ + " 条考勤记录");
							}
						}

					}

				} catch (AVException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	public MyApplication getContext() {
		return mContext;
	}

	/**
	 * 初始化 LeanCloud
	 */
	private void initLeanCloud() {

		// 初始化 AVObject 子类
		AVUser.alwaysUseSubUserClass(MyUser.class);
		AVObject.registerSubclass(CheckItem.class);
		AVObject.registerSubclass(Course.class);
		AVObject.registerSubclass(Student.class);
		AVObject.registerSubclass(CourseDetialTime.class);
		AVObject.registerSubclass(CheckItemPreview.class);
		AVObject.registerSubclass(StudentToCourse.class);

		// 初始化参数依次为 this, AppId, AppKey
		AVOSCloud.initialize(this, "xFY1tb9f2039kf2VucpsRDva-gzGzoHsz",
				"f428q4rbkKRUsrhXHtXghQw6");

		/*
		 * // 测试 SDK 是否正常工作的代码 AVObject testObject = new AVObject("TestObject");
		 * testObject.put("words", "Hello World!");
		 * testObject.saveInBackground(new SaveCallback() {
		 * 
		 * @Override public void done(AVException e) { if (e == null) {
		 * Log.d("saved", "success!"); } } });
		 */

		/*
		 * // 创建测试账号 AVUser user = new AVUser();
		 * user.setUsername("18824195991"); user.setPassword("520402"); //
		 * user.setEmail("hang@leancloud.rocks");
		 * 
		 * // 其他属性可以像其他AVObject对象一样使用put方法添加 // user.put("phone",
		 * "186-1234-0000");
		 * 
		 * user.signUpInBackground(new SignUpCallback() { public void
		 * done(AVException e) { if (e == null) { // successfully Log.d(null,
		 * "创建测试账号成功"); } else { // failed } } });
		 */
		LogUtils.Log_debug(tag, "LeanCloud 初始化成功");
	}

	public UserUtils getmUserUtils() {
		if (null == mUserUtils) {
			mUserUtils = new UserUtils(getApplicationContext());
		}
		return mUserUtils;
	}

	private String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return "";
	}

}
