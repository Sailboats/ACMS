package com.caoligai.acms.adapter;

import java.util.ArrayList;
import java.util.List;

import com.caoligai.acms.R;
import com.caoligai.acms.avobject.CheckItem;
import com.caoligai.acms.utils.DateUtils;
import com.caoligai.acms.utils.StringHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 考勤详情 Adapter
 * 
 * @author Noodle
 * 
 */
public class CheckDetailsAdapter extends BaseListViewAdapter {

	private List<CheckItem> opt_queen = new ArrayList<CheckItem>(); // 待操作对象队列

	public CheckDetailsAdapter(List<Object> data, Context context) {
		super(data, context);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		final ViewHolder viewHolder;
		if (null == arg1) {

			viewHolder = new ViewHolder();

			arg1 = LayoutInflater.from(getmContext()).inflate(
					R.layout.item_check_detail, null);

			viewHolder.tv_name = (TextView) arg1.findViewById(R.id.tv_name);
			viewHolder.tv_xuehao = (TextView) arg1.findViewById(R.id.tv_xuehao);
			viewHolder.iv_normal = (ImageView) arg1
					.findViewById(R.id.iv_normal);
			viewHolder.tv_week = (TextView) arg1.findViewById(R.id.tv_week);
			viewHolder.tv_day_of_week = (TextView) arg1
					.findViewById(R.id.tv_day_of_week);
			viewHolder.tv_course_index_of_day = (TextView) arg1
					.findViewById(R.id.tv_course_index_of_day);
			viewHolder.tv_date = (TextView) arg1.findViewById(R.id.tv_date);
			viewHolder.iv_late = (ImageView) arg1.findViewById(R.id.iv_late);
			viewHolder.iv_leave = (ImageView) arg1.findViewById(R.id.iv_leave);
			viewHolder.iv_absent = (ImageView) arg1
					.findViewById(R.id.iv_absent);

			arg1.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}

		final CheckItem item = (CheckItem) getmData().get(arg0);

		viewHolder.tv_name.setText(item.getStuName());
		viewHolder.tv_xuehao.setText(item.getStuXueHao());
		viewHolder.tv_week.setText(StringHelper.getWeekString(item.getWeek()
				.intValue()));
		viewHolder.tv_day_of_week.setText(DateUtils.getDayOfWeek(item
				.getDayOfWeek().intValue()));
		viewHolder.tv_course_index_of_day.setText(StringHelper
				.getCourseIndexString(item.getIndexOfDay().intValue()));
		viewHolder.tv_date.setText(item.getDate());

		viewHolder.iv_normal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewHolder.iv_normal.setAlpha(1f);
				viewHolder.iv_late.setAlpha(0.1f);
				viewHolder.iv_leave.setAlpha(0.1f);
				viewHolder.iv_absent.setAlpha(0.1f);

				item.setIsNormal(Boolean.valueOf(true));
				item.setIsLate(Boolean.valueOf(false));
				item.setIsLeave(Boolean.valueOf(false));
				item.setIsAbsent(Boolean.valueOf(false));
				opt_queen.add(item);
			}
		});
		viewHolder.iv_late.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewHolder.iv_normal.setAlpha(0.1f);
				viewHolder.iv_late.setAlpha(1f);
				viewHolder.iv_leave.setAlpha(0.1f);
				viewHolder.iv_absent.setAlpha(0.1f);

				item.setIsNormal(Boolean.valueOf(false));
				item.setIsLate(Boolean.valueOf(true));
				item.setIsLeave(Boolean.valueOf(false));
				item.setIsAbsent(Boolean.valueOf(false));
				opt_queen.add(item);
			}
		});
		viewHolder.iv_leave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewHolder.iv_normal.setAlpha(0.1f);
				viewHolder.iv_late.setAlpha(0.1f);
				viewHolder.iv_leave.setAlpha(1f);
				viewHolder.iv_absent.setAlpha(0.1f);

				item.setIsNormal(Boolean.valueOf(false));
				item.setIsLate(Boolean.valueOf(false));
				item.setIsLeave(Boolean.valueOf(true));
				item.setIsAbsent(Boolean.valueOf(false));
				opt_queen.add(item);
			}
		});
		viewHolder.iv_absent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewHolder.iv_normal.setAlpha(0.1f);
				viewHolder.iv_late.setAlpha(0.1f);
				viewHolder.iv_leave.setAlpha(0.1f);
				viewHolder.iv_absent.setAlpha(1f);

				item.setIsNormal(Boolean.valueOf(false));
				item.setIsLate(Boolean.valueOf(false));
				item.setIsLeave(Boolean.valueOf(false));
				item.setIsAbsent(Boolean.valueOf(true));
				opt_queen.add(item);
			}
		});

		if (item.getIsNormal().booleanValue()) {
			viewHolder.iv_normal.setAlpha(1f);
			viewHolder.iv_normal.setOnClickListener(null);
			viewHolder.iv_late.setAlpha(0.1f);
			viewHolder.iv_leave.setAlpha(0.1f);
			viewHolder.iv_absent.setAlpha(0.1f);
		}
		if (item.getIsLate().booleanValue()) {
			viewHolder.iv_normal.setAlpha(0.1f);
			viewHolder.iv_late.setAlpha(1f);
			viewHolder.iv_late.setOnClickListener(null);
			viewHolder.iv_leave.setAlpha(0.1f);
			viewHolder.iv_absent.setAlpha(0.1f);
		}
		if (item.getIsLeave().booleanValue()) {
			viewHolder.iv_normal.setAlpha(0.1f);
			viewHolder.iv_late.setAlpha(0.1f);
			viewHolder.iv_leave.setAlpha(1f);
			viewHolder.iv_leave.setOnClickListener(null);
			viewHolder.iv_absent.setAlpha(0.1f);
		}
		if (item.getIsAbsent().booleanValue()) {
			viewHolder.iv_normal.setAlpha(0.1f);
			viewHolder.iv_late.setAlpha(0.1f);
			viewHolder.iv_leave.setAlpha(0.1f);
			viewHolder.iv_absent.setAlpha(1f);
			viewHolder.iv_absent.setOnClickListener(null);
		}

		return arg1;
	}

	public List<CheckItem> getOpt_queen() {
		return opt_queen;
	}

	public void clearOpt_queen() {
		opt_queen.clear();
	}

	static class ViewHolder {
		TextView tv_name, tv_xuehao, tv_week, tv_day_of_week,
				tv_course_index_of_day, tv_date;
		ImageView iv_normal, iv_late, iv_leave, iv_absent;
	}

}
