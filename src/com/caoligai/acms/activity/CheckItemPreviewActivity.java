package com.caoligai.acms.activity;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.Constants;
import com.caoligai.acms.R;
import com.caoligai.acms.adapter.CheckItemPreviewAdapter;
import com.caoligai.acms.avobject.CheckItemPreview;
import com.caoligai.acms.dao.CheckItemPreviewDao;
import com.caoligai.acms.utils.LogUtils;

public class CheckItemPreviewActivity extends BaseActivity {

	private String tag = "CheckItemPreviewActivity";

	private CheckItemPreviewAdapter mAdapter;

	private ListView lv_listView;

	/**
	 * ¿Î³Ìid
	 */
	private String courseId;

	private List<CheckItemPreview> data;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_checkitempreview;
	}

	@Override
	protected void initView() {
		lv_listView = (ListView) findViewById(R.id.lv_listview);
	}

	@Override
	protected void initData() {
		setIsTopActivity(false);

		courseId = getIntent().getStringExtra(Constants.COURSE_ID);
		LogUtils.Log_debug(tag, "¿Î³ÌidÎª£º " + courseId);

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

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			mAdapter = new CheckItemPreviewAdapter((List<Object>) msg.obj,
					CheckItemPreviewActivity.this);

			lv_listView.setAdapter(mAdapter);

		};
	};

}
