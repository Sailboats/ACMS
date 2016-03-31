package com.caoligai.acms.adapter;

import java.util.List;

import com.avos.avoscloud.GetDataCallback;
import com.caoligai.acms.R;
import com.caoligai.acms.avobject.CheckItemPreview;
import com.caoligai.acms.utils.LogUtils;
import com.caoligai.acms.utils.StringHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckItemPreviewAdapter extends BaseListViewAdapter {

	private String tag = "CheckItemPreviewAdapter";
	private int total_stu;

	public CheckItemPreviewAdapter(List<Object> data, Context context) {
		super(data, context);
	}

	public CheckItemPreviewAdapter(List<Object> data, int total_stu,
			Context context) {
		super(data, context);
		this.total_stu = total_stu;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {

		ViewHolder viewHolder = null;

		if (null == convertView) {
			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(getmContext()).inflate(
					R.layout.item_checkitempreview, null);

			viewHolder.ll_late = (LinearLayout) convertView
					.findViewById(R.id.ll_late);
			viewHolder.ll_normal = (LinearLayout) convertView
					.findViewById(R.id.ll_normal);
			viewHolder.ll_absent = (LinearLayout) convertView
					.findViewById(R.id.ll_absent);
			viewHolder.ll_nodatatips = (LinearLayout) convertView
					.findViewById(R.id.ll_notdatatips);
			viewHolder.ll_leave = (LinearLayout) convertView
					.findViewById(R.id.ll_leave);

			viewHolder.tv_week = (TextView) convertView
					.findViewById(R.id.tv_week);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);
			viewHolder.tv_late = (TextView) convertView
					.findViewById(R.id.tv_late);
			viewHolder.tv_normal = (TextView) convertView
					.findViewById(R.id.tv_normal);
			viewHolder.tv_absent = (TextView) convertView
					.findViewById(R.id.tv_absent);
			viewHolder.tv_leave = (TextView) convertView
					.findViewById(R.id.tv_leave);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CheckItemPreview item = (CheckItemPreview) getmData().get(arg0);

		// 如果未录入数据，则显示“未录入”提示
		if (!item.hasData()) {
			viewHolder.ll_late.setVisibility(View.GONE);
			viewHolder.ll_normal.setVisibility(View.GONE);
			viewHolder.ll_absent.setVisibility(View.GONE);
			viewHolder.ll_leave.setVisibility(View.GONE);
			viewHolder.ll_nodatatips.setVisibility(View.VISIBLE);
		} else {
			viewHolder.ll_late.setVisibility(View.VISIBLE);
			viewHolder.ll_normal.setVisibility(View.VISIBLE);
			viewHolder.ll_absent.setVisibility(View.VISIBLE);
			viewHolder.ll_leave.setVisibility(View.VISIBLE);
			viewHolder.ll_nodatatips.setVisibility(View.GONE);
		}

		viewHolder.tv_week.setText(StringHelper.getWeekString(item.getWeek()
				.intValue()));
		viewHolder.tv_time.setText(StringHelper.getWeekAndCourseIndexString(
				item.getDayOfWeek().intValue(), item.getCourseIndexOfDay()
						.intValue()));
		viewHolder.tv_date.setText(item.getDate());
		LogUtils.Log_debug(tag, "预览项日期为： " + item.getDate());
		viewHolder.tv_late.setText(item.getLateCount() + "");
		viewHolder.tv_normal.setText(item.getNormalCount() + "");
		viewHolder.tv_leave.setText(item.getLeaveCount() + "");
		viewHolder.tv_absent.setText(total_stu - item.getLateCount().intValue()
				- item.getNormalCount().intValue()
				- item.getLeaveCount().intValue() + "");

		return convertView;
	}

	static class ViewHolder {
		public LinearLayout ll_late, ll_normal, ll_absent, ll_leave,
				ll_nodatatips;
		public TextView tv_week, tv_time, tv_date, tv_late, tv_leave,
				tv_normal, tv_absent;
	}
}
