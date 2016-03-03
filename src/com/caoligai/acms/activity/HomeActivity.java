/**
 * 
 */
package com.caoligai.acms.activity;

import java.util.ArrayList;
import java.util.List;

import com.caoligai.acms.BaseActivity;
import com.caoligai.acms.R;
import com.caoligai.acms.adapter.MyFragmentPagerAdapter;
import com.caoligai.acms.fragment.AdminCheckFragment;
import com.caoligai.acms.fragment.AdminMainFragment;
import com.caoligai.acms.fragment.AdminOtherFragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * @ClassName: HomeActivity
 * @Description:
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年2月23日 下午11:50:19
 * 
 */
public class HomeActivity extends BaseActivity implements OnClickListener {

	private ViewPager mViewPager;
	private List<Fragment> list;

	/**
	 * 底部菜单栏
	 */
	private RelativeLayout rl_btn1, rl_btn2, rl_btn3;
	/**
	 * 被选中的菜单背景色和普通状态的背景色
	 */
	private int color_selected, color_normal;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_home;
	}

	@Override
	protected void initView() {

		color_normal = new Color().rgb(255, 255, 255);
		color_selected = new Color().rgb(107, 194, 53);

		rl_btn1 = (RelativeLayout) findViewById(R.id.rl_bottom1);
		rl_btn1.setBackgroundColor(color_selected);
		rl_btn2 = (RelativeLayout) findViewById(R.id.rl_bottom2);
		rl_btn3 = (RelativeLayout) findViewById(R.id.rl_bottom3);

		initViewPager();

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initListener() {

		rl_btn1.setOnClickListener(this);
		rl_btn2.setOnClickListener(this);
		rl_btn3.setOnClickListener(this);

	}

	@SuppressWarnings("deprecation")
	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.vp_viewpager);

		list = new ArrayList<Fragment>();

		list.add(new AdminMainFragment());
		list.add(new AdminCheckFragment());
		list.add(new AdminOtherFragment());

		mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), list));
		mViewPager.setCurrentItem(0);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				setButtomState(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_bottom1:
			setButtomState(0);
			break;
		case R.id.rl_bottom2:
			setButtomState(1);
			break;
		case R.id.rl_bottom3:
			setButtomState(2);
			break;

		default:
			break;
		}

	}

	private void setButtomState(int index) {

		// 切换 Fragment
		mViewPager.setCurrentItem(index);

		// 设置被选中项的颜色
		switch (index) {
		case 0:
			rl_btn1.setBackgroundColor(color_selected);
			rl_btn2.setBackgroundColor(color_normal);
			rl_btn3.setBackgroundColor(color_normal);
			break;
		case 1:
			rl_btn1.setBackgroundColor(color_normal);
			rl_btn2.setBackgroundColor(color_selected);
			rl_btn3.setBackgroundColor(color_normal);
			break;
		case 2:
			rl_btn1.setBackgroundColor(color_normal);
			rl_btn2.setBackgroundColor(color_normal);
			rl_btn3.setBackgroundColor(color_selected);
			break;

		default:
			break;
		}

	}

}
