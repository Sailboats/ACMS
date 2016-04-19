package com.caoligai.acms.activity;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;
import com.caoligai.acms.Setting;
import com.caoligai.acms.adapter.ListViewAdapter;
import com.caoligai.acms.avobject.Course;
import com.caoligai.acms.avobject.MyUser;
import com.caoligai.acms.dao.CourseDao;
import com.caoligai.acms.entity.Person;
import com.caoligai.acms.utils.StringHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class IndexListViewActivity extends Activity {
	private HashMap<String, Integer> selector;// 存放含有索引字母的位置
	private LinearLayout layoutIndex;
	private ListView listView;
	private TextView tv_show;
	private ListViewAdapter adapter;
	private String[] indexStr = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
	private List<Person> persons = null;
	private List<Person> newPersons = new ArrayList<Person>();
	private int height;// 字体高度
	private boolean flag = false;

	private List<Course> courses;
	private List<Course> newCourse = new ArrayList<Course>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去标题栏
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_index_listview);
		layoutIndex = (LinearLayout) this.findViewById(R.id.layout);
		// layoutIndex.setBackgroundColor(Color.parseColor("#00ffffff"));
		listView = (ListView) findViewById(R.id.listView);
		tv_show = (TextView) findViewById(R.id.tv);
		tv_show.setVisibility(View.GONE);

		final int user_type = getIntent().getIntExtra(Setting.USER_TYPE, 3);

		new Thread(new Runnable() {
			public void run() {

				// 用户类型是教师，查询该教师所教授的课程列表
				if (user_type == 1) {
					courses = CourseDao
							.getAllCourseByTeacherId(((MyUser) ((MyApplication) IndexListViewActivity.this
									.getApplication()).getmUserUtils()
									.getmAVUser()).getTeacherId());
				}
				// 用户类型是管理员，查询所有课程列表
				if (user_type == 0) {
					courses = CourseDao.getAllCourse();
				}

				Message message = myHandler.obtainMessage();
				message.obj = courses;
				myHandler.sendMessage(message);
			}
		}).start();
		/*
		 * setData(); String[] allNames = sortIndex(persons);
		 * sortList(allNames);
		 * 
		 * selector = new HashMap<String, Integer>(); for (int j = 0; j <
		 * indexStr.length; j++) {// 循环字母表，找出newPersons中对应字母的位置 for (int i = 0;
		 * i < newPersons.size(); i++) { if
		 * (newPersons.get(i).getName().equals(indexStr[j])) {
		 * selector.put(indexStr[j], i); } }
		 * 
		 * } adapter = new ListViewAdapter(this, newPersons);
		 * listView.setAdapter(adapter);
		 */
	}

	/**
	 * 重新排序获得一个新的List集合
	 * 
	 * @param allNames
	 */
	private void sortList(String[] allNames) {
		for (int i = 0; i < allNames.length; i++) {
			if (allNames[i].length() != 1) {
				for (int j = 0; j < courses.size(); j++) {
					if (allNames[i].equals(StringHelper.getPingYin(courses
							.get(j).getName().toString()))) {
						// Person p = new Person(persons.get(j).getName(),
						// persons.get(j).getPinYinName());
						newCourse.add(courses.get(j));
					}
				}
			} else {
				newCourse.add(new Course(allNames[i]));
			}
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// 在oncreate里面执行下面的代码没反应，因为oncreate里面得到的getHeight=0
		if (!flag) {// 这里为什么要设置个flag进行标记，我这里不先告诉你们，请读者研究，因为这对你们以后的开发有好处
			height = layoutIndex.getMeasuredHeight() / indexStr.length;
			getIndexView();
			flag = true;
		}
	}

	/**
	 * 获取排序后的新数据
	 * 
	 * @param persons
	 * @return
	 */
	public String[] sortIndex(List<Course> courses) {
		TreeSet<String> set = new TreeSet<String>();
		// 获取初始化数据源中的首字母，添加到set中
		for (Course course : courses) {
			set.add(StringHelper.getPinYinHeadChar(course.getName()).substring(
					0, 1));
		}
		// 新数组的长度为原数据加上set的大小
		String[] names = new String[courses.size() + set.size()];
		int i = 0;
		for (String string : set) {
			names[i] = string;
			i++;
		}
		String[] pinYinNames = new String[courses.size()];
		for (int j = 0; j < courses.size(); j++) {
			// persons.get(j).setPinYinName(StringHelper.getPingYin(persons.get(j).getName().toString()));
			pinYinNames[j] = StringHelper.getPingYin(courses.get(j).getName()
					.toString());
		}
		// 将原数据拷贝到新数据中
		System.arraycopy(pinYinNames, 0, names, set.size(), pinYinNames.length);
		// 自动按照首字母排序
		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
		return names;
	}

	/**
	 * 绘制索引列表
	 */
	public void getIndexView() {
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, height);
		for (int i = 0; i < indexStr.length; i++) {
			final TextView tv = new TextView(this);
			tv.setLayoutParams(params);
			tv.setText(indexStr[i]);
			tv.setPadding(10, 0, 10, 0);
			layoutIndex.addView(tv);
			layoutIndex.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)

				{
					float y = event.getY();
					int index = (int) (y / height);
					if (index > -1 && index < indexStr.length) {// 防止越界
						String key = indexStr[index];
						if (selector.containsKey(key)) {
							int pos = selector.get(key);
							if (listView.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
								listView.setSelectionFromTop(
										pos + listView.getHeaderViewsCount(), 0);
							} else {
								listView.setSelectionFromTop(pos, 0);// 滑动到第一项
							}
							tv_show.setVisibility(View.VISIBLE);
							tv_show.setText(indexStr[index]);
						}
					}
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						layoutIndex.setBackgroundColor(Color
								.parseColor("#606060"));
						break;

					case MotionEvent.ACTION_MOVE:

						break;
					case MotionEvent.ACTION_UP:
						layoutIndex.setBackgroundColor(Color
								.parseColor("#00ffffff"));
						tv_show.setVisibility(View.GONE);
						break;
					}
					return true;
				}
			});
		}
	}

	private Handler myHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			// setData();
			String[] allNames = sortIndex((List<Course>) msg.obj);
			sortList(allNames);

			selector = new HashMap<String, Integer>();
			for (int j = 0; j < indexStr.length; j++) {// 循环字母表，找出newPersons中对应字母的位置
				for (int i = 0; i < newCourse.size(); i++) {
					if (newCourse.get(i).getName().equals(indexStr[j])) {
						selector.put(indexStr[j], i);
					}
				}

			}
			// adapter = new ListViewAdapter(IndexListViewActivity.this,
			// newPersons);
			adapter = new ListViewAdapter(IndexListViewActivity.this, newCourse);
			listView.setAdapter(adapter);
		};

	};

}