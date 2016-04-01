package com.caoligai.acms.activity;

import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.R;
import com.caoligai.acms.Setting;

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
		// 首先，根据 checkItemPreviewId 查询出 包含该 id 的 CheckItem 列表
		// 其次，根据 checkItemPreviewId 查询出的单个 checkItemPreview 的 courseId 查询 StudentToCourse,找出选修了当门课的所有学生 xuehao
	}

	@Override
	protected void initListener() {

	}

}
