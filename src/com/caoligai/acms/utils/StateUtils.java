/**
 * 
 */
package com.caoligai.acms.utils;

import com.caoligai.acms.avobject.CheckItem;

/**
 * 状态工具，集中处理考勤状态（准时、迟到、缺席、请假）相关操作
 * 
 * @author Noodle
 * 
 */
public class StateUtils {

	public static int getState(CheckItem item){
		if (item.getIsNormal()) {
			return 1;
		}
		if (item.getIsLate()) {
			return 2;
		}
		if (item.getIsAbsent()) {
			return 3;
		}
		if (item.getIsLeave()) {
			return 4;
		}
		return 0;
	}
	
}
