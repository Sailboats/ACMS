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
	private HashMap<String, Integer> selector;// ��ź���������ĸ��λ��
	private LinearLayout layoutIndex;
	private ListView listView;
	private TextView tv_show;
	private ListViewAdapter adapter;
	private String[] indexStr = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
	private List<Person> persons = null;
	private List<Person> newPersons = new ArrayList<Person>();
	private int height;// ����߶�
	private boolean flag = false;

	private List<Course> courses;
	private List<Course> newCourse = new ArrayList<Course>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ������
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

				// �û������ǽ�ʦ����ѯ�ý�ʦ�����ڵĿγ��б�
				if (user_type == 1) {
					courses = CourseDao
							.getAllCourseByTeacherId(((MyUser) ((MyApplication) IndexListViewActivity.this
									.getApplication()).getmUserUtils()
									.getmAVUser()).getTeacherId());
				}
				// �û������ǹ���Ա����ѯ���пγ��б�
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
		 * indexStr.length; j++) {// ѭ����ĸ���ҳ�newPersons�ж�Ӧ��ĸ��λ�� for (int i = 0;
		 * i < newPersons.size(); i++) { if
		 * (newPersons.get(i).getName().equals(indexStr[j])) {
		 * selector.put(indexStr[j], i); } }
		 * 
		 * } adapter = new ListViewAdapter(this, newPersons);
		 * listView.setAdapter(adapter);
		 */
	}

	/**
	 * ����������һ���µ�List����
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
		// ��oncreate����ִ������Ĵ���û��Ӧ����Ϊoncreate����õ���getHeight=0
		if (!flag) {// ����ΪʲôҪ���ø�flag���б�ǣ������ﲻ�ȸ������ǣ�������о�����Ϊ��������Ժ�Ŀ����кô�
			height = layoutIndex.getMeasuredHeight() / indexStr.length;
			getIndexView();
			flag = true;
		}
	}

	/**
	 * ��ȡ������������
	 * 
	 * @param persons
	 * @return
	 */
	public String[] sortIndex(List<Course> courses) {
		TreeSet<String> set = new TreeSet<String>();
		// ��ȡ��ʼ������Դ�е�����ĸ����ӵ�set��
		for (Course course : courses) {
			set.add(StringHelper.getPinYinHeadChar(course.getName()).substring(
					0, 1));
		}
		// ������ĳ���Ϊԭ���ݼ���set�Ĵ�С
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
		// ��ԭ���ݿ�������������
		System.arraycopy(pinYinNames, 0, names, set.size(), pinYinNames.length);
		// �Զ���������ĸ����
		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
		return names;
	}

	/**
	 * ���������б�
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
					if (index > -1 && index < indexStr.length) {// ��ֹԽ��
						String key = indexStr[index];
						if (selector.containsKey(key)) {
							int pos = selector.get(key);
							if (listView.getHeaderViewsCount() > 0) {// ��ֹListView�б�������������û�С�
								listView.setSelectionFromTop(
										pos + listView.getHeaderViewsCount(), 0);
							} else {
								listView.setSelectionFromTop(pos, 0);// ��������һ��
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
			for (int j = 0; j < indexStr.length; j++) {// ѭ����ĸ���ҳ�newPersons�ж�Ӧ��ĸ��λ��
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