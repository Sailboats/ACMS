package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.caoligai.acms.utils.LogUtils;

@AVClassName("StudentToCourse")
public class StudentToCourse extends AVObject {

	private String tag = getClass().getSimpleName();

	public String getCourseId() {
		return getString("courseId");
	}

	public void setCourseId(String courseid) {
		put("courseId", courseid);
	}

	public String getXuehao() {
		return getString("student_xuehao");
	}

	public void setXuehao(String xuehao) {
		put("student_xuehao", xuehao);
	}
}
