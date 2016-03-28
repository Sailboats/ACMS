package com.caoligai.acms.entity;

/**
 * @ClassName: CheckResult
 * @Description: 包含了当前时间是第几节课,是否准时、迟到、缺席等信息
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月25日 上午1:30:18
 * 
 */
public class CheckResult {

	private int course_index_of_day;

	/**
	 * 1 = 准时; 2 = 迟到; 3 = 缺席; 4 = 请假;
	 */
	private int checkMode;

	public CheckResult() {
	}

	public CheckResult(int course_index_of_day, int checkMode) {
		this.course_index_of_day = course_index_of_day;
		this.checkMode = checkMode;
	}

	public int getCheckMode() {
		return checkMode;
	}

	public void setCheckMode(int checkMode) {
		this.checkMode = checkMode;
	}

	public int getCourse_index_of_day() {
		return course_index_of_day;
	}

	public void setCourse_index_of_day(int course_index_of_day) {
		this.course_index_of_day = course_index_of_day;
	}

}
