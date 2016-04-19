package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVUser;

public class MyUser extends AVUser {

	// 用户类型，0 = 管理员; 1 = 教师; 2 = 学生;
	public Number getUserType() {
		return getNumber("usertype");
	}

	public void setUserType(int type) {
		put("usertype", type);
	}

	// 学生学号，当用户类型为 2 时，该字段为学生的学号
	public String getStudentXueHao() {
		return getString("stu_xuehao");
	}

	public void setStudentXueHao(String xuehao) {
		put("stu_xuehao", xuehao);
	}

	// 学生姓名，当用户类型为 2 时，该字段为学生的姓名
	public String getName() {
		return getString("name");
	}

	public void setStudentName(String name) {
		put("name", name);
	}

	// 教师id，当用户类型为1时，该字段为教师id
	public String getTeacherId() {
		return getString("teacherId");
	}

	public void setTeacherId(String teacherId) {
		put("teacherId", teacherId);
	}

}
