//package com.cgwx.yyfwptz.lixiang.AQBP.view.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//
//import com.baidu.location.BDAbstractLocationListener;
//import com.baidu.location.BDLocation;
//import com.baidu.location.LocationClientOption;
//import com.baidu.location.Poi;
//import com.cgwx.yyfwptz.lixiang.AQBApplication;
//import com.cgwx.yyfwptz.lixiang.AQBP.R;
//import com.cgwx.yyfwptz.lixiang.AQBP.util.LocationService;
//
//
//public class testLocationActivity extends AppCompatActivity {
//
//    private LocationService locService;
//    private LocationClientOption mOption;
//    private LocationService locationService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_location);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        locService =  ((AQBApplication)getApplication()).locationService;
//        mOption = new LocationClientOption();
//        mOption = locService.getDefaultLocationClientOption();
//        mOption.setOpenAutoNotifyMode(); //设置默认值
//
//
//        int setSensitivity = LocationClientOption.LOC_SENSITIVITY_HIGHT;
//        mOption.setOpenAutoNotifyMode(60000,100,setSensitivity);
//        locService.setLocationOption(mOption);
////        locationService.setLocationOption(locationService.getOption());
//
//        Intent locIntent = new Intent(testLocationActivity.this, LocationActivity.class);
//        locIntent.putExtra("from", 1);
//        testLocationActivity.this.startActivity(locIntent);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
////    @Override
////    protected void onStart() {
////        super.onStart();
////        locationService = ((AQBApplication) getApplication()).locationService;
////        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
////        locationService.registerListener(mListener);
////        locationService.setLocationOption(locationService.getOption());
////    }
////
////    @Override
////    protected void onStop() {
////        // TODO Auto-generated method stub
////        locationService.unregisterListener(mListener); //注销掉监听
////        locationService.stop(); //停止定位服务
////        super.onStop();
////    }
//
////    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
////
////        @Override
////        public void onReceiveLocation(BDLocation location) {
////            // TODO Auto-generated method stub
////            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
////                StringBuffer sb = new StringBuffer(256);
////                sb.append("time : ");
////                /**
////                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
////                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
////                 */
////                sb.append(location.getTime());
////                sb.append("\nlocType : ");// 定位类型
////                sb.append(location.getLocType());
////                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
////                sb.append(location.getLocTypeDescription());
////                sb.append("\nlatitude : ");// 纬度
////                sb.append(location.getLatitude());
////                sb.append("\nlontitude : ");// 经度
////                sb.append(location.getLongitude());
////                sb.append("\nradius : ");// 半径
////                sb.append(location.getRadius());
////                sb.append("\nCountryCode : ");// 国家码
////                sb.append(location.getCountryCode());
////                sb.append("\nCountry : ");// 国家名称
////                sb.append(location.getCountry());
////                sb.append("\ncitycode : ");// 城市编码
////                sb.append(location.getCityCode());
////                sb.append("\ncity : ");// 城市
////                sb.append(location.getCity());
////                sb.append("\nDistrict : ");// 区
////                sb.append(location.getDistrict());
////                sb.append("\nStreet : ");// 街道
////                sb.append(location.getStreet());
////                sb.append("\naddr : ");// 地址信息
////                sb.append(location.getAddrStr());
////                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
////                sb.append(location.getUserIndoorState());
////                sb.append("\nDirection(not all devices have value): ");
////                sb.append(location.getDirection());// 方向
////                sb.append("\nlocationdescribe: ");
////                sb.append(location.getLocationDescribe());// 位置语义化信息
////                sb.append("\nPoi: ");// POI信息
////                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
////                    for (int i = 0; i < location.getPoiList().size(); i++) {
////                        Poi poi = (Poi) location.getPoiList().get(i);
////                        sb.append(poi.getName() + ";");
////                    }
////                }
////                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
////                    sb.append("\nspeed : ");
////                    sb.append(location.getSpeed());// 速度 单位：km/h
////                    sb.append("\nsatellite : ");
////                    sb.append(location.getSatelliteNumber());// 卫星数目
////                    sb.append("\nheight : ");
////                    sb.append(location.getAltitude());// 海拔高度 单位：米
////                    sb.append("\ngps status : ");
////                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
////                    sb.append("\ndescribe : ");
////                    sb.append("gps定位成功");
////                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
////                    // 运营商信息
////                    if (location.hasAltitude()) {// *****如果有海拔高度*****
////                        sb.append("\nheight : ");
////                        sb.append(location.getAltitude());// 单位：米
////                    }
////                    sb.append("\noperationers : ");// 运营商信息
////                    sb.append(location.getOperators());
////                    sb.append("\ndescribe : ");
////                    sb.append("网络定位成功");
////                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
////                    sb.append("\ndescribe : ");
////                    sb.append("离线定位成功，离线定位结果也是有效的");
////                } else if (location.getLocType() == BDLocation.TypeServerError) {
////                    sb.append("\ndescribe : ");
////                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
////                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
////                    sb.append("\ndescribe : ");
////                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
////                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
////                    sb.append("\ndescribe : ");
////                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
////                }
////                System.out.println(sb.toString());
////            }
////
////        }
////
////    };
//
//}
