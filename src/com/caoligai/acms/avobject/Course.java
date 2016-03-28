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

	// �γ���
	public final String NAME = "name";

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	// ��ʦ��
	private final String TEACHERNAME = "teacher_name";

	public String getTeacherName() {
		return getString(TEACHERNAME);
	}

	public void setTeacherName(String teacherName) {
		put(TEACHERNAME, teacherName);
	}

	// ������
	private String total_weeks = "total_weeks";

	public Number getTotalWeeks() {
		return getNumber(total_weeks);
	}

	public void setTotalWeeks(Number weeks) {
		put(total_weeks, weeks);
	}

	// �ÿγ�����ѧ�ڵĵ�һ������һ
	private String initDate = "init_date";

	public String getInitDate() {
		return getString(initDate);
	}

	public void setInitDate(String date) {
		put(initDate, date);
	}

	// �ÿγ̵���ϸ�Ͽ�ʱ�䣨���ڼ����ڼ��ڣ�
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
	 * ��ѯ��ǰ����ǩ���Ŀγ�
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

					LogUtils.Log_debug(null, "��ǰ���Խ���ǩ���Ŀγ�idΪ�� " + course.getObjectId() + " �γ���Ϊ�� " + course.getName());

					return course;
				}
			}

		} catch (AVException e) {
			e.printStackTrace();
		}

		LogUtils.Log_debug(null, "��ǰû�п��Խ���ǩ���Ŀγ�");
		return null;
	}

	/**
	 * ���ſγ̵�ǰ�Ƿ���ǩ��
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

					LogUtils.Log_debug(null, "��ǰ�п��Խ���ǩ���Ŀγ�");
					return true;

				}
			}
		} catch (AVException e) {
			e.printStackTrace();
		}

		return false;
	}
}
