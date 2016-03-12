package com.caoligai.acms.avobject;

import java.security.PublicKey;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * 考勤记录预览表
 * 
 * @author Noodle
 * 
 */
@AVClassName("CheckItemPreview")
public class CheckItemPreview extends AVObject {

	public CheckItemPreview() {
	}

	// 课程 id
	private String courseId = "courseId";

	public String getCourseId() {
		return getString(courseId);
	}

	public void setCourseId(String Id) {
		put(courseId, Id);
	}

	// 第几周
	private String week = "week";

	public Number getWeek() {
		return getNumber(week);
	}

	public void setWeek(Number wee) {
		put(week, wee);
	}

	// 星期几
	private String dayOfWeek = "day_of_week";

	public Number getDayOfWeek() {
		return getNumber(dayOfWeek);
	}

	public void setDayOfWeek(Number day) {
		put(dayOfWeek, day);
	}

	private String courseIndexOfDay = "course_index_of_day";

	public Number getCourseIndexOfDay() {
		return getNumber(courseIndexOfDay);
	}

	public void setCourseIndexOfDay(Number index) {
		put(courseIndexOfDay, index);
	}

	// 日期
	private String date = "date";

	public String getDate() {
		return getString(date);
	}

	public void setDate(String d) {
		put(date, d);
	}

	// 迟到人数
	private String num_late = "num_late";

	public Number getLateCount() {
		return getNumber(num_late);
	}

	public void setLateCount(Number count) {
		put(num_late, count);
	}

	// 正常人数
	private String num_normal = "num_normal";

	public Number getNormalCount() {
		return getNumber(num_normal);
	}

	public void setNormalCount(Number count) {
		put(num_normal, count);
	}

	// 请假人数
	private String num_leave = "num_leave";

	public Number getLeaveCount() {
		return getNumber(num_leave);
	}

	public void setLeaveCount(Number count) {
		put(num_leave, count);
	}

	// 缺席人数
	private String num_absent = "num_absent";

	public Number getAbsentCount() {
		return getNumber(num_absent);
	}

	public void setAbsentCount(Number count) {
		put(num_absent, count);
	}

}
