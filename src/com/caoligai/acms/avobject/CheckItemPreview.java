package com.caoligai.acms.avobject;

import java.security.PublicKey;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * ���ڼ�¼Ԥ����
 * 
 * @author Noodle
 * 
 */
@AVClassName("CheckItemPreview")
public class CheckItemPreview extends AVObject {

	public CheckItemPreview() {
	}

	// �γ� id
	private String courseId = "courseId";

	public String getCourseId() {
		return getString(courseId);
	}

	public void setCourseId(String Id) {
		put(courseId, Id);
	}

	// �ڼ���
	private String week = "week";

	public Number getWeek() {
		return getNumber(week);
	}

	public void setWeek(Number wee) {
		put(week, wee);
	}

	// ���ڼ�
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

	// ����
	private String date = "date";

	public String getDate() {
		return getString(date);
	}

	public void setDate(String d) {
		put(date, d);
	}

	// �ٵ�����
	private String num_late = "num_late";

	public Number getLateCount() {
		return getNumber(num_late);
	}

	public void setLateCount(Number count) {
		put(num_late, count);
	}

	// ��������
	private String num_normal = "num_normal";

	public Number getNormalCount() {
		return getNumber(num_normal);
	}

	public void setNormalCount(Number count) {
		put(num_normal, count);
	}

	// �������
	private String num_leave = "num_leave";

	public Number getLeaveCount() {
		return getNumber(num_leave);
	}

	public void setLeaveCount(Number count) {
		put(num_leave, count);
	}

	// ȱϯ����
	private String num_absent = "num_absent";

	public Number getAbsentCount() {
		return getNumber(num_absent);
	}

	public void setAbsentCount(Number count) {
		put(num_absent, count);
	}

}
