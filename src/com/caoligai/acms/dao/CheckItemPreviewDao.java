package com.caoligai.acms.dao;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.caoligai.acms.avobject.CheckItemPreview;
import com.caoligai.acms.utils.LogUtils;

/**
 * 课程预览项 Dao 类
 * 
 * @author Noodle
 * 
 */
public class CheckItemPreviewDao {

	private static String tag = "CheckItemPreviewDao";

	public static List<CheckItemPreview> getAllItemByCourseId(String courseId) {

		AVQuery<CheckItemPreview> query = AVObject
				.getQuery(CheckItemPreview.class);
		query.whereEqualTo("courseId", courseId);
		query.addAscendingOrder("createAt");

		List<CheckItemPreview> data = null;
		try {
			data = query.find();
		} catch (AVException e) {
			e.printStackTrace();
			LogUtils.Log_err(tag, "查询考勤预览项出现错误");
		}

		return data;
	}

}
