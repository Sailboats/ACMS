package com.caoligai.acms.activity;

import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.R;
import com.caoligai.acms.Setting;
import com.caoligai.acms.adapter.CheckItemPreviewAdapter;
import com.caoligai.acms.avobject.CheckItemPreview;
import com.caoligai.acms.dao.CheckItemPreviewDao;
import com.caoligai.acms.utils.LogUtils;

public class CheckItemPreviewActivity extends BaseActivity {

	private String tag = "CheckItemPreviewActivity";

	private CheckItemPreviewAdapter mAdapter;

	private ListView lv_listView;

	private ImageView iv_back;

	/**
	 * 课程id
	 */
	private String courseId;
	private int total_stu;

	private List<CheckItemPreview> data;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_checkitempreview;
	}

	@Override
	protected void initView() {
		lv_listView = (ListView) findViewById(R.id.lv_listview);
		iv_back = (ImageView) findViewById(R.id.iv_left);
	}

	@Override
	protected void initData() {
		setIsTopActivity(false);

		courseId = getIntent().getStringExtra(Setting.COURSE_ID);
		total_stu = getIntent().getIntExtra(Setting.TOTAL_STU, 0);
		LogUtils.Log_debug(tag, "课程id为： " + courseId + " 共有 " + total_stu
				+ " 人选修了该门课");

		new Thread(new Runnable() {

			@Override
			public void run() {
				data = CheckItemPreviewDao.getAllItemByCourseId(courseId);
				Message message = mHandler.obtainMessage();
				message.obj = data;
				mHandler.sendMessage(message);
			}
		}).start();
	}

	@Override
	protected void initListener() {
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			final List<CheckItemPreview> data = (List<CheckItemPreview>) msg.obj;

			mAdapter = new CheckItemPreviewAdapter((List<Object>) msg.obj,
					total_stu, CheckItemPreviewActivity.this);

			lv_listView.setAdapter(mAdapter);
			lv_listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					startActivity(new Intent(CheckItemPreviewActivity.this,
							StudentCheckDetailsActivity.class).putExtra(
							Setting.CHECKITEMPREVIEW_ID, data.get(arg2)
									.getObjectId()));
				}
			});

		};
	};

}
