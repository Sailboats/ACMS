package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVUser;

public class MyUser extends AVUser {

	// �û����ͣ�0 = ����Ա; 1 = ��ʦ; 2 = ѧ��;
	public Number getUserType() {
		return getNumber("usertype");
	}

	public void setUserType(int type) {
		put("usertype", type);
	}

	// ѧ��ѧ�ţ����û�����Ϊ 2 ʱ�����ֶ�Ϊѧ����ѧ��
	public String getStudentXueHao() {
		return getString("stu_xuehao");
	}

	public void setStudentXueHao(String xuehao) {
		put("stu_xuehao", xuehao);
	}

	// ѧ�����������û�����Ϊ 2 ʱ�����ֶ�Ϊѧ��������
	public String getName() {
		return getString("name");
	}

	public void setStudentName(String name) {
		put("name", name);
	}

	// ��ʦid�����û�����Ϊ1ʱ�����ֶ�Ϊ��ʦid
	public String getTeacherId() {
		return getString("teacherId");
	}

	public void setTeacherId(String teacherId) {
		put("teacherId", teacherId);
	}

}
