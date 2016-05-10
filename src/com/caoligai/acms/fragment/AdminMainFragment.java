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
 * @Description: ����Ա��ҳ
 * @author Noodle caoligai520402@gmail.com
 * @date 2016��3��3�� ����9:55:01
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

	// �ٶȶ�λ���
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener4teacher();

	/**
	 * �ٵ���������ȱϯ
	 */
	private TextView tv_late, tv_normal, tv_absent;

	private TextView tv_tips_no_course;

	/**
	 * �ٵ��ʣ�׼ʱ�ʣ�ȱϯ��
	 */
	private TextView percent_normal, percent_late, percent_absent;

	/**
	 * ǰһ�죬��һ��
	 */
	private ImageView iv_pre, iv_next, iv_details;
	private int offset_day = 0;
	private String date_query;

	private TextView tv_date;

	// ��λУ��
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
				// ��ʾ/���� ������λ����Ϣ��
				if (state) {
					tv_updateLocation.setVisibility(View.VISIBLE);
				} else {
					tv_updateLocation.setVisibility(View.GONE);
				}
				// ���·������ˡ��Ƿ�ʹ�ö�λУ�顱����
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
				// ��ʼ��λ������λ����Ϣ���µ� CheckItemPreview ��
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
			// ����Ա
			rl_location.setVisibility(View.GONE);
			date_query = DateUtils.getDateString();

			new Thread(new Runnable() {

				@Override
				public void run() {
					CheckSummary summary = null;
					isRefreshing = true;

					while (isRefreshing) {
						try {
							// ��ѯ��������
							int total_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("date", date_query).count();// ѧ�����˴�

							int late_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("late", true)
									.whereEqualTo("date", date_query).count(); // �ٵ��˴�
							int normal_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("normal", true)
									.whereEqualTo("date", date_query).count();// ׼ʱ�˴�
							int absent_stu = AVObject.getQuery(CheckItem.class)
									.whereEqualTo("absent", true)
									.whereEqualTo("date", date_query).count(); // ȱϯ�˴�
							LogUtils.Log_debug(tag, "ѧ�����˴Σ�" + total_stu
									+ " �ٵ��˴Σ�" + late_stu + " ׼ʱ�˴Σ�"
									+ normal_stu + " ȱϯ�˴Σ�" + absent_stu);

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
			// ��ʦ

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
						// ���ݽ�ʦid��ѯ��ǰʱ�����ڽ��п��ڵ��Լ�����Ŀγ�
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

							// ��ѯ������Ϣ
							int total_stu = CheckItem
									.getStudentCountByCourseId(courseId);
							// ѧ�����˴�

							int late_stu = CheckItem
									.getLateCountByCourseId(courseId); // �ٵ��˴�
							int normal_stu = CheckItem
									.getNormalCountByCourseId(courseId);// ׼ʱ�˴�
							int absent_stu = CheckItem
									.getAbsentCountByCourseId(courseId); // ȱϯ�˴�
							LogUtils.Log_debug(tag, "ѧ�����˴Σ�" + total_stu
									+ " �ٵ��˴Σ�" + late_stu + " ׼ʱ�˴Σ�"
									+ normal_stu + " ȱϯ�˴Σ�" + absent_stu);

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

		mLocationClient = new LocationClient(getActivity()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������

		initLocation();

		// mLocationClient.start();

	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		// int span = 1000;
		// option.setScanSpan(span);//
		// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(true);// ��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(true);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(true);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIgnoreKillProcess(false);// ��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.setEnableSimulateGps(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
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
				Toast.makeText(getActivity(), "���³ɹ�", Toast.LENGTH_SHORT)
						.show();
			}

			if (msg.what == GET_LOCATION_ERROR) {
				Toast.makeText(getActivity(),
						"��λʧ�ܣ�������Ӫ���������wifi�����Ƿ�������������ȷ�ϵ�ǰ�����ֻ������Ƿ�ͨ��",
						Toast.LENGTH_SHORT).show();
			}

		};
	};

	/**
	 * ��λ�ص�
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
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS��λ���
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// ��λ������ÿСʱ
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// ��λ����
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// ��λ��
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps��λ�ɹ�");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// ���綨λ���
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// ��Ӫ����Ϣ
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("���綨λ�ɹ�");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���
				sb.append("\ndescribe : ");
				sb.append("���߶�λ�ɹ������߶�λ���Ҳ����Ч��");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("��������綨λʧ�ܣ����Է���IMEI�źʹ��嶨λʱ�䵽loc-bugs@baidu.com��������׷��ԭ��");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("���粻ͬ���¶�λʧ�ܣ����������Ƿ�ͨ��");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("�޷���ȡ��Ч��λ���ݵ��¶�λʧ�ܣ�һ���������ֻ���ԭ�򣬴��ڷ���ģʽ��һ���������ֽ�����������������ֻ�");
			}
			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());// λ�����廯��Ϣ
			List<Poi> list = location.getPoiList();// POI����
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			Log.i("BaiduLocationApiDem", sb.toString());

			// ����λ����Ϣ�� CheckItemPreview ��
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
