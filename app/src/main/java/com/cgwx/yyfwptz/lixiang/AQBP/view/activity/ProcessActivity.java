package com.cgwx.yyfwptz.lixiang.AQBP.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.cgwx.yyfwptz.lixiang.AQBP.R;
import com.cgwx.yyfwptz.lixiang.AQBP.di.components.DaggerProcessComponent;
import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.ProcessModule;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.MyOrientationListener;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.OverlayManager;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.RouteLineAdapter;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.WalkingRouteOverlay;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.ProcessContract;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.ProcessPresenter;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

public class ProcessActivity extends AppCompatActivity implements OnGetRoutePlanResultListener, ProcessContract.View {

    @Inject
    ProcessPresenter presenter;
    @BindView(R.id.navi)
    Button navi;
    @BindView(R.id.donePo)
    Button donePo;
    @BindView(R.id.call)
    ImageView call;
    @BindView(R.id.poi)
    TextView poi;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.mOrkm)
    TextView mOrkm;

    public static boolean index = false;
    boolean isFirstLoc = true;
    BitmapDescriptor start;
    BitmapDescriptor end;
    String infos[];
    String reserved[];
    private Marker mMarkerA;
    private Marker mMarkerB;
    int nodeIndex = -1;
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = true;
    RoutePlanSearch mSearch = null;
    WalkingRouteResult nowResultwalk = null;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mlocationClient;
    private MylocationListener mlistener;
    private Context context;
    private double mLatitude;
    private double mLongitude;
    private float mCurrentX;
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private MyLocationConfiguration.LocationMode locationMode;
    public static ProcessActivity pa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_process);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));

        pa = this;
        ButterKnife.bind(this);
        DaggerProcessComponent.builder().processModule(new ProcessModule(this))
                .build()
                .inject(this);


        this.context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + infos[9]));
                startActivity(intent);
            }
        });

        start = BitmapDescriptorFactory.fromResource(R.drawable.start);
        end = BitmapDescriptorFactory.fromResource(R.drawable.end);

        donePo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String policeid = "";
                String alarmid = "";
                if (infos == null) {
                    policeid = reserved[8];
                    alarmid = reserved[7];
                } else {
                    policeid = infos[8];
                    alarmid = infos[7];
                }
                presenter.completeAlarm(policeid, alarmid);
            }
        });

        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng pt1;
                LatLng pt2;
                if (reserved == null) {
                    System.out.println("this");
                    pt1 = new LatLng(Double.valueOf(infos[0]), Double.valueOf(infos[1]));
                    pt2 = new LatLng(Double.valueOf(infos[2]), Double.valueOf(infos[3]));
                } else {
                    System.out.println("that");
                    pt1 = new LatLng(Double.valueOf(reserved[0]), Double.valueOf(reserved[1]));
                    pt2 = new LatLng(Double.valueOf(reserved[2]), Double.valueOf(reserved[3]));
                }
                NaviParaOption para = new NaviParaOption()
                        .startPoint(pt2).endPoint(pt1)
                        .startName("天安门").endName("百度大厦");

                try {
                    BaiduMapNavigation.openBaiduMapWalkNavi(para, ProcessActivity.this);
                } catch (BaiduMapAppNotSupportNaviException e) {
                    e.printStackTrace();
                    showDialog();
//                    Toast.makeText(ProcessActivity.this, "您尚未安装百度地图app或app版本过低", Toast.LENGTH_SHORT).show();
                }

            }
        });
        initView();
        initLocation();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        reserved = bundle.getStringArray("reserved");
        infos = bundle.getStringArray("infos");

        if (infos == null) {
            for (int i = 0; i < reserved.length; i++)
                Log.e("dddd", reserved[i]);
            initOverlay(reserved[0], reserved[1], reserved[2], reserved[3]);

            if (Double.valueOf(reserved[6]) > 1000) {
                distance.setText("" + Double.valueOf(reserved[6]) / 1000);
                mOrkm.setText("千米");
            } else {
                distance.setText(reserved[6]);
                mOrkm.setText("米");
            }
            poi.setText(reserved[4]);
            String shortAddress = reserved[5];

            if (reserved[5].contains("自治区"))
                shortAddress = reserved[5].substring(reserved[5].indexOf("自治区") + 1, reserved[5].length());
            address.setText(" " + shortAddress);
            mSearch = RoutePlanSearch.newInstance();
            mSearch.setOnGetRoutePlanResultListener(this);
            PlanNode stNode = PlanNode.withLocation(new LatLng(Double.valueOf(reserved[0]), Double.valueOf(reserved[1])));
            PlanNode enNode = PlanNode.withLocation(new LatLng(Double.valueOf(reserved[2]), Double.valueOf(reserved[3])));
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));
        } else {
            for (int i = 0; i < infos.length; i++)
                Log.e("dddd", infos[i]);
            initOverlay(infos[0], infos[1], infos[2], infos[3]);
            if (Double.valueOf(infos[6]) > 1000) {
                distance.setText("" + Double.valueOf(infos[6]) / 1000);
                mOrkm.setText("千米");
            } else {
                distance.setText(infos[6]);
                mOrkm.setText("米");
            }
            poi.setText(infos[4]);
            String shortAddress = infos[5];
            if (infos[5].contains("自治区"))
                shortAddress = infos[5].substring(infos[5].indexOf("自治区") + 1, infos[5].length());
            address.setText(shortAddress);
            mSearch = RoutePlanSearch.newInstance();
            mSearch.setOnGetRoutePlanResultListener(this);
            PlanNode stNode = PlanNode.withLocation(new LatLng(Double.valueOf(infos[0]), Double.valueOf(infos[1])));
            PlanNode enNode = PlanNode.withLocation(new LatLng(Double.valueOf(infos[2]), Double.valueOf(infos[3])));
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));
        }


    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        getMyLocation();
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

    }

    public void getMyLocation() {
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    @Override
    public void completeAlarmSuccess() {
        finish();
        index = true;
    }

    public class MylocationListener implements BDLocationListener {
        private boolean isFirstIn = true;

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mCurrentX)
                    .accuracy(bdLocation.getRadius())
                    .latitude(mLatitude)
                    .longitude(mLongitude)
                    .build();
            mBaiduMap.setMyLocationData(data);
            MyLocationConfiguration configuration
                    = new MyLocationConfiguration(locationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(configuration);
            if (isFirstIn) {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.setMapStatus(msu);
                isFirstIn = false;
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if (!mlocationClient.isStarted()) {
            mlocationClient.start();
        }
        myOrientationListener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mlocationClient.stop();
        myOrientationListener.stop();
    }

    private void initLocation() {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mlocationClient = new LocationClient(this);
        mlistener = new MylocationListener();
        mlocationClient.registerLocationListener(mlistener);
        LocationClientOption mOption = new LocationClientOption();
        mOption.setCoorType("bd09ll");
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        int span = 1000;
        mOption.setScanSpan(span);
        mlocationClient.setLocOption(mOption);
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.drawable.move);
        myOrientationListener = new MyOrientationListener(context);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(ProcessActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }


    public void initOverlay(String sa, String so, String ea, String eo) {
        LatLng startPo = new LatLng(Double.valueOf(sa), Double.valueOf(so));
        LatLng endPo = new LatLng(Double.valueOf(ea), Double.valueOf(eo));

        MarkerOptions startMarkerOptions = new MarkerOptions().position(startPo).icon(start)
                .zIndex(9).draggable(true);
        MarkerOptions endMarkerOptions = new MarkerOptions().position(endPo).icon(end)
                .zIndex(9).draggable(true);

        mMarkerA = (Marker) (mBaiduMap.addOverlay(startMarkerOptions));
        mMarkerB = (Marker) (mBaiduMap.addOverlay(endMarkerOptions));


        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        ProcessActivity.this,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(ProcessActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;

            if (result.getRouteLines().size() > 1) {
                nowResultwalk = result;

                MyTransitDlg myTransitDlg = new MyTransitDlg(ProcessActivity.this,
                        result.getRouteLines(),
                        RouteLineAdapter.Type.WALKING_ROUTE);
                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                    public void onItemClick(int position) {
                        route = nowResultwalk.getRouteLines().get(position);
                        WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        routeOverlay = overlay;
                        overlay.setData(nowResultwalk.getRouteLines().get(position));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }

                });
                myTransitDlg.show();

            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    public class MyLocationListenner implements BDLocationListener {
        public double lati;
        public double longi;
        public String address;
        List<Poi> poi;

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(0)
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            lati = location.getLatitude();
            longi = location.getLongitude();
            address = location.getAddrStr();
            poi = location.getPoiList();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }


    }


    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.start);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.end);
            }
            return null;
        }
    }


    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }

    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List<? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
                type) {
            this(context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick(position);
                    dismiss();

                }
            });
        }

        public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Toast.makeText(getApplicationContext(), "请不要退出该页面，若接警完成，请点击“完成本次出警”返回。", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
