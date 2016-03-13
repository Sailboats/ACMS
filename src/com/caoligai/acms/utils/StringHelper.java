package com.caoligai.acms.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class StringHelper {
	/**
	 * �õ� ȫƴ
	 * 
	 * @param src
	 * @return
	 */
	public static String getPingYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// �ж��Ƿ�Ϊ�����ַ�
				if (java.lang.Character.toString(t1[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += t2[0];
				} else {
					t4 += java.lang.Character.toString(t1[i]);
				}
			}
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}

	/**
	 * �õ�����ĸ
	 * 
	 * @param str
	 * @return
	 */
	public static String getHeadChar(String str) {

		String convert = "";
		char word = str.charAt(0);
		String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
		if (pinyinArray != null) {
			convert += pinyinArray[0].charAt(0);
		} else {
			convert += word;
		}
		return convert.toUpperCase();
	}

	/**
	 * �õ���������ĸ��д
	 * 
	 * @param str
	 * @return
	 */
	public static String getPinYinHeadChar(String str) {

		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert.toUpperCase();
	}

	/**
	 * ���磺����5��ת���ɡ���5�ܡ�
	 * 
	 * @param str_old
	 * @return
	 */
	public static String getWeekString(int str_old) {

		StringBuilder sb = new StringBuilder();
		sb.append("�� ").append(str_old).append(" ��");

		return sb.toString();
	}

	/**
	 * ���磺���롰3,1�� ���ء������� 1��2�ڡ�
	 * 
	 * @param week_index
	 * @param course_index
	 * @return
	 */
	public static String getWeekAndCourseIndexString(int week_index,
			int course_index) {

		StringBuilder sb = new StringBuilder();

		sb.append(DateUtils.getDayOfWeek(week_index)).append(" ")
				.append(getCourseIndexString(course_index));
		return sb.toString();
	}

	/**
	 * ���磺���롰1�� ���ء�1��2�ڡ�
	 * 
	 * @param course_index
	 * @return
	 */
	public static String getCourseIndexString(int course_index) {
		String str = null;
		switch (course_index) {
		case 1:
			str = "1��2��";
			break;
		case 2:
			str = "3��4��";
			break;
		case 3:
			str = "5��6��";
			break;
		case 4:
			str = "7��8��";
			break;

		default:
			break;
		}

		return str;
	}
}