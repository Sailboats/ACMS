package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("CourseDetialTime")
public class CourseDetialTime extends AVObject {

	public CourseDetialTime() {
	}

	// �ڼ���
	private String indexOfDay = "course_index_of_day";

	public Number getIndexOfDay() {
		return getNumber(indexOfDay);
	}

	public void setIndexOfDay(Number index) {
		put(indexOfDay, index);
	}

	// ���ڼ�
	private String dayOfWeek = "day_of_week";

	public Number getDayOfWeek() {
		return getNumber(dayOfWeek);
	}

	public void setDayOfWeek(Number index) {
		put(dayOfWeek, index);
	}

}
