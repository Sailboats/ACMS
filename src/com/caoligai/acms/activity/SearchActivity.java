package com.caoligai.acms.activity;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;
import com.caoligai.acms.Setting;
import com.caoligai.acms.adapter.SearchStudentAdapter;
import com.caoligai.acms.avobject.Course;
import com.caoligai.acms.avobject.Student;
import com.caoligai.acms.avobject.StudentToCourse;
import com.caoligai.acms.utils.DialogUtils;

/**
 * @author Noodle
 * 
 */
public class SearchActivity extends BaseActivity {

	private String tag = getClass().getSimpleName();

	private static final int CHECK_STU_XUEHAO = 0;
	private static final int CHECK_STU_NAME = 1;
	private static final int CHECK_COURSE_NAME = 2;
	private static final int CHECK_COURSE = 3;

	private Button btn_check;
	private EditText ed_stu;
	private TextView tv_course;
	private ListView lv_list;
	private SearchStudentAdapter mAdapter;
	private List<Student> result = null;

	private String xuehao;
	private boolean has_select = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.caoligai.acms.BaseActivity#getLayoutId()
	 */
	@Override
	protected int getLayoutId() {
		return R.layout.activity_search;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.caoligai.acms.BaseActivity#initView()
	 */
	@Override
	protected void initView() {

		ed_stu = (EditText) findViewById(R.id.ed_stu);
		tv_course = (TextView) findViewById(R.id.ed_course);
		lv_list = (ListView) findViewById(R.id.lv_list);
		btn_check = (Button) findViewById(R.id.btn_search);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.caoligai.acms.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		setIsTopActivity(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.caoligai.acms.BaseActivity#initListener()
	 */
	@Override
	protected void initListener() {

		ed_stu.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

				String str = ed_stu.getText().toString();
				if (str.length() == 0) {
					return;
				}

				if (has_select) {
					has_select = false;
					return;
				}

				xuehao = "";

				new Thread(new Runnable() {

					@Override
					public void run() {
						if (Character.isDigit(ed_stu.getText().toString()
								.charAt(0))) { // 输入数字开头，按照学号查询
							try {
								result = AVObject
										.getQuery(Student.class)
										.whereStartsWith("xuehao",
												ed_stu.getText().toString())
										.setLimit(10).find();
								if (result.size() != 0) {
									Message msg = mHandler.obtainMessage();
									msg.obj = result;
									msg.what = CHECK_STU_XUEHAO;
									mHandler.sendMessage(msg);
								}
							} catch (AVException e) {
								e.printStackTrace();
							}
						} else { // 以姓名查询
							try {
								result = AVObject
										.getQuery(Student.class)
										.whereStartsWith("name",
												ed_stu.getText().toString())
										.setLimit(10).find();
								if (result.size() != 0) {
									Message msg = mHandler.obtainMessage();
									msg.obj = result;
									msg.what = CHECK_STU_NAME;
									mHandler.sendMessage(msg);
								}
							} catch (AVException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();

			}
		});

		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				has_select = true;
				ed_stu.setText(result.get(arg2).getXueHao());
				xuehao = result.get(arg2).getXueHao();

				result.clear();
				mAdapter.notifyDataSetChanged();
			}
		});

		tv_course.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (xuehao.equals(""))
							return;

						try {
							List<StudentToCourse> result = AVObject
									.getQuery(StudentToCourse.class)
									.whereEqualTo("student_xuehao", xuehao)
									.find();
							String[] courses = new String[result.size()];
							for (int j = 0; j < result.size(); j++) {

								courses[j] = result.get(j).getCourseName();
							}
							if (result.size() > 0) {
								Message msg = mHandler.obtainMessage();
								msg.obj = courses;
								msg.what = CHECK_COURSE;
								mHandler.sendMessage(msg);
							}
						} catch (AVException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

		btn_check.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(SearchActivity.this,
						StudentAbsentCheckActivity.class)
						.putExtra(Setting.XUEHAO, xuehao)
						.putExtra(Setting.COURSE_NAME,
								tv_course.getText().toString())
						.putExtra(
								Setting.USER_TYPE,
								((MyApplication) SearchActivity.this
										.getApplication()).getmUserUtils()
										.getUserType()));
			}
		});
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == CHECK_STU_XUEHAO) {
				// if (mAdapter == null) {
				mAdapter = new SearchStudentAdapter((List<Object>) msg.obj,
						SearchActivity.this);
				lv_list.setAdapter(mAdapter);
				// }
				mAdapter.notifyDataSetChanged();
			} else if (msg.what == CHECK_STU_NAME) {
				// if (mAdapter == null) {
				mAdapter = new SearchStudentAdapter((List<Object>) msg.obj,
						SearchActivity.this);
				lv_list.setAdapter(mAdapter);
				// }
				mAdapter.notifyDataSetChanged();
			} else if (msg.what == CHECK_COURSE) {
				final String[] course_array = (String[]) msg.obj;
				DialogUtils.showListDialog(SearchActivity.this,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								tv_course.setText(course_array[arg1]);
							}
						}, course_array);
			}

		};
	};

}
