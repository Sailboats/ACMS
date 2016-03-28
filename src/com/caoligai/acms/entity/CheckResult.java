package com.caoligai.acms.entity;

/**
 * @ClassName: CheckResult
 * @Description: �����˵�ǰʱ���ǵڼ��ڿ�,�Ƿ�׼ʱ���ٵ���ȱϯ����Ϣ
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��3��25�� ����1:30:18
 * 
 */
public class CheckResult {

	private int course_index_of_day;

	/**
	 * 1 = ׼ʱ; 2 = �ٵ�; 3 = ȱϯ; 4 = ���;
	 */
	private int checkMode;

	public CheckResult() {
	}

	public CheckResult(int course_index_of_day, int checkMode) {
		this.course_index_of_day = course_index_of_day;
		this.checkMode = checkMode;
	}

	public int getCheckMode() {
		return checkMode;
	}

	public void setCheckMode(int checkMode) {
		this.checkMode = checkMode;
	}

	public int getCourse_index_of_day() {
		return course_index_of_day;
	}

	public void setCourse_index_of_day(int course_index_of_day) {
		this.course_index_of_day = course_index_of_day;
	}

}
