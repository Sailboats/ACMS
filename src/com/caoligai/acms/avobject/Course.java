package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("Course")
public class Course extends AVObject {

	public Course() {
	}
	
	public Course(String name){
		setName(name);
	}

	// �γ���
	private final String NAME = "name";

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

}
