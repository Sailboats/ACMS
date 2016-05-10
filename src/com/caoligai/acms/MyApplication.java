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
 * @date 2016��2��24�� ����9:01:29
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

		LogUtils.Log_debug(tag, "MyApplication �ɹ���ʼ��,������Ϊ��"
				+ getCurProcessName(this));
	}

	/**
	 * ��ʼ��ͼƬ���ع��� Fresco
	 */
	private void initFresco() {
		// Fresco.initialize(this);
		// ����Ĭ�ϵ�ImageLoader���ò���
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
		LogUtils.Log_debug(tag, "ImageLoader ��ʼ���ɹ�");
	}

	/**
	 * ������������
	 */
	private void createTestData() {

		// createCheckItemPreviewdata("Ӧ������ѧ����");
		// createCheckItemPreviewdata("��Ϣ��ȫ");
		// createCheckItemPreviewdata("java���Գ������");
		// createCheckItemPreviewdata("C���Ա������");
		//
		// createAllCheckItemDataByCourseName("Ӧ������ѧ����");
		// createAllCheckItemDataByCourseName("��Ϣ��ȫ");
		// createAllCheckItemDataByCourseName("java���Գ������");
		// createAllCheckItemDataByCourseName("C���Ա������");

		// countCheckItem();

		// createAllStudentToCourseData();
	}

	/**
	 * ��ӡ�������˹��м���ǩ����¼
	 */
	private void countCheckItem() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int count = AVObject.getQuery(CheckItem.class).count();

					LogUtils.Log_debug(tag, "�������˹��� " + count + " ��ǩ����¼");
				} catch (AVException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * ���� 29 ��ͬѧѡ�εļ�¼
	 */
	private void createAllStudentToCourseData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 1; i < 30; i++) {

					StudentToCourse stc1 = new StudentToCourse();
					stc1.setCourseName("Ӧ������ѧ����");
					stc1.setCourseId("56e0148fefa631005cd5dd85");
					stc1.setXuehao("2012104094" + (i >= 10 ? i : "0" + i));

					StudentToCourse stc2 = new StudentToCourse();
					stc2.setCourseName("��Ϣ��ȫ");
					stc2.setCourseId("56e01487128fe10059f455aa");
					stc2.setXuehao("2012104094" + (i >= 10 ? i : "0" + i));

					StudentToCourse stc3 = new StudentToCourse();
					stc3.setCourseName("C���Ա������");
					stc3.setCourseId("56dee7e7816dfa0052ed297e");
					stc3.setXuehao("2012104094" + (i >= 10 ? i : "0" + i));

					StudentToCourse stc4 = new StudentToCourse();
					stc4.setCourseName("java���Գ������");
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

					LogUtils.Log_debug(tag, "�ɹ������� " + i + " ��ͬѧ��ѡ�μ�¼");

				}

			}
		}).start();
	}

	/**
	 * �����γ���Ϊ courseName �Ŀ��ڼ�¼Ԥ����
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

					// ��ȡ���ڼ��ڼ��ڿ�
					AVRelation<CourseDetialTime> relation = course
							.getDetailTime();
					List<CourseDetialTime> times = (List<CourseDetialTime>) relation
							.getQuery().find();

					// ��ȡ������
					int totalWeeks = (Integer) course.getTotalWeeks();
					course.setInitDate("2016-02-29");
					course.save();
					LogUtils.Log_debug(tag, "��ʼ���Ͽ����ڣ�" + course.getInitDate());

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
							LogUtils.Log_debug(tag, "�ɹ������� " + create_record
									+ " �����ڼ�¼Ԥ����");
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
	 * ����һ�ſγ̵�����ѧ�����ڼ�¼
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

					// ��ȡ���ڼ��ڼ��ڿ�
					AVRelation<CourseDetialTime> relation = course
							.getDetailTime();
					List<CourseDetialTime> times = (List<CourseDetialTime>) relation
							.getQuery().find();

					// ��ȡ������
					int totalWeeks = (Integer) course.getTotalWeeks();
					course.setInitDate("2016-02-29");
					course.save();
					LogUtils.Log_debug(tag, "��ʼ���Ͽ����ڣ�" + course.getInitDate());

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
							// LogUtils.Log_debug(tag, "�ɹ������� " + create_record
							// + " �����ڼ�¼Ԥ����");
							// create_record++;

							// ������Ӧ��ѧ�����ڼ�¼ CheckItem

							// ��ѯѡ���˸��ſγ̵�����ѧ��
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

								// �γ� id �� ���� �� �ڼ��� Ψһȷ��һ�� CheckItemPreview ��¼
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
								LogUtils.Log_debug(tag, "�ɹ������� "
										+ create_record++ + " �����ڼ�¼");
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
	 * ��ʼ�� LeanCloud
	 */
	private void initLeanCloud() {

		// ��ʼ�� AVObject ����
		AVUser.alwaysUseSubUserClass(MyUser.class);
		AVObject.registerSubclass(CheckItem.class);
		AVObject.registerSubclass(Course.class);
		AVObject.registerSubclass(Student.class);
		AVObject.registerSubclass(CourseDetialTime.class);
		AVObject.registerSubclass(CheckItemPreview.class);
		AVObject.registerSubclass(StudentToCourse.class);

		// ��ʼ����������Ϊ this, AppId, AppKey
		AVOSCloud.initialize(this, "xFY1tb9f2039kf2VucpsRDva-gzGzoHsz",
				"f428q4rbkKRUsrhXHtXghQw6");

		/*
		 * // ���� SDK �Ƿ����������Ĵ��� AVObject testObject = new AVObject("TestObject");
		 * testObject.put("words", "Hello World!");
		 * testObject.saveInBackground(new SaveCallback() {
		 * 
		 * @Override public void done(AVException e) { if (e == null) {
		 * Log.d("saved", "success!"); } } });
		 */

		/*
		 * // ���������˺� AVUser user = new AVUser();
		 * user.setUsername("18824195991"); user.setPassword("520402"); //
		 * user.setEmail("hang@leancloud.rocks");
		 * 
		 * // �������Կ���������AVObject����һ��ʹ��put������� // user.put("phone",
		 * "186-1234-0000");
		 * 
		 * user.signUpInBackground(new SignUpCallback() { public void
		 * done(AVException e) { if (e == null) { // successfully Log.d(null,
		 * "���������˺ųɹ�"); } else { // failed } } });
		 */
		LogUtils.Log_debug(tag, "LeanCloud ��ʼ���ɹ�");
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
