/**
 * 
 */
package com.caoligai.acms.utils;

import com.caoligai.acms.avobject.CheckItem;

/**
 * ״̬���ߣ����д�����״̬��׼ʱ���ٵ���ȱϯ����٣���ز���
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
