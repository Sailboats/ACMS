package com.caoligai.acms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.caoligai.acms.Setting;
import com.caoligai.acms.entity.CheckResult;

/**
 * @ClassName: DateUtils
 * @Description: 日期工具类
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月5日 下午10:51:50
 * 
 */
public class DateUtils {

	private static String tag = "DateUtils";

	/**
	 * 获取指定日期的"2016-3-28 星期一"形式字符串
	 * 
	 * @param calendar
	 *            日期
	 * @param offset
	 *            偏移天数
	 * @return
	 */
	public static String getDateString(Calendar calendar, int offset) {

		StringBuilder sb = new StringBuilder();

		sb.append(calendar.get(Calendar.YEAR)).append("-").append(calendar.get(Calendar.MONTH) + 1).append("-")
				.append(calendar.get(Calendar.DAY_OF_MONTH))
				.append(" " + getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));

		return sb.toString();
	}

	/**
	 * 获取当前日期的"2016-03-28"形式字符串
	 * 
	 * @return
	 */
	public static String getDateString() {

		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 计算偏移几周几天后的日期
	 * 
	 * @param old_date
	 * @param offset_week
	 * @param offset_day
	 * @return
	 */
	public static String getDateString(String old_date, int offset_week, int offset_day) {
		String str_date = null;
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(old_date);
			Calendar old_cal = Calendar.getInstance();
			old_cal.setTime(date);

			old_cal.add(Calendar.DATE, offset_week * 7 + offset_day);

			Date new_date = old_cal.getTime();
			str_date = new SimpleDateFormat("yyyy-MM-dd").format(new_date);

		} catch (ParseException e) {
			e.printStackTrace();
			LogUtils.Log_debug(tag, "!!!!!!!!!!!!!!!!!!日期转换出现错误!!!!!!!!!!!!!!!");
		}

		return str_date;
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

	/**
	 * 获取开始签到时间
	 * 
	 * @param course_index
	 *            第几节课
	 * @return
	 */
	public static Calendar getBeginCheckTime(int course_index) {

		Calendar cal = Calendar.getInstance();

		switch (course_index) {
		case 1:
			cal.set(Calendar.HOUR_OF_DAY, Setting.COURSE_INDEX_1_BEGIN_HOUR);
			cal.set(Calendar.MINUTE, Setting.COURSE_INDEX_1_BEGIN_MINUTE);
			break;
		case 2:
			cal.set(Calendar.HOUR_OF_DAY, Setting.COURSE_INDEX_2_BEGIN_HOUR);
			cal.set(Calendar.MINUTE, Setting.COURSE_INDEX_2_BEGIN_MINUTE);
			break;
		case 3:
			cal.set(Calendar.HOUR_OF_DAY, Setting.COURSE_INDEX_3_BEGIN_HOUR);
			cal.set(Calendar.MINUTE, Setting.COURSE_INDEX_3_BEGIN_MINUTE);
			break;
		case 4:
			cal.set(Calendar.HOUR_OF_DAY, Setting.COURSE_INDEX_4_BEGIN_HOUR);
			cal.set(Calendar.MINUTE, Setting.COURSE_INDEX_4_BEGIN_MINUTE);
			break;

		default:
			break;
		}

		return cal;
	}

	/**
	 * 获取截止签到时间
	 * 
	 * @param course_index
	 *            第几节课
	 * @return
	 */
	public static Calendar getEndCheckTime(int course_index) {

		Calendar cal = Calendar.getInstance();

		switch (course_index) {
		case 1:
			cal.set(Calendar.HOUR_OF_DAY, Setting.COURSE_INDEX_1_END_HOUR);
			cal.set(Calendar.MINUTE, Setting.COURSE_INDEX_1_END_MINUTE);
			break;
		case 2:
			cal.set(Calendar.HOUR_OF_DAY, Setting.COURSE_INDEX_2_END_HOUR);
			cal.set(Calendar.MINUTE, Setting.COURSE_INDEX_2_END_MINUTE);
			break;
		case 3:
			cal.set(Calendar.HOUR_OF_DAY, Setting.COURSE_INDEX_3_END_HOUR);
			cal.set(Calendar.MINUTE, Setting.COURSE_INDEX_3_END_MINUTE);
			break;
		case 4:
			cal.set(Calendar.HOUR_OF_DAY, Setting.COURSE_INDEX_4_END_HOUR);
			cal.set(Calendar.MINUTE, Setting.COURSE_INDEX_4_END_MINUTE);
			break;

		default:
			break;
		}

		return cal;
	}

	/**
	 * 将 Calendar 对象的 DAY_OF_WEEK 属性转化为 LeanCloud 平台的格式
	 * 
	 * @param day_of_week
	 * @return
	 */
	public static int getRealDayOfWeek(int day_of_week) {

		int real_day_of_week = 0;

		switch (day_of_week) {
		case 1:
			real_day_of_week = 7;
			break;

		case 2:
			real_day_of_week = 1;
			break;

		case 3:
			real_day_of_week = 2;
			break;

		case 4:
			real_day_of_week = 3;
			break;

		case 5:
			real_day_of_week = 4;
			break;

		case 6:
			real_day_of_week = 5;
			break;

		case 7:
			real_day_of_week = 6;
			break;

		default:
			break;
		}
		return real_day_of_week;

	}

	/**
	 * 获取当前时间是第几节课，是迟到或者准时
	 * 
	 * @return
	 */
	public static CheckResult getCheckResult() {

		CheckResult result = new CheckResult();

		Date date = new Date();
		switch (date.getHours()) {
		case 7:
			result.setCourse_index_of_day(1);
			result.setCheckMode(1);
			break;
		case 8:
			result.setCourse_index_of_day(1);
			result.setCheckMode(2);
			break;
		case 9:
			if (date.getMinutes() < 45) {
				result.setCourse_index_of_day(1);
				result.setCheckMode(2);
			} else {
				result.setCourse_index_of_day(2);
				result.setCheckMode(1);
			}
			break;
		case 10:
			result.setCourse_index_of_day(2);
			result.setCheckMode(2);
			break;
		case 11:
			result.setCourse_index_of_day(2);
			result.setCheckMode(2);
			break;
		case 13:
			result.setCourse_index_of_day(3);
			result.setCheckMode(1);
			break;
		case 14:
			result.setCourse_index_of_day(3);
			result.setCheckMode(2);
			break;
		case 15:
			if (date.getMinutes() < 35) {
				result.setCourse_index_of_day(3);
				result.setCheckMode(2);
			} else {
				result.setCourse_index_of_day(4);
				result.setCheckMode(1);
			}
			break;
		case 16:
			result.setCourse_index_of_day(4);
			result.setCheckMode(2);
			break;
		case 17:
			result.setCourse_index_of_day(4);
			result.setCheckMode(2);
			break;

		default:
			break;
		}

		return result;
	}
}
