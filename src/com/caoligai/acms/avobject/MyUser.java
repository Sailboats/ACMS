package com.caoligai.acms.avobject;

import com.avos.avoscloud.AVUser;

public class MyUser extends AVUser {
	
	public Number getUserType(){
		return getNumber("usertype");
	}
	
	public void setUserType(int type){
		put("usertype", type);
	}

}
