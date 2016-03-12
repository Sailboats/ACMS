package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("CourseDetialTime")
public class CourseDetialTime extends AVObject {

	public CourseDetialTime() {
	}

	// 第几节
	private String indexOfDay = "course_index_of_day";

	public Number getIndexOfDay() {
		return getNumber(indexOfDay);
	}

	public void setIndexOfDay(Number index) {
		put(indexOfDay, index);
	}

	// 星期几
	private String dayOfWeek = "day_of_week";

	public Number getDayOfWeek() {
		return getNumber(dayOfWeek);
	}

	public void setDayOfWeek(Number index) {
		put(dayOfWeek, index);
	}

}
