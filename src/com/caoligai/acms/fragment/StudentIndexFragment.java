package com.caoligai.acms.fragment;

import java.util.Calendar;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.LocationClientOption.LocationMode;
import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;
import com.caoligai.acms.avobject.CheckItem;
import com.caoligai.acms.avobject.Course;
import com.caoligai.acms.avobject.MyUser;
import com.caoligai.acms.utils.ImageUtils;
import com.caoligai.acms.utils.LogUtils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentIndexFragment extends Fragment {

	private Course course = null;

	private TextView tv_tips, tv_courseName;
	private Button btn_check;
	private ImageView iv_course;
	private MyUser mUser;

	// 百度定位相关
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	/**
	 * 经度，纬度
	 */
	double longitude, latitude;
	/**
	 * 签到成功
	 */
	private static final int CHECK_SUCCESS = 1;

	/**
	 * 有一门可以签到的课程
	 */
	private static final int GOT_ONE_COURSE = 2;

	/**
	 * 已经签到
	 */
	private static final int HAS_CHECKED = 3;

	/**
	 * 签到失败，因为不在考勤范围内
	 */
	private static final int CHECK_FAIL = 4;

	private boolean isCheck = false;

	private boolean has_loaded_image = false;

	private String tag = getClass().getSimpleName();

	public StudentIndexFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_student_index,
				container, false);

		initView(rootView);
		return rootView;
	}

	private void initView(View view) {
		tv_tips = (TextView) view.findViewById(R.id.tv_tips);
		tv_courseName = (TextView) view.findViewById(R.id.tv_course_name);
		btn_check = (Button) view.findViewById(R.id.btn_check);
		iv_course = (ImageView) view.findViewById(R.id.iv_course_image);

		// tv_tips.setVisibility(View.GONE);
		tv_courseName.setVisibility(View.GONE);
		btn_check.setVisibility(View.GONE);
		btn_check.setOnClickListener(listener);
	}

	@Override
	public void onResume() {
		super.onResume();

		initData();
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (CheckItem.Check(course, mUser, longitude, latitude) != null) {
						// TODO 签到成功
						Message msg = mHandler.obtainMessage();
						msg.what = CHECK_SUCCESS;
						mHandler.sendMessage(msg);
					} else {
						// TODO 签到失败
						Message msg = mHandler.obtainMessage();
						msg.what = CHECK_FAIL;
						mHandler.sendMessage(msg);
					}
				}
			}).start();

		}
	};

	private void initData() {

		mUser = (MyUser) ((MyApplication) getActivity().getApplication())
				.getmUserUtils().getmAVUser();
		LogUtils.Log_debug(tag, "当前用户的学号： " + mUser.getStudentXueHao());

		new Thread(new Runnable() {

			@Override
			public void run() {

				while (!isCheck) {

					// 查询当前是否存在需要签到的课程
					course = Course.getNowCanCheckCourse(mUser
							.getStudentXueHao());

					if (course != null) {
						// 检查是否已经进行签到
						CheckItem item = CheckItem.hasChecked(course,
								mUser.getStudentXueHao());

						if (!item.getIsAbsent().booleanValue()) {
							// 已经签到过
							isCheck = true;
							Message msg = mHandler.obtainMessage();
							msg.what = HAS_CHECKED;
							msg.obj = item;
							mHandler.sendMessage(msg);
						} else {
							// 未签到
							Message msg = mHandler.obtainMessage();
							msg.what = GOT_ONE_COURSE;
							msg.obj = course;
							mHandler.sendMessage(msg);
						}

					}

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		}).start();

		mLocationClient = new LocationClient(getActivity()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		initLocation();

		mLocationClient.start();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == GOT_ONE_COURSE) {
				tv_tips.setVisibility(View.GONE);
				tv_courseName.setVisibility(View.VISIBLE);
				btn_check.setVisibility(View.VISIBLE);
				btn_check.setClickable(true);
				btn_check.setOnClickListener(listener);

				tv_courseName.setText(course.getName());

			}

			if (msg.what == CHECK_SUCCESS) {
				Toast.makeText(StudentIndexFragment.this.getActivity(), "签到成功",
						Toast.LENGTH_SHORT).show();
				btn_check.setText("已签到");
				btn_check.setClickable(false);
				btn_check.setOnClickListener(null);

			}

			if (msg.what == HAS_CHECKED) {
				tv_courseName.setVisibility(View.VISIBLE);
				tv_courseName.setText(course.getName());
				btn_check.setVisibility(View.VISIBLE);
				btn_check.setText("已签到");
				// tv_tips.setVisibility(View.GONE);
				btn_check.setClickable(false);
				btn_check.setOnClickListener(null);
				CheckItem item = (CheckItem) msg.obj;
				tv_tips.setVisibility(View.VISIBLE);
				LogUtils.Log_debug(tag, "签到信息为：" + item.getIsLate() + "  "
						+ item.getIsNormal());
				if (item.getIsLate()) {
					tv_tips.setTextColor(Color.YELLOW);
					tv_tips.setText("你已迟到，下次要早点来");
				}
				if (item.getIsNormal()) {
					tv_tips.setTextColor(Color.GREEN);
					tv_tips.setText("准时签到，请继续保持");
				}

			}

			if (msg.what == CHECK_FAIL) {
				Toast.makeText(getActivity(),
						"签到失败，请确保你在教室范围内，或者是因为定位失败，建议打开手机GPS",
						Toast.LENGTH_SHORT).show();
			}

			if (!has_loaded_image) {
				ImageUtils.displayImage(course.getImageUrl(), iv_course);
				has_loaded_image = true;
			}

		};
	};

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 5000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
	}

	/**
	 * 定位回调
	 * 
	 * @author Noodle
	 * 
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			latitude = location.getLatitude();
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			longitude = location.getLongitude();
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());// 位置语义化信息
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			Log.i("BaiduLocationApiDem", sb.toString());
		}
	}

}
