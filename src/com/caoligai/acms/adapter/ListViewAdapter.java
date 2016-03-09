package com.caoligai.acms.adapter;

import java.util.List;

import com.caoligai.acms.R;
import com.caoligai.acms.avobject.Course;
import com.caoligai.acms.entity.Person;

import android.content.Context;
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
		if (list.get(position).getName().length() == 1) // �������ĸ����
			return false;// ��ʾ���ܵ��
		return super.isEnabled(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String item = list.get(position).getName();
		final String name = item;
		viewHolder = new ViewHolder();
		if (item.length() == 1) {
			convertView = LayoutInflater.from(context).inflate(R.layout.index, null);
			viewHolder.indexTv = (TextView) convertView.findViewById(R.id.indexTv);
			viewHolder.indexTv.setText(list.get(position).getName());
		} else {
			convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
			viewHolder.itemTv = (TextView) convertView.findViewById(R.id.itemTv);
			viewHolder.itemTv.setText(list.get(position).getName());
			viewHolder.itemTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(context, "����� " + name, Toast.LENGTH_SHORT).show();
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
