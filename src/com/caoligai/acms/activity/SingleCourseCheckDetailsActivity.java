package com.caoligai.acms.activity;

import com.caoligai.acms.Setting;
import com.caoligai.acms.avobject.CheckItem;

/**
 * µ¥½Ú¿Î¿¼ÇÚÏêÇé
 * 
 * @author Noodle
 * 
 */
public class SingleCourseCheckDetailsActivity extends
		StudentCheckDetailsActivity {
	@Override
	protected void initData() {
		final String courseId = getIntent().getStringExtra(Setting.COURSE_ID);

		new Thread(new Runnable() {

			@Override
			public void run() {
				result = CheckItem.getAllCheckItemByCourseId(courseId);
				android.os.Message msg = mHandler.obtainMessage();
				msg.obj = result;
				msg.what = INIT_DATA;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
}
