package com.caoligai.acms.activity;

import java.util.List;

import android.os.Handler;

import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.R;
import com.caoligai.acms.Setting;
import com.caoligai.acms.avobject.CheckItem;

/**
 * @author Noodle
 * 
 * 
 */
public class StudentCheckDetailsActivity extends BaseActivity {

	private String tag = getClass().getSimpleName();

	/**
	 * CheckItemPreviewId
	 */
	private String preId;
	
	private List<CheckItem> result;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_student_check_details;
	}

	@Override
	protected void initView() {
		setIsTopActivity(false);
	}

	@Override
	protected void initData() {
		preId = getIntent().getStringExtra(Setting.CHECKITEMPREVIEW_ID);
		// 根据 checkItemPreviewId 查询出 包含该 id 的 CheckItem 列表
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				result = CheckItem.getAllCheckItemByCheckItemPreviewId(preId);
			}
		}).start();
	}

	@Override
	protected void initListener() {

	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};

}
