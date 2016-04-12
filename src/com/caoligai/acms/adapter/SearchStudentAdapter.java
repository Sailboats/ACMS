package com.caoligai.acms.adapter;

import java.util.List;

import com.caoligai.acms.R;
import com.caoligai.acms.activity.SearchActivity;
import com.caoligai.acms.avobject.Student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchStudentAdapter extends BaseListViewAdapter {

	public SearchStudentAdapter(List<Object> data, Context context) {
		super(data, context);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		ViewHolder viewHolder;

		if (arg1 == null) {

			viewHolder = new ViewHolder();

			arg1 = LayoutInflater.from(getmContext()).inflate(
					R.layout.item_search_student, null);

			viewHolder.tv_name = (TextView) arg1.findViewById(R.id.tv_name);
			viewHolder.tv_xuehao = (TextView) arg1.findViewById(R.id.tv_xuehao);

			arg1.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}

		Student item = (Student) getmData().get(arg0);

		viewHolder.tv_name.setText(item.getName());
		viewHolder.tv_xuehao.setText(item.getXueHao());

		return arg1;
	}

	static class ViewHolder {
		TextView tv_name, tv_xuehao;
	}

}
