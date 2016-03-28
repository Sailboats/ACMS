package com.caoligai.acms.avobject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.caoligai.acms.utils.DateUtils;
import com.caoligai.acms.utils.LogUtils;

@AVClassName("Course")
public class Course extends AVObject {

	private String tag = getClass().getSimpleName();

	public Course() {
	}

	public Course(String name) {
		setName(name);
	}

	// 课程名
	public final String NAME = "name";

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	// 老师名
	private final String TEACHERNAME = "teacher_name";

	public String getTeacherName() {
		return getString(TEACHERNAME);
	}

	public void setTeacherName(String teacherName) {
		put(TEACHERNAME, teacherName);
	}

	// 总周数
	private String total_weeks = "total_weeks";

	public Number getTotalWeeks() {
		return getNumber(total_weeks);
	}

	public void setTotalWeeks(Number weeks) {
		put(total_weeks, weeks);
	}

	// 该课程所在学期的第一周星期一
	private String initDate = "init_date";

	public String getInitDate() {
		return getString(initDate);
	}

	public void setInitDate(String date) {
		put(initDate, date);
	}

	// 该课程的详细上课时间（星期几，第几节）
	private String detailTime = "detial_time";

	public AVRelation<CourseDetialTime> getDetailTime() {
		return getRelation(detailTime);
	}

	public void setDetailTime(AVRelation<CourseDetialTime> times) {
		put(detailTime, times);
	}

	public boolean getDone() {
		return getBoolean("done");
	}

	public void setDone(boolean isDone) {
		put("done", isDone);
	}

	/**
	 * 查询当前可以签到的课程
	 * 
	 * @return
	 */
	public static Course getNowCanCheckCourse(String xuehao) {

		AVQuery<StudentToCourse> query1 = AVObject.getQuery(StudentToCourse.class);
		query1.whereEqualTo("student_xuehao", xuehao);

		try {
			List<StudentToCourse> results1 = query1.find();
			for (StudentToCourse studentToCourse : results1) {
				AVQuery<Course> query = AVObject.getQuery(Course.class);
				Course course = query.get(studentToCourse.getCourseId());
				if (course.canNowCheck()) {

					LogUtils.Log_debug(null, "当前可以进行签到的课程id为： " + course.getObjectId() + " 课程名为： " + course.getName());

					return course;
				}
			}

		} catch (AVException e) {
			e.printStackTrace();
		}

		LogUtils.Log_debug(null, "当前没有可以进行签到的课程");
		return null;
	}

	/**
	 * 该门课程当前是否能签到
	 * 
	 * @return
	 */
	boolean canNowCheck() {

		if (getDone()) {
			return false;
		}

		Date now = new Date();
		Calendar cal_now = Calendar.getInstance();
		cal_now.setTime(now);

		AVRelation<CourseDetialTime> time_relation = getDetailTime();
		List<CourseDetialTime> times;
		try {
			times = time_relation.getQuery().find();

			for (CourseDetialTime time : times) {
				if (time.getDayOfWeek().intValue() == DateUtils.getRealDayOfWeek(cal_now.get(Calendar.DAY_OF_WEEK))
						&& cal_now.after(DateUtils.getBeginCheckTime(time.getIndexOfDay().intValue()))
						&& cal_now.before(DateUtils.getEndCheckTime(time.getIndexOfDay().intValue()))) {

					LogUtils.Log_debug(null, "当前有可以进行签到的课程");
					return true;

				}
			}
		} catch (AVException e) {
			e.printStackTrace();
		}

		return false;
	}
}
