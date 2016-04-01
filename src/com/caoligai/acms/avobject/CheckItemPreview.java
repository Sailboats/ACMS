package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.caoligai.acms.entity.CheckResult;
import com.caoligai.acms.utils.DateUtils;

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
		return getString("date");
	}

	public void setDate(String d) {
		put("date", d);
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

	// 自定义逻辑

	public boolean hasData() {
		return !(getLeaveCount().intValue() + getNormalCount().intValue() + getAbsentCount().intValue()
				+ getLateCount().intValue() == 0);
	}

	/**
	 * 根据所要进行考勤的课程获取对应的考勤预览项
	 * 
	 * @param course
	 * @return
	 */
	public static CheckItemPreview getCheckItemPreviewByCourse(Course course) {

		CheckItemPreview item = null;

		CheckResult checkResult = DateUtils.getCheckResult();

		AVQuery<CheckItemPreview> query = AVObject.getQuery(CheckItemPreview.class);
		// 日期和第几节唯一确定一条 CheckItemPreview 记录
		query.whereEqualTo("date", DateUtils.getDateString());
		query.whereEqualTo("course_index_of_day", checkResult.getCourse_index_of_day());

		try {
			item = query.find().get(0);
		} catch (AVException e) {
			e.printStackTrace();
		}

		return item;
	}

	/**
	 * 增加一个迟到或者准时或者缺席或者请假的数量
	 * 
	 * @param checkMode
	 *            1 = 准时; 2 = 迟到; 3 = 缺席; 4 = 请假;
	 */
	public void AddOneCount(int checkMode) {
		switch (checkMode) {
		case 1:
			setNormalCount(getNormalCount().intValue() + 1);
			break;
		case 2:
			setLateCount(getLateCount().intValue() + 1);
			break;
		case 3:
			setAbsentCount(getAbsentCount().intValue() + 1);
			break;
		case 4:
			setLeaveCount(getLeaveCount().intValue() + 1);
			break;

		default:
			break;

		}

		try {
			save();
		} catch (AVException e) {
			e.printStackTrace();
		}
	}
}
