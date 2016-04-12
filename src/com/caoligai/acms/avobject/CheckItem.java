package com.caoligai.acms.avobject;

import java.lang.reflect.Modifier;
import java.util.List;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.caoligai.acms.entity.CheckResult;
import com.caoligai.acms.utils.DateUtils;

/**
 * @ClassName: CheckItem
 * @Description: ����ѧ������Ч���ڼ�¼
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��3��24�� ����10:00:14
 * 
 */
@AVClassName("CheckItem")
public class CheckItem extends AVObject {

	public CheckItem() {
	}

	// ѧ��ѧ��
	public String getStuXueHao() {
		return getString("stu_xuehao");
	}

	public void setStuXueHao(String xuehao) {
		put("stu_xuehao", xuehao);
	}

	// ѧ������
	public String getStuName() {
		return getString("stu_name");
	}

	public void setStuName(String name) {
		put("stu_name", name);
	}

	// �Ƿ�ٵ�
	public Boolean getIsLate() {
		return getBoolean("late");
	}

	public void setIsLate(Boolean isLate) {
		put("late", isLate);
	}

	// �Ƿ�׼ʱ
	public Boolean getIsNormal() {
		return getBoolean("normal");
	}

	public void setIsNormal(Boolean isNormal) {
		put("normal", isNormal);
	}

	// �Ƿ�ȱϯ
	public Boolean getIsAbsent() {
		return getBoolean("absent");
	}

	public void setIsAbsent(Boolean isAbsent) {
		put("absent", isAbsent);
	}

	// �Ƿ����
	public Boolean getIsLeave() {
		return getBoolean("leave");
	}

	public void setIsLeave(Boolean isLeave) {

		put("leave", isLeave);
	}

	// �γ�id
	public String getCourseId() {
		return getString("courseId");
	}

	public void setCourseId(String courseId) {
		put("courseId", courseId);
	}

	// �ڼ���
	public Number getWeek() {
		return getNumber("week");
	}

	public void setWeek(Number week) {
		put("week", week);
	}

	// ���ڼ�
	public Number getDayOfWeek() {
		return getNumber("day_of_week");
	}

	public void setDayOfWeek(Number day_of_week) {
		put("day_of_week", day_of_week);
	}

	// �ڼ��ڿ�
	public Number getIndexOfDay() {
		return getNumber("index_of_day");
	}

	public void setIndexOfDay(Number index_of_day) {
		put("index_of_day", index_of_day);
	}

	// ����Ԥ����Id
	public String getCheckItemPreviewId() {
		return getString("checkItemPreviewId");
	}

	public void setCheckItemPreviewId(String id) {
		put("checkItemPreviewId", id);
	}

	// ����
	public String getDate() {
		return getString("date");
	}

	public void setDate(String date) {
		put("date", date);
	}

	/**
	 * ѧ��ǩ���� �½�һ�� CheckItem ��¼�����ڶ�Ӧ�Ŀ���Ԥ���CheckItemPreview��������һ���������ݣ�׼ʱ���ٵ�����ȱϯ��
	 * 
	 * @param course
	 * @param mUser
	 * @return �ɹ������� ObjectId;ʧ�ܣ����� null
	 * 
	 *         �޸�:�� hasChecked()
	 */
	public static String Check(Course course, MyUser mUser) {

		CheckItemPreview preView = CheckItemPreview
				.getCheckItemPreviewByCourse(course);
		CheckResult checkResult = DateUtils.getCheckResult();
		CheckItem item;
		try {
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
	 * ��鵱ǰʱ���Ƿ��Ѿ����й�ǩ��,�������һ�� CheckItem ��¼������ checkItemPreviewId �ֶε���Ҫ����ǩ����
	 * CheckItemPreview �� id ������ xuehao ����ѧ��ѧ�ţ�˵����ѧ���Ѿ����й�ǩ��
	 * 
	 * �޸ģ������޸����ݿ���ƣ���ԭ����ǩ��ʱ����һ�� CheckItem ��¼�޸�Ϊһ��ʼ�͸�����ѡ�γ̵��Ͽ�ʱ����Ϣ���������е� CheckItem
	 * ��¼������ �ж��Ƿ��Ѿ�����ǩ�����߼�Ҳ�޸ģ��޸����£� ϵͳ������ CheckItem ��¼�� absent �ֶ�Ĭ��Ϊ true��ǩ������
	 * absent ��Ϊ false�����ҽ� late,normal,leave ����һ������Ϊ true
	 * 
	 * @param course
	 * @return
	 */
	public static CheckItem hasChecked(Course course, String xuehao) {
		// ���Ȼ�ȡ��Ҫ����ǩ���� CheckItemPreview �� id
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
			// // �Ѿ�����ǩ��
			// return result;
			// }
		} catch (AVException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ���� CheckItemPreviewId ��ѯ���� CheckItem ��¼
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

}
