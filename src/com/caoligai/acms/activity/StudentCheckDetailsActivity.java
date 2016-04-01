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
		// ���ȣ����� checkItemPreviewId ��ѯ�� ������ id �� CheckItem �б�
		// ��Σ����� checkItemPreviewId ��ѯ���ĵ��� checkItemPreview �� courseId ��ѯ StudentToCourse,�ҳ�ѡ���˵��ſε�����ѧ�� xuehao
	}

	@Override
	protected void initListener() {

	}

}
