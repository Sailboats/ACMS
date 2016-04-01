package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("Student")
public class Student extends AVObject {
	// ÐÕÃû
	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	// Ñ§ºÅ
	public String getXueHao() {
		return getString("xuehao");
	}

	public void setXueHao(String xuehao) {
		put("xuehao", xuehao);
	}
}
