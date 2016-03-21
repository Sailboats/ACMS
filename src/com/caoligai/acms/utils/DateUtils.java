package com.caoligai.acms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.caoligai.acms.Setting;

/**
 * @ClassName: DateUtils
 * @Description: ���ڹ�����
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��3��5�� ����10:51:50
 * 
 */
public class DateUtils {

	private static String tag = "DateUtils";

	public static String getDateString(Calendar calendar, int offset) {

		StringBuilder sb = new StringBuilder();

		sb.append(calendar.get(Calendar.YEAR)).append("-")
				.append(calendar.get(Calendar.MONTH) + 1).append("-")
				.append(calendar.get(Calendar.DAY_OF_MONTH))
				.append(" " + getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));

		return sb.toString();
	}

	/**
	 * ����ƫ�Ƽ��ܼ���������
	 * 
	 * @param old_date
	 * @param offset_week
	 * @param offset_day
	 * @return
	 */
	public static String getDateString(String old_date, int offset_week,
			int offset_day) {
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
			LogUtils.Log_debug(tag, "!!!!!!!!!!!!!!!!!!����ת�����ִ���!!!!!!!!!!!!!!!");
		}

		return str_date;
	}

	/**
	 * ��ȡ�����ַ���
	 * 
	 * @param dayOfWeek
	 * @return
	 */
	public static String getDayOfWeek(int dayOfWeek) {

		String str = null;

		switch (dayOfWeek) {
		case 1:
			str = "������";
			break;
		case 2:
			str = "����һ";
			break;
		case 3:
			str = "���ڶ�";
			break;
		case 4:
			str = "������";
			break;
		case 5:
			str = "������";
			break;
		case 6:
			str = "������";
			break;
		case 7:
			str = "������";
			break;

		default:
			break;
		}

		return str;
	}

	/**
	 * ��ȡ��ʼǩ��ʱ��
	 * 
	 * @param course_index
	 *            �ڼ��ڿ�
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
	 * ��ȡ��ֹǩ��ʱ��
	 * 
	 * @param course_index
	 *            �ڼ��ڿ�
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
	 * �� Calendar ����� DAY_OF_WEEK ����ת��Ϊ LeanCloud ƽ̨�ĸ�ʽ
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
}
