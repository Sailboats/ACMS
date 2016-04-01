package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("Student")
public class Student extends AVObject {
	// ����
	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	// ѧ��
	public String getXueHao() {
		return getString("xuehao");
	}

	public void setXueHao(String xuehao) {
		put("xuehao", xuehao);
	}
}
