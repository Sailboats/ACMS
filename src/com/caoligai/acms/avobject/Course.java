package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVRelation;

@AVClassName("Course")
public class Course extends AVObject {

	public Course() {
	}

	public Course(String name) {
		setName(name);
	}

	// 课程名
	public final String NAME = "name";

	public String getName() {
		return getString(NAME);
	}

	public void setName(String name) {
		put(NAME, name);
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

}
