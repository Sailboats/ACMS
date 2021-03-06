package com.caoligai.acms.avobject;

import java.lang.reflect.Modifier;
import java.util.List;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.caoligai.acms.entity.CheckResult;
import com.caoligai.acms.utils.DateUtils;
import com.caoligai.acms.utils.DistanceUtils;
import com.caoligai.acms.utils.LogUtils;

/**
 * @ClassName: CheckItem
 * @Description: 单个学生的音效考勤记录
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月24日 下午10:00:14
 * 
 */
@AVClassName("CheckItem")
public class CheckItem extends AVObject {

	private static String tag = "CheckItem";

	/**
	 * 旧状态，用于修改考勤信息时保存修改之前的状态（准时=1、迟到=2、缺席=3、请假=4）以便于更新对应的 CheckItemPreview信息
	 */
	private int old_state = 0;

	public int getOld_state() {
		return old_state;
	}

	public void setOld_state(int old_state) {
		
		if (getOld_state() == 0) {
			this.old_state = old_state;
		}
	}

	public CheckItem() {
	}

	// 学生学号
	public String getStuXueHao() {
		return getString("stu_xuehao");
	}

	public void setStuXueHao(String xuehao) {
		put("stu_xuehao", xuehao);
	}

	// 学生姓名
	public String getStuName() {
		return getString("stu_name");
	}

	public void setStuName(String name) {
		put("stu_name", name);
	}

	// 是否迟到
	public Boolean getIsLate() {
		return getBoolean("late");
	}

	public void setIsLate(Boolean isLate) {
		put("late", isLate);
	}

	// 是否准时
	public Boolean getIsNormal() {
		return getBoolean("normal");
	}

	public void setIsNormal(Boolean isNormal) {
		put("normal", isNormal);
	}

	// 是否缺席
	public Boolean getIsAbsent() {
		return getBoolean("absent");
	}

	public void setIsAbsent(Boolean isAbsent) {
		put("absent", isAbsent);
	}

	// 是否请假
	public Boolean getIsLeave() {
		return getBoolean("leave");
	}

	public void setIsLeave(Boolean isLeave) {

		put("leave", isLeave);
	}

	// 课程id
	public String getCourseId() {
		return getString("courseId");
	}

	public void setCourseId(String courseId) {
		put("courseId", courseId);
	}

	// 第几周
	public Number getWeek() {
		return getNumber("week");
	}

	public void setWeek(Number week) {
		put("week", week);
	}

	// 星期几
	public Number getDayOfWeek() {
		return getNumber("day_of_week");
	}

	public void setDayOfWeek(Number day_of_week) {
		put("day_of_week", day_of_week);
	}

	// 第几节课
	public Number getIndexOfDay() {
		return getNumber("index_of_day");
	}

	public void setIndexOfDay(Number index_of_day) {
		put("index_of_day", index_of_day);
	}

	// 考勤预览项Id
	public String getCheckItemPreviewId() {
		return getString("checkItemPreviewId");
	}

	public void setCheckItemPreviewId(String id) {
		put("checkItemPreviewId", id);
	}

	// 日期
	public String getDate() {
		return getString("date");
	}

	public void setDate(String date) {
		put("date", date);
	}

	// 课程名
	public String getCourseName() {
		return getString("courseName");
	}

	public void setCourseName(String courseName) {
		put("courseName", courseName);
	}

