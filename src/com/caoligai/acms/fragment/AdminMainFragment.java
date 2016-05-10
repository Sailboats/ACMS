package com.caoligai.acms.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.LocationClientOption.LocationMode;
import com.caoligai.acms.MyApplication;
import com.caoligai.acms.R;
import com.caoligai.acms.Setting;
import com.caoligai.acms.activity.SingleCourseCheckDetailsActivity;
import com.caoligai.acms.activity.StudentCheckDetailsActivity;
import com.caoligai.acms.avobject.CheckItem;
import com.caoligai.acms.avobject.CheckItemPreview;
import com.caoligai.acms.avobject.Course;
import com.caoligai.acms.avobject.MyUser;
import com.caoligai.acms.entity.CheckSummary;
import com.caoligai.acms.fragment.StudentIndexFragment.MyLocationListener;
import com.caoligai.acms.utils.DateUtils;
import com.caoligai.acms.utils.LogUtils;
import com.caoligai.acms.widget.SwitchButton;
import com.caoligai.acms.widget.SwitchButton.OnChangeListener;

/**
 * @ClassName: AdminMainFragment
 * @Description: 管理员主页
 * @author Noodle caoligai520402@gmail.com
 * @date 2016年3月3日 下午9:55:01
 * 
 */
public class AdminMainFragment extends Fragment implements OnClickListener {

	private String tag = "AdminMainFragment";

	private static final int ADMIN = 0;
	private static final int TEACHER_COURSE = 1;
	private static final int TEACHER_CHECK = 2;
	private static final int LOCATION_INFO = 3;
	private static final int UPDATE_LOCATION = 4;
	private static final int GET_LOCATION_ERROR = 5;

	private boolean isRefreshing = false;
	private String courseId = "";

	// 百度定位相关
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener4teacher();

	/**
	 * 迟到，正常，缺席
	 */
	private TextView tv_late, tv_normal, tv_absent;

	private TextView tv_tips_no_course;

	/**
	 * 迟到率，准时率，缺席率
	 */
	private TextView percent_normal, percent_late, percent_absent;

	/**
	 * 前一天，后一天
	 */
	private ImageView iv_pre, iv_next, iv_details;
	private int offset_day = 0;
	private String date_query;

	private TextView tv_date;

	// 定位校验
	private RelativeLayout rl_location;
	private SwitchButton sb_location;
	private TextView tv_updateLocation;
	private double longitude, latitude;

