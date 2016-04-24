/**
 * 
 */
package com.caoligai.acms;

import java.util.Calendar;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
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

import android.app.Application;
import android.util.Log;

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

		initLeanCloud();

		createTestData();

		initFresco();
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

		// createAllCheckItemDataByCourseName("Ӧ������ѧ����");
		// createAllCheckItemDataByCourseName("��Ϣ��ȫ");
		// createAllCheckItemDataByCourseName("java���Գ������");
		// createAllCheckItemDataByCourseName("C���Ա������");

		// testQuery();

	}

	private void testQuery() {
		AVQuery<CheckItemPreview> query = AVObject
				.getQuery(CheckItemPreview.class);

		query.whereEqualTo("date", "2016-03-28");
		query.whereEqualTo("course_index_of_day",
		/* checkResult.getCourse_index_of_day() */4);
		try {
			List<CheckItemPreview> result = query.find();
			LogUtils.Log_debug(tag, "����id = " + result.get(0).getObjectId());
			return;
		} catch (AVException e) {
			e.printStackTrace();
		}

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

}
