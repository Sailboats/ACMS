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

	// �ٶȶ�λ���
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	/**
	 * ���ȣ�γ��
	 */
	double longitude, latitude;
	/**
	 * ǩ���ɹ�
	 */
	private static final int CHECK_SUCCESS = 1;

	/**
	 * ��һ�ſ���ǩ���Ŀγ�
	 */
	private static final int GOT_ONE_COURSE = 2;

	/**
	 * �Ѿ�ǩ��
	 */
	private static final int HAS_CHECKED = 3;

	/**
	 * ǩ��ʧ�ܣ���Ϊ���ڿ��ڷ�Χ��
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
						// TODO ǩ���ɹ�
						Message msg = mHandler.obtainMessage();
						msg.what = CHECK_SUCCESS;
						mHandler.sendMessage(msg);
					} else {
						// TODO ǩ��ʧ��
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
		LogUtils.Log_debug(tag, "��ǰ�û���ѧ�ţ� " + mUser.getStudentXueHao());

		new Thread(new Runnable() {

			@Override
			public void run() {

				while (!isCheck) {

					// ��ѯ��ǰ�Ƿ������Ҫǩ���Ŀγ�
					course = Course.getNowCanCheckCourse(mUser
							.getStudentXueHao());

					if (course != null) {
						// ����Ƿ��Ѿ�����ǩ��
						CheckItem item = CheckItem.hasChecked(course,
								mUser.getStudentXueHao());

						if (!item.getIsAbsent().booleanValue()) {
							// �Ѿ�ǩ����
							isCheck = true;
							Message msg = mHandler.obtainMessage();
							msg.what = HAS_CHECKED;
							msg.obj = item;
							mHandler.sendMessage(msg);
						} else {
							// δǩ��
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

		mLocationClient = new LocationClient(getActivity()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������

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
				Toast.makeText(StudentIndexFragment.this.getActivity(), "ǩ���ɹ�",
						Toast.LENGTH_SHORT).show();
				btn_check.setText("��ǩ��");
				btn_check.setClickable(false);
				btn_check.setOnClickListener(null);

			}

			if (msg.what == HAS_CHECKED) {
				tv_courseName.setVisibility(View.VISIBLE);
				tv_courseName.setText(course.getName());
				btn_check.setVisibility(View.VISIBLE);
				btn_check.setText("��ǩ��");
				// tv_tips.setVisibility(View.GONE);
				btn_check.setClickable(false);
				btn_check.setOnClickListener(null);
				CheckItem item = (CheckItem) msg.obj;
				tv_tips.setVisibility(View.VISIBLE);
				LogUtils.Log_debug(tag, "ǩ����ϢΪ��" + item.getIsLate() + "  "
						+ item.getIsNormal());
				if (item.getIsLate()) {
					tv_tips.setTextColor(Color.YELLOW);
					tv_tips.setText("���ѳٵ����´�Ҫ�����");
				}
				if (item.getIsNormal()) {
					tv_tips.setTextColor(Color.GREEN);
					tv_tips.setText("׼ʱǩ�������������");
				}

			}

			if (msg.what == CHECK_FAIL) {
				Toast.makeText(getActivity(),
						"ǩ��ʧ�ܣ���ȷ�����ڽ��ҷ�Χ�ڣ���������Ϊ��λʧ�ܣ�������ֻ�GPS",
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
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 5000;
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
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
	public void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
	}

	/**
	 * ��λ�ص�
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
		}
	}

}