	public AdminMainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admin_main,
				container, false);

		initView(rootView);

		return rootView;
	}

	private void initView(View rootView) {
		tv_late = (TextView) rootView.findViewById(R.id.tv_late);
		tv_normal = (TextView) rootView.findViewById(R.id.tv_normal);
		tv_absent = (TextView) rootView.findViewById(R.id.tv_absent);
		tv_tips_no_course = (TextView) rootView
				.findViewById(R.id.tv_tips_no_course);

		percent_normal = (TextView) rootView
				.findViewById(R.id.tv_percent_normal);
		percent_late = (TextView) rootView.findViewById(R.id.tv_percent_late);
		percent_absent = (TextView) rootView
				.findViewById(R.id.tv_percent_absent);

		rl_location = (RelativeLayout) rootView.findViewById(R.id.rl_location);
		sb_location = (SwitchButton) rootView.findViewById(R.id.sb_location);
		tv_updateLocation = (TextView) rootView
				.findViewById(R.id.tv_updatelacation);
		sb_location.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChange(SwitchButton sb, boolean state) {
				final boolean button_state = state;
				// 显示/隐藏 “更新位置信息”
				if (state) {
					tv_updateLocation.setVisibility(View.VISIBLE);
				} else {
					tv_updateLocation.setVisibility(View.GONE);
				}
				// 更新服务器端“是否使用定位校验”数据
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (courseId != null || !courseId.equals("")) {
							try {
								CheckItemPreview item = CheckItemPreview
										.getCheckItemPreviewByCourse(AVObject
												.getQuery(Course.class).get(
														courseId));

								item.setUseLocationVerification(button_state ? true
										: false);

								item.save();

							} catch (AVException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		});

		tv_updateLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 开始定位，并将位置信息更新到 CheckItemPreview 中
				if (mLocationClient != null) {
					mLocationClient.start();
				}
			}
		});

		iv_pre = (ImageView) rootView.findViewById(R.id.iv_preday);
		iv_next = (ImageView) rootView.findViewById(R.id.iv_nextday);
		iv_details = (ImageView) rootView.findViewById(R.id.iv_details);
		iv_details.setOnClickListener(this);
		iv_pre.setOnClickListener(this);
		iv_next.setOnClickListener(this);

		tv_date = (TextView) rootView.findViewById(R.id.tv_date);
		tv_date.setText(DateUtils.getDateString(Calendar.getInstance(), 0));

	}

	@Override
	public void onResume() {
		super.onResume();

		if (isRefreshing) {
			return;
		}

		if (((MyUser) ((MyApplication) getActivity().getApplication())
				.getmUserUtils().getmAVUser()).getUserType().intValue() == 0) {
			// 管理员
			rl_location.setVisibility(View.GONE);
			date_query = DateUtils.getDateString();

			new Thread(new Runnable() {

				@Override
				public void run() {
					CheckSummary summary = null;
					isRefreshing = true;

					while (isRefreshing) {
						try {
							// 查询最新数据
							int total_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("date", date_query).count();// 学生总人次

							int late_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("late", true)
									.whereEqualTo("date", date_query).count(); // 迟到人次
							int normal_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("normal", true)
									.whereEqualTo("date", date_query).count();// 准时人次
							int absent_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("absent", true)
									.whereEqualTo("date", date_query).count(); // 缺席人次
							LogUtils.Log_debug(tag, "学生总人次：" + total_stu
									+ " 迟到人次：" + late_stu + " 准时人次："
									+ normal_stu + " 缺席人次：" + absent_stu);

							summary = new CheckSummary(normal_stu, late_stu,
									absent_stu, total_stu);

						} catch (AVException e) {
							e.printStackTrace();
						}

						Message msg = mHandler.obtainMessage();
						msg.what = ADMIN;
						msg.obj = summary;
						mHandler.sendMessage(msg);

						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}).start();
		}

		if (((MyUser) ((MyApplication) getActivity().getApplication())
				.getmUserUtils().getmAVUser()).getUserType().intValue() == 1) {
			// 教师

			rl_location.setVisibility(View.GONE);
			iv_pre.setVisibility(View.GONE);
			iv_next.setVisibility(View.GONE);
			tv_tips_no_course.setVisibility(View.VISIBLE);
			tv_date.setText("");

			new Thread(new Runnable() {

				@Override
				public void run() {
					isRefreshing = true;
					String teacherId = ((MyUser) ((MyApplication) getActivity()
							.getApplication()).getmUserUtils().getmAVUser())
							.getTeacherId();
					while (isRefreshing) {
						// 根据教师id查询当前时刻正在进行考勤的自己开设的课程
						Course course = Course
								.getNowCanCheckCourseByTeacherId(teacherId);

						if (course != null) {
							Message msg = mHandler.obtainMessage();
							msg.what = TEACHER_COURSE;
							msg.obj = course;
							mHandler.sendMessage(msg);

							// rl_location.setVisibility(View.VISIBLE);
							CheckItemPreview item = CheckItemPreview
									.getCheckItemPreviewByCourse(course);
							Message msg2 = mHandler.obtainMessage();
							msg2.what = LOCATION_INFO;
							msg2.obj = item;
							mHandler.sendMessage(msg2);

							courseId = course.getObjectId();

							CheckSummary summary = null;

							// 查询考勤信息
							int total_stu = CheckItem
									.getStudentCountByCourseId(courseId);
							// 学生总人次

							int late_stu = CheckItem
									.getLateCountByCourseId(courseId); // 迟到人次
							int normal_stu = CheckItem
									.getNormalCountByCourseId(courseId);// 准时人次
							int absent_stu = CheckItem
									.getAbsentCountByCourseId(courseId); // 缺席人次
							LogUtils.Log_debug(tag, "学生总人次：" + total_stu
									+ " 迟到人次：" + late_stu + " 准时人次："
									+ normal_stu + " 缺席人次：" + absent_stu);

							summary = new CheckSummary(normal_stu, late_stu,
									absent_stu, total_stu);

							summary = new CheckSummary(normal_stu, late_stu,
									absent_stu, total_stu);

							Message message = mHandler.obtainMessage();
							message.what = TEACHER_CHECK;
							message.obj = summary;
							mHandler.sendMessage(message);
						}

						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}).start();

		}

		mLocationClient = new LocationClient(getActivity()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		initLocation();

		// mLocationClient.start();

	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		// int span = 1000;
		// option.setScanSpan(span);//
		// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_preday:
			offset_day--;
			tv_date.setText(DateUtils.getDateString(Calendar.getInstance(),
					offset_day));
			date_query = DateUtils.getDateString(new SimpleDateFormat(
					"yyyy-MM-dd").format(new Date()), 0, offset_day);
			break;
		case R.id.iv_nextday:
			offset_day++;
			tv_date.setText(DateUtils.getDateString(Calendar.getInstance(),
					offset_day));
			date_query = DateUtils.getDateString(new SimpleDateFormat(
					"yyyy-MM-dd").format(new Date()), 0, offset_day);
			break;

		case R.id.iv_details:
			startActivity(new Intent(getActivity(),
					SingleCourseCheckDetailsActivity.class).putExtra(
					Setting.COURSE_ID, courseId));
			break;

		default:
			break;
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == ADMIN) {
				CheckSummary summary = (CheckSummary) msg.obj;

				tv_normal.setText(summary.getNormal() + "");
				percent_normal.setText(summary.getNormal_rate() + "");

				tv_late.setText(summary.getLate() + "");
				percent_late.setText(summary.getLate_rate() + "");

				tv_absent.setText(summary.getAbsent() + "");
				percent_absent.setText(summary.getAbsent_rate() + "");
			}

			if (msg.what == TEACHER_COURSE) {
				Course course = (Course) msg.obj;
				tv_date.setText(course.getName());
				tv_tips_no_course.setVisibility(View.GONE);
				iv_details.setVisibility(View.VISIBLE);
			}

			if (msg.what == TEACHER_CHECK) {
				CheckSummary summary = (CheckSummary) msg.obj;

				tv_normal.setText(summary.getNormal() + "");
				percent_normal.setText(summary.getNormal_rate() + "");

				tv_late.setText(summary.getLate() + "");
				percent_late.setText(summary.getLate_rate() + "");

				tv_absent.setText(summary.getAbsent() + "");
				percent_absent.setText(summary.getAbsent_rate() + "");

			}

			if (msg.what == LOCATION_INFO) {
				rl_location.setVisibility(View.VISIBLE);
				CheckItemPreview item = (CheckItemPreview) msg.obj;
				if (item.getUseLocationVerification()) {
					sb_location.setFlage(true);
					tv_updateLocation.setVisibility(View.VISIBLE);
				} else {
					sb_location.setFlage(false);
					tv_updateLocation.setVisibility(View.GONE);
				}
			}

			if (msg.what == UPDATE_LOCATION) {
				Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT)
						.show();
			}

			if (msg.what == GET_LOCATION_ERROR) {
				Toast.makeText(getActivity(),
						"定位失败，请检查运营商网络或者wifi网络是否正常开启，请确认当前测试手机网络是否通畅",
						Toast.LENGTH_SHORT).show();
			}

		};
	};

	/**
	 * 定位回调
	 * 
	 * @author Noodle
	 * 
	 */
	public class MyLocationListener4teacher implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			if (162 <= location.getLocType() && location.getLocType() <= 167) {
				Message msg = mHandler.obtainMessage();
				msg.what = GET_LOCATION_ERROR;
				mHandler.sendMessage(msg);
				return;
			}
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

			// 更新位置信息到 CheckItemPreview 上
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (courseId != null || !courseId.equals("")) {
						try {
							CheckItemPreview item = CheckItemPreview
									.getCheckItemPreviewByCourse(AVObject
											.getQuery(Course.class).get(
													courseId));

							item.setLatitude(latitude + "");
							item.setLongitude(longitude + "");

							item.save();

							Message msg = mHandler.obtainMessage();
							msg.what = UPDATE_LOCATION;
							mHandler.sendMessage(msg);

						} catch (AVException e) {
							e.printStackTrace();
						}
					}

				}
			}).start();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
	}
}
