package com.caoligai.acms.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName: DateUtils
 * @Description: 日期工具类
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月5日 下午10:51:50
 * 
 */
public class DateUtils {

	public static String getDateString(Calendar calendar, int offset) {

		StringBuilder sb = new StringBuilder();

		sb.append(calendar.get(Calendar.YEAR)).append("-").append(calendar.get(Calendar.MONTH) + 1).append("-")
				.append(calendar.get(Calendar.DAY_OF_MONTH))
				.append(" " + getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));

		return sb.toString();
	}

	/**
	 * 获取星期字符串
	 * 
	 * @param dayOfWeek
	 * @return
	 */
	public static String getDayOfWeek(int dayOfWeek) {

		String str = null;

		switch (dayOfWeek) {
		case 1:
			str = "星期日";
			break;
		case 2:
			str = "星期一";
			break;
		case 3:
			str = "星期二";
			break;
		case 4:
			str = "星期三";
			break;
		case 5:
			str = "星期四";
			break;
		case 6:
			str = "星期五";
			break;
		case 7:
			str = "星期六";
			break;

		default:
			break;
		}

		return str;
	}

}
