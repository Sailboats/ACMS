package com.caoligai.acms.adapter;

import java.util.List;

import com.caoligai.acms.Setting;
import com.caoligai.acms.R;
import com.caoligai.acms.activity.CheckItemPreviewActivity;
import com.caoligai.acms.avobject.CheckItemPreview;
import com.caoligai.acms.avobject.Course;
import com.caoligai.acms.entity.Person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter {
	private Context context;
	private List<Course> list;
	private ViewHolder viewHolder;

	public ListViewAdapter(Context context, List<Course> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		if (list.get(position).getName().length() == 1) // 如果是字母索引
			return false;// 表示不能点击
		return super.isEnabled(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String item = list.get(position).getName();
		final String name = item;
		final String itemId = list.get(position).getObjectId();
		final Number total_stu = list.get(position).getTotalStudents();
		viewHolder = new ViewHolder();
		if (item.length() == 1) {
			convertView = LayoutInflater.from(context).inflate(R.layout.index,
					null);
			viewHolder.indexTv = (TextView) convertView
					.findViewById(R.id.indexTv);
			viewHolder.indexTv.setText(list.get(position).getName());
		} else {
			convertView = LayoutInflater.from(context).inflate(R.layout.item,
					null);
			viewHolder.itemTv = (TextView) convertView
					.findViewById(R.id.itemTv);
			viewHolder.itemTv.setText(list.get(position).getName());
			viewHolder.itemTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Toast.makeText(context, "点击了 " + name,
					// Toast.LENGTH_SHORT)
					// .show();
					// Intent intent = new Intent(context,
					// CheckItemPreview.class);
					// Bundle bundle = new Bundle();
					// bundle.putString("CheckItemPreviewId", itemId);
					// // intent.putextra
					context.startActivity(new Intent(context,
							CheckItemPreviewActivity.class).putExtra(
							Setting.COURSE_ID, itemId).putExtra(
							Setting.TOTAL_STU, total_stu.intValue()));
				}
			});
		}
		if (item.length() == 1) {
			viewHolder.indexTv.setText(list.get(position).getName());
		} else {
			viewHolder.itemTv.setText(list.get(position).getName());
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView indexTv;
		private TextView itemTv;
	}

}
