package com.caoligai.acms.adapter;

import java.util.List;

import com.caoligai.acms.R;
import com.caoligai.acms.avobject.CheckItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ¿¼ÇÚÏêÇé Adapter
 * 
 * @author Noodle
 * 
 */
public class CheckDetailsAdapter extends BaseListViewAdapter {

	public CheckDetailsAdapter(List<Object> data, Context context) {
		super(data, context);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		ViewHolder viewHolder;
		if (null == arg1) {

			viewHolder = new ViewHolder();

			arg1 = LayoutInflater.from(getmContext()).inflate(
					R.layout.item_check_detail, null);

			viewHolder.tv_name = (TextView) arg1.findViewById(R.id.tv_name);
			viewHolder.tv_xuehao = (TextView) arg1.findViewById(R.id.tv_xuehao);
			viewHolder.iv_normal = (ImageView) arg1
					.findViewById(R.id.iv_normal);
			viewHolder.iv_late = (ImageView) arg1.findViewById(R.id.iv_late);
			viewHolder.iv_leave = (ImageView) arg1.findViewById(R.id.iv_leave);
			viewHolder.iv_absent = (ImageView) arg1
					.findViewById(R.id.iv_absent);

			arg1.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}

		CheckItem item = (CheckItem) getmData().get(arg0);

		viewHolder.tv_name.setText(item.getStuName());
		viewHolder.tv_name.setText(item.getStuXueHao());

		if (item.getIsNormal().booleanValue()) {
			viewHolder.iv_normal.setAlpha(1f);
			viewHolder.iv_late.setAlpha(0.1f);
			viewHolder.iv_leave.setAlpha(0.1f);
			viewHolder.iv_absent.setAlpha(0.1f);
		}
		// TODO

		return null;
	}

	static class ViewHolder {
		TextView tv_name, tv_xuehao;
		ImageView iv_normal, iv_late, iv_leave, iv_absent;
	}

}
