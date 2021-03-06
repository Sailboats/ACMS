package com.caoligai.acms.activity;

import java.util.List;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.R;
import com.caoligai.acms.Setting;
import com.caoligai.acms.adapter.CheckDetailsAdapter;
import com.caoligai.acms.avobject.CheckItem;
import com.caoligai.acms.avobject.CheckItemPreview;
import com.caoligai.acms.utils.LogUtils;

/**
 * @author Noodle
 * 
 * 
 */
public class StudentCheckDetailsActivity extends BaseActivity {

	private String tag = getClass().getSimpleName();

	protected static final int INIT_DATA = 0;
	private static final int UPDATE_DATA = 1;

	/**
	 * “提交”“取消”按钮
	 */
	private LinearLayout ll_edit;

	/**
	 * CheckItemPreviewId
	 */
	private String preId;

	protected List<CheckItem> result;

	private ListView lv_list;
	private CheckDetailsAdapter mAdapter;

	private TextView tv_summit, tv_cancel;
	private ImageView iv_back;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_student_check_details;
	}

	@Override
	protected void initView() {
		setIsTopActivity(false);

		ll_edit = (LinearLayout) findViewById(R.id.ll_edit);

		lv_list = (ListView) findViewById(R.id.lv_list);

		tv_summit = (TextView) findViewById(R.id.tv_summit);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);

		iv_back = (ImageView) findViewById(R.id.iv_left);
	}

	/**
	 * 是否显示“提交、取消”按钮，是否允许修改
	 * 
	 * @param isShow
	 */
	protected void showOrHideEditLayout(boolean isShow) {
		if (!isShow) {
			ll_edit.setVisibility(View.GONE);
		}
	}

	@Override
	protected void initData() {
		preId = getIntent().getStringExtra(Setting.CHECKITEMPREVIEW_ID);
		// 根据 checkItemPreviewId 查询出 包含该 id 的 CheckItem 列表
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 根据 CheckItemPreviewId 查询全部考勤记录
				result = CheckItem.getAllCheckItemByCheckItemPreviewId(preId);
				android.os.Message msg = mHandler.obtainMessage();
				msg.obj = result;
				msg.what = INIT_DATA;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	protected void initListener() {

		tv_summit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						List<CheckItem> queen = mAdapter.getOpt_queen();
						LogUtils.Log_debug(tag, "待操作队列中共有 " + queen.size()
								+ " 个对象");
						for (CheckItem checkItem : queen) {
							try {
								checkItem.save();
								// 更新考勤预览项信息
								CheckItemPreview
										.updateCountCauseByCheckItem(checkItem);
							} catch (AVException e) {
								e.printStackTrace();
							}
						}
						android.os.Message msg = mHandler.obtainMessage();
						msg.what = UPDATE_DATA;
						mHandler.sendMessage(msg);
					}
				}).start();
			}
		});
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mAdapter.clearOpt_queen();
				LogUtils.Log_debug(tag, "清理待操作队列");
				finish();
			}
		});

		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == INIT_DATA) {
				mAdapter = new CheckDetailsAdapter((List<Object>) msg.obj,
						StudentCheckDetailsActivity.this);
				lv_list.setAdapter(mAdapter);
			} else if (msg.what == UPDATE_DATA) {
				showToast("修改成功");
			}

		};
	};

}
