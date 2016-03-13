package com.caoligai.acms.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListViewAdapter extends BaseAdapter {

	private List<Object> mData;
	private Context mContext;

	public BaseListViewAdapter(List<Object> data, Context context) {
		mData = data;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public List<Object> getmData() {
		return mData;
	}

	public Context getmContext() {
		return mContext;
	}

	@Override
	public abstract View getView(int arg0, View arg1, ViewGroup arg2);

}
