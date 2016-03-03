/**
 * 
 */
package com.caoligai.acms.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @ClassName: MyFragmentPagerAdapter
 * @Description: Fragment ������
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��3��3�� ����10:08:10
 * 
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;

	public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return fragments.size();
	}

}
