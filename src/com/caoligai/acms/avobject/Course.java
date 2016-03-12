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

	// �γ���
	public final String NAME = "name";

	public String getName() {
		return getString(NAME);
	}

	public void setName(String name) {
		put(NAME, name);
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

}