	/**
	 * 学生签到。 新建一条 CheckItem 记录，并在对应的考勤预览项（CheckItemPreview）中增加一个考勤数据（准时、迟到或者缺席）
	 * 
	 * @param course
	 * @param mUser
	 * @return 成功，返回 ObjectId;失败，返回 null
	 * 
	 *         修改:见 hasChecked()
	 */
	public static String Check(Course course, MyUser mUser, double longitude,
			double latitude) {

		CheckItemPreview preView = CheckItemPreview
				.getCheckItemPreviewByCourse(course);
		CheckResult checkResult = DateUtils.getCheckResult();
		CheckItem item;
		try {

			// 检查是否在考勤范围半径内
			if (preView.getUseLocationVerification()) {
				int radius = preView.getRadius().intValue();
				double target_latitude = Double.parseDouble(preView
						.getLatitude());
				double target_longitude = Double.parseDouble(preView
						.getLongitude());

				if (DistanceUtils.GetDistance(latitude, longitude,
						target_latitude, target_longitude) <= radius) {
					// 在考勤范围内
					LogUtils.Log_debug(tag, "位置校验成功");
				} else {
					LogUtils.Log_debug(tag, "位置校验失败，不在考勤范围内");
					return null;
				}
			}

			item = AVObject.getQuery(CheckItem.class)
					.whereEqualTo("stu_xuehao", mUser.getStudentXueHao())
					.whereEqualTo("checkItemPreviewId", preView.getObjectId())
					.find().get(0);
			// item.setStuXueHao(mUser.getStudentXueHao());
			// item.setStuName(mUser.getStudentName());
			item.setIsNormal(checkResult.getCheckMode() == 1 ? true : false);
			item.setIsLate(checkResult.getCheckMode() == 2 ? true : false);
			item.setIsAbsent(checkResult.getCheckMode() == 3 ? true : false);
			item.setIsLeave(checkResult.getCheckMode() == 4 ? true : false);
			// item.setCourseId(course.getObjectId());
			// item.setWeek(preView.getWeek());
			// item.setDayOfWeek(preView.getDayOfWeek());
			// item.setIndexOfDay(preView.getCourseIndexOfDay());
			// item.setCheckItemPreviewId(preView.getObjectId());

			item.save();
			preView.AddOneCount(checkResult.getCheckMode());

			return item.getObjectId();
		} catch (AVException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	/**
	 * 检查当前时刻是否已经进行过签到,如果存在一个 CheckItem 记录，它的 checkItemPreviewId 字段等于要进行签到的
	 * CheckItemPreview 的 id ，并且 xuehao 等于学生学号，说明该学生已经进行过签到
	 * 
	 * 修改：由于修改数据库设计，由原来的签到时创建一条 CheckItem 记录修改为一开始就根据所选课程的上课时间信息创建好所有的 CheckItem
	 * 记录，所以 判断是否已经进行签到的逻辑也修改，修改如下： 系统创建的 CheckItem 记录的 absent 字段默认为 true，签到过后将
	 * absent 改为 false，并且将 late,normal,leave 其中一个设置为 true
	 * 
	 * @param course
	 * @return
	 */
	public static CheckItem hasChecked(Course course, String xuehao) {
		// 首先获取想要进行签到的 CheckItemPreview 的 id
		CheckItemPreview preItem = CheckItemPreview
				.getCheckItemPreviewByCourse(course);
		String itemId = preItem.getObjectId();

		AVQuery<CheckItem> query = AVObject.getQuery(CheckItem.class);
		query.whereEqualTo("checkItemPreviewId", itemId);
		query.whereEqualTo("stu_xuehao", xuehao);
		try {
			CheckItem result = query.find().get(0);
			return result;
			// if (!result.getIsAbsent().booleanValue()) {
			// // 已经进行签到
			// return result;
			// }
		} catch (AVException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据 CheckItemPreviewId 查询所有 CheckItem 记录
	 * 
	 * @param preViewId
	 * @return
	 */
	public static List<CheckItem> getAllCheckItemByCheckItemPreviewId(
			String preViewId) {
		List<CheckItem> result = null;

		try {
			AVQuery<CheckItem> query = AVObject.getQuery(CheckItem.class);

			query.whereEqualTo("checkItemPreviewId", preViewId);
			query.setLimit(500);

			result = query.find();

		} catch (AVException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 根据课程名获取准时人数,courseId 和 date 和 index_of_day 唯一确定一条 CheckItem 记录
	 * 
	 * @param courseId
	 * @return
	 */
	public static int getNormalCountByCourseId(String courseId) {

		try {
			return AVObject
					.getQuery(CheckItem.class)
					.whereEqualTo("courseId", courseId)
					.whereEqualTo("date", DateUtils.getDateString())
					.whereEqualTo("index_of_day",
							DateUtils.getIndexOfDayByNow())
					.whereEqualTo("normal", true).count();
		} catch (AVException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 根据课程名获取迟到人数,courseId 和 date 和 index_of_day 唯一确定一条 CheckItem 记录
	 * 
	 * @param courseId
	 * @return
	 */
	public static int getLateCountByCourseId(String courseId) {

		try {
			return AVObject
					.getQuery(CheckItem.class)
					.whereEqualTo("courseId", courseId)
					.whereEqualTo("date", DateUtils.getDateString())
					.whereEqualTo("index_of_day",
							DateUtils.getIndexOfDayByNow())
					.whereEqualTo("late", true).count();
		} catch (AVException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 根据课程名获取缺席人数,courseId 和 date 和 index_of_day 唯一确定一条 CheckItem 记录
	 * 
	 * @param courseId
	 * @return
	 */
	public static int getAbsentCountByCourseId(String courseId) {

		try {
			return AVObject
					.getQuery(CheckItem.class)
					.whereEqualTo("courseId", courseId)
					.whereEqualTo("date", DateUtils.getDateString())
					.whereEqualTo("index_of_day",
							DateUtils.getIndexOfDayByNow())
					.whereEqualTo("absent", true).count();
		} catch (AVException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 根据课程名获取人数,courseId 和 date 和 index_of_day 唯一确定一条 CheckItem 记录
	 * 
	 * @param courseId
	 * @return
	 */
	public static int getStudentCountByCourseId(String courseId) {

		try {
			return AVObject
					.getQuery(CheckItem.class)
					.whereEqualTo("courseId", courseId)
					.whereEqualTo("date", DateUtils.getDateString())
					.whereEqualTo("index_of_day",
							DateUtils.getIndexOfDayByNow()).count();
		} catch (AVException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 根据 courseId 获取所有 CheckItem 记录,courseId 和 date 和 index_of_day 唯一确定一条
	 * CheckItem 记录
	 * 
	 * @param courseId
	 * @return
	 */
	public static List<CheckItem> getAllCheckItemByCourseId(String courseId) {
		try {
			return AVObject
					.getQuery(CheckItem.class)
					.whereEqualTo("courseId", courseId)
					.whereEqualTo("date", DateUtils.getDateString())
					.whereEqualTo("index_of_day",
							DateUtils.getIndexOfDayByNow()).find();
		} catch (AVException e) {
			e.printStackTrace();
		}
		return null;
	}
}
