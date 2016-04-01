package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.caoligai.acms.entity.CheckResult;
import com.caoligai.acms.utils.DateUtils;

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
		return getString("date");
	}

	public void setDate(String d) {
		put("date", d);
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

	// �Զ����߼�

	public boolean hasData() {
		return !(getLeaveCount().intValue() + getNormalCount().intValue() + getAbsentCount().intValue()
				+ getLateCount().intValue() == 0);
	}

	/**
	 * ������Ҫ���п��ڵĿγ̻�ȡ��Ӧ�Ŀ���Ԥ����
	 * 
	 * @param course
	 * @return
	 */
	public static CheckItemPreview getCheckItemPreviewByCourse(Course course) {

		CheckItemPreview item = null;

		CheckResult checkResult = DateUtils.getCheckResult();

		AVQuery<CheckItemPreview> query = AVObject.getQuery(CheckItemPreview.class);
		// ���ں͵ڼ���Ψһȷ��һ�� CheckItemPreview ��¼
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
	 * ����һ���ٵ�����׼ʱ����ȱϯ������ٵ�����
	 * 
	 * @param checkMode
	 *            1 = ׼ʱ; 2 = �ٵ�; 3 = ȱϯ; 4 = ���;
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
