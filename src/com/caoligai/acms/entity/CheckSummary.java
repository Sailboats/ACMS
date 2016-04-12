package com.caoligai.acms.entity;

import java.math.BigDecimal;

/**
 * 管理员首页 Entity
 * 
 * @author Noodle
 * 
 */
public class CheckSummary {
	private int normal, late, absent;
	private int normal_rate, late_rate, absent_rate;

	public CheckSummary(int normal, int late, int absent, float total_stu) {
		this.normal = normal;
		this.late = late;
		this.absent = absent;

		if (total_stu == 0) {
			normal = late = absent = normal_rate = late_rate = absent_rate = 0;
			return;
		}

		normal_rate = (int) (normal / total_stu * 100);
		late_rate = (int) (late / total_stu * 100);
		absent_rate = 100 - normal_rate - late_rate;
	}

	public int getNormal() {
		return normal;
	}

	public void setNormal(int normal) {
		this.normal = normal;
	}

	public int getLate() {
		return late;
	}

	public void setLate(int late) {
		this.late = late;
	}

	public int getAbsent() {
		return absent;
	}

	public void setAbsent(int absent) {
		this.absent = absent;
	}

	public int getNormal_rate() {
		return normal_rate;
	}

	public void setNormal_rate(int normal_rate) {
		this.normal_rate = normal_rate;
	}

	public int getLate_rate() {
		return late_rate;
	}

	public void setLate_rate(int late_rate) {
		this.late_rate = late_rate;
	}

	public int getAbsent_rate() {
		return absent_rate;
	}

	public void setAbsent_rate(int absent_rate) {
		this.absent_rate = absent_rate;
	}

}
