package com.caoligai.acms.dao;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.caoligai.acms.avobject.Course;

/**
 * @ClassName: CourseDao
 * @Description: Course 表 Dao 类
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月9日 下午8:50:40
 * 
 */
public class CourseDao {

	/**
	 * 获取所有课程
	 * 
	 * @return
	 */
	public static List<Course> getAllCourse() {

		List<Course> data = null;

		AVQuery<Course> query = AVObject.getQuery(Course.class);

		try {
			data = query.find();
		} catch (AVException e) {
			e.printStackTrace();
		}

		return data;
	}

}
