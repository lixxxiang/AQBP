
package com.cgwx.yyfwptz.lixiang.AQBP.view.fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.cgwx.yyfwptz.lixiang.AQBP.R;
import com.cgwx.yyfwptz.lixiang.AQBP.di.components.DaggerFMainComponent;
import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.FMainModule;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.ActivityCollector;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.reserved;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.FMainContract;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.FMainPresenter;
import com.cgwx.yyfwptz.lixiang.AQBP.util.Constants;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.LoginActivity;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.MainActivity;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.ProcessActivity;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.VCodeActivity;

import static com.cgwx.yyfwptz.lixiang.AQBP.view.activity.ProcessActivity.index;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MainFragment extends Fragment implements FMainContract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public static Button outpolice;
    public static View appear;
    public static View done;
    public static Button listenPolice;
    public static Button quit;
    @BindView(R.id.close)
    Button close;
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.alarmLocation)
    TextView alarmlocation;
    @BindView(R.id.alarmDistance)
    TextView alarmdistance;
    @BindView(R.id.poiinfoo)
    TextView poiInfo;
    @BindView(R.id.dpoi)
    TextView dpoi;
    @BindView(R.id.dd)
    TextView ddis;
    @BindView(R.id.dadr)
    TextView dadd;
    @BindView(R.id.accept)
    Button acceptP;
    @BindView(R.id.decline)
    Button declineP;

    @Inject
    FMainPresenter presenter;

    private com.cgwx.yyfwptz.lixiang.AQBP.model.entity.alarmInfo alarmInfo;
    protected View mRootView;
    private String pid;
    private LocationClient mLocClient;
    private UiSettings mUiSettings;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private BaiduMap mBaiduMap;
    boolean isFirstLoc = true;
    private OnFragmentInteractionListener mListener;
    private SimpleDateFormat formatter;
    private Date curDate;
    public static Timer getAlarmtimer;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    public static Timer countdownTimer;
    public static Timer timer;

    private int count10sec = 11;
    public static MainFragment mainFragment;
    public double distance;
    private getAlarmhandler gahandler;
    private int countDown = 0;
    private OkHttpClient modifyPositionClient;
    public static final String POST_URL_MODIFYPOSITION = Constants.prefix + "mobile/police/modifyPosition/";
    private boolean isQuitPolice;

    @SuppressLint("HandlerLeak")
    private class getAlarmhandler extends Handler {

        public getAlarmhandler(MainFragment mainFragment) {
        }

        @Override
        public void handleMessage(Message msg) {
            countDown++;
            String time = formatter.format(curDate);

            if (!outpolice.isEnabled())
                outpolice.setEnabled(true);
            modifyPositionClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            RequestBody requestBodyPost2 = new FormBody.Builder()
                    .add("latitude", "" + myListener.lati)
                    .add("longitude", "" + myListener.longi)
                    .add("policeId", pid)
                    .add("time", time)
                    .build();
            Request requestPost2 = new Request.Builder()
                    .url(POST_URL_MODIFYPOSITION)
                    .post(requestBodyPost2)
                    .build();
            modifyPositionClient.newCall(requestPost2).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String string = response.body().string();
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            });

            if (MineFragment.quitting) {
                if (getAlarmtimer != null) {
                    getAlarmtimer.cancel();
                    getAlarmtimer.purge();
                }
                presenter.modifyPoliceState2("1", pid);
            } else {
                presenter.getAlarm(pid);
            }
            if (getAlarmtimer != null) {

            } else
                System.out.println("getAlarmTimer null");
        }
    }


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e("start", "start");
        if (index && !isQuitPolice) {
            Log.e("in", "" + index + "  " + isQuitPolice);
            listenPolice.setVisibility(View.VISIBLE);
            listenPolice.setText("听警中");
            quit.setVisibility(View.VISIBLE);
            outpolice.setVisibility(View.INVISIBLE);

            if (countdownTimer != null) {
                countdownTimer.purge();
                countdownTimer.cancel();
            }
            System.out.println("countdownTimer start" + countdownTimer);
            appear.setVisibility(View.INVISIBLE);
            done.setVisibility(View.INVISIBLE);
            if (getAlarmtimer != null) {
                getAlarmtimer.purge();
                getAlarmtimer.cancel();
            }
            getAlarmtimer = new Timer();
            System.out.println("getAlarmTimer" + getAlarmtimer);
            getAlarmtimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Message message = new Message();
                    message.what = 1;
                    gahandler.sendMessage(message);
                    System.gc();
                }
            }, 0, 1000);
        } else {
            System.out.println("getAlarmTimer" + getAlarmtimer);
            Log.e("not in", "" + index + "  " + isQuitPolice);
            index = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        DaggerFMainComponent.builder().fMainModule(new FMainModule(this))
                .build()
                .inject(this);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done.setVisibility(View.INVISIBLE);
            }
        });
        if (MainActivity.infos[0] != null)
            pid = MainActivity.infos[0];
        else
            System.out.println("empty pid");
        outpolice = (Button) getActivity().findViewById(R.id.outPolice);
        appear = getActivity().findViewById(R.id.appear);
        done = getActivity().findViewById(R.id.done);
        listenPolice = (Button) getActivity().findViewById(R.id.lisPolice);
        quit = (Button) getActivity().findViewById(R.id.quit);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.location);
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        mainFragment = this;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        curDate = new Date(System.currentTimeMillis());
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setScrollGesturesEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setAddrType("all");
        option.setIsNeedLocationPoiList(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
        gahandler = new getAlarmhandler(this);
        outpolice.setText("请等待");
        outpolice.setEnabled(false);
        Toast.makeText(getActivity(), "正在恢复您的状态，请稍后", Toast.LENGTH_LONG).show();
        mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);


        /**
         * 状态判断
         */
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (pid != null) {
                    presenter.initStatus(pid);
                } else
                    System.out.println("pid null");
            }
        }, 2000);


        /**
         * 点击出警
         */
        outpolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isQuitPolice = false;
                index = true;
                countdownTimer = new Timer();
                getAlarmtimer = new Timer();
                timer = new Timer();
                outpolice.setVisibility(View.INVISIBLE);
                listenPolice.setVisibility(View.VISIBLE);
                quit.setVisibility(View.VISIBLE);
                outpolice.setEnabled(true);

                if (pid != null)
                    presenter.modifyPoliceState("2", pid);
                else
                    System.out.println("outpolice pid null");
                getAlarmtimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Message message = new Message();
                        message.what = 1;
                        gahandler.sendMessage(message);
                        System.gc();
                    }
                }, 0, 1000);
                if (MainActivity.doOnce!= null && MainActivity.doOnce == "1"){
                    MainActivity.mainActivity.restart();
                    MainActivity.doOnce = "0";
                }
            }
        });

        /**
         * 点击收工
         */
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAlarmtimer != null) {
                    getAlarmtimer.cancel();
                    getAlarmtimer.purge();
                }

                if (countdownTimer != null) {
                    countdownTimer.cancel();
                    countdownTimer.purge();
                }
                appear.setVisibility(View.INVISIBLE);
                done.setVisibility(View.INVISIBLE);
                listenPolice.setText("听警中");
                listenPolice.setVisibility(View.INVISIBLE);
                outpolice.setVisibility(View.VISIBLE);
                outpolice.setText("出警");
                outpolice.setEnabled(true);
                quit.setVisibility(View.INVISIBLE);

                if (pid != null)
                    presenter.modifyPoliceState("1", pid);
                else
                    System.out.println("quit pid null");

//                restartApplication();
                index = false;

                isQuitPolice = true;
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void restartApplication() {
        final Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class MyLocationListenner implements BDLocationListener {
        public double lati;
        public double longi;
        public String address;
        List<Poi> poi;

        @Override
        public void onReceiveLocation(BDLocation location) {
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

    public void getAlarmRoll() {
//        countdownTimer = new Timer();

    }


    @Override
    public void stateIsListenPolice() {
//        countdownTimer = new Timer();
        getAlarmtimer = new Timer();

        outpolice.setVisibility(View.INVISIBLE);
        listenPolice.setVisibility(View.VISIBLE);
        quit.setVisibility(View.VISIBLE);
        getAlarmtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                gahandler.sendMessage(message);
                System.gc();
            }
        }, 0, 1000);
        index = true;
        isQuitPolice = false;
    }


    @Override
    public void stateIsOutPolice(String str1, final reserved infos) {
        if (infos != null) {
            BNRoutePlanNode sNode = new BNRoutePlanNode(myListener.longi, myListener.lati, "", null, BNRoutePlanNode.CoordinateType.GCJ02);
            BNRoutePlanNode eNode = new BNRoutePlanNode(Double.valueOf(infos.getLongitude()), Double.valueOf(infos.getLatitude()), "", null, BNRoutePlanNode.CoordinateType.GCJ02);
            if (sNode.getLatitude() != 0.0 && sNode.getLatitude() != 0.0 && eNode.getLatitude() != 0.0 && eNode.getLongitude() != 0.0)
                searchRoute(sNode, eNode);
            else
                System.out.println("sNode or eNode is wrong");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(getActivity(), ProcessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("reserved", new String[]{
                            String.valueOf(myListener.lati),
                            String.valueOf(myListener.longi),
                            infos.getLatitude(),
                            infos.getLongitude(),
                            infos.getPoi(),
                            infos.getAddress(),
                            "" + distance,
                            infos.getAlarmId(),
                            pid,
                            infos.getCivilianTelephone()
                    });
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }, 2000);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void gotAlarm(com.cgwx.yyfwptz.lixiang.AQBP.model.entity.alarmInfo ai) {
        if (getAlarmtimer != null) {
            getAlarmtimer.cancel();
            getAlarmtimer.purge();
        }

        alarmInfo = ai;
        BNRoutePlanNode sNode = new BNRoutePlanNode(myListener.longi, myListener.lati, "", null, BNRoutePlanNode.CoordinateType.GCJ02);      //新建两个坐标点
        BNRoutePlanNode eNode = new BNRoutePlanNode(Double.valueOf(alarmInfo.getLongitude()), Double.valueOf(alarmInfo.getLatitude()), "", null, BNRoutePlanNode.CoordinateType.GCJ02);
        searchRoute(sNode, eNode);
        countdownTimer = new Timer();

        if (ActivityCollector.hasActivityInForeground()) {
            isQuitPolice = false;
            countdownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void run() {
                            count10sec--;
                            Log.e("foreground", "" + count10sec + ActivityCollector.hasActivityInForeground());
                            quit.setVisibility(View.INVISIBLE);
                            listenPolice.setText("" + count10sec);

                            /**
                             * 倒计时结束 相当于拒接
                             */
                            if (count10sec < 1) {
                                if (countdownTimer != null) {
                                    countdownTimer.cancel();
                                    countdownTimer.purge();
                                }
                                quit.setVisibility(View.VISIBLE);
                                if (appear.getVisibility() == 0) {
                                    appear.setVisibility(View.INVISIBLE);
                                }
                                listenPolice.setText("听警中");
                                presenter.refuseAlarm(alarmInfo.getAlarmId(), pid);
                            }
                        }
                    });
                }
            }, 0, 1000);


            alarmlocation.setText(alarmInfo.getPoi());
            String shortAddress = alarmInfo.getAddress();
            if (alarmInfo.getAddress().contains("自治区")) {
                shortAddress = alarmInfo.getAddress().substring(alarmInfo.getAddress().indexOf("自治区") + 1, alarmInfo.getAddress().length());
            }
            poiInfo.setText(shortAddress);
            dadd.setText("详情：" + shortAddress);
            dpoi.setText("位置： " + alarmInfo.getPoi());

        } else {
            isQuitPolice = false;
            alarmInfo = ai;
            String shortAddress = alarmInfo.getAddress();
            if (alarmInfo.getAddress().contains("自治区")) {
                shortAddress = alarmInfo.getAddress().substring(alarmInfo.getAddress().indexOf("自治区") + 1, alarmInfo.getAddress().length());
            }
            Context application = getActivity().getApplicationContext();
            Intent resultIntent = new Intent(application, MainActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(application, 0, resultIntent, 0);
            mNotification = new NotificationCompat.Builder(MainActivity.mainActivity)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("您有新的警情！")
                    .setContentText("案件发生在：" + alarmInfo.getPoi() + "，地址是：" + shortAddress)
                    .setContentIntent(resultPendingIntent)
                    .build();
            mNotificationManager.notify(0, mNotification);
            Log.e("background", "" + count10sec);

            countdownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {      // UI thread
                        @Override
                        public void run() {
                            count10sec--;
                            /**
                             * 接到警 切回app
                             */
                            if(!ActivityCollector.hasActivityInForeground()){
                                if (count10sec < 1) {//十秒钟内没切回前台
                                    mNotification = new NotificationCompat.Builder(MainActivity.mainActivity)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("该案件已被系统拒接")
                                            .build();
                                    mNotificationManager.notify(0, mNotification);
                                    if (countdownTimer != null) {
                                        countdownTimer.cancel();
                                        countdownTimer.purge();
                                    }
                                    System.out.println("countdownTimer gotalarm still back" + countdownTimer);

                                    presenter.refuseAlarm(alarmInfo.getAlarmId(), pid);
                                }
                            }else{

                            }
                        }
                    });
                }
            }, 0, 1000);
        }

        done.setVisibility(View.INVISIBLE);
        appear.setVisibility(View.VISIBLE);

        acceptP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("356", alarmInfo.getAlarmId() + pid);
                presenter.acceptAlarm(alarmInfo.getAlarmId(), pid);
            }
        });

        declineP.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (appear.getVisibility() == 0) {
                    appear.setVisibility(View.INVISIBLE);
                }
                quit.setVisibility(View.VISIBLE);
                listenPolice.setText("听警中");
                ///crash
                if (countdownTimer != null) {
                    countdownTimer.cancel();
                    countdownTimer.purge();
                }
                presenter.refuseAlarm(alarmInfo.getAlarmId(), pid);
            }
        });

    }

    @Override
    public void alarmRefused() {
        appear.setVisibility(View.INVISIBLE);
        done.setVisibility(View.VISIBLE);
        getAlarmtimer = new Timer();
        getAlarmtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                gahandler.sendMessage(message);
                System.gc();
            }
        }, 0, 1000);
        count10sec = 11;
    }


    @Override
    public void stateIsStop() {
        outpolice.setEnabled(true);
        outpolice.setText("出警");
        index = false;
    }

    @Override
    public void alarmAccpted(String str1) {
        if (getAlarmtimer != null) {
            getAlarmtimer.cancel();
            getAlarmtimer.purge();
        }

        count10sec = 11;
        if (countdownTimer != null) {
            countdownTimer.cancel();
            countdownTimer.purge();
        }

        Intent intent = new Intent(getActivity(), ProcessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("infos", new String[]{
                String.valueOf(myListener.lati),
                String.valueOf(myListener.longi),
                alarmInfo.getLatitude(),
                alarmInfo.getLongitude(),
                alarmInfo.getPoi(),
                alarmInfo.getAddress(),
                "" + distance,
                alarmInfo.getAlarmId(),
                pid,
                str1
        });
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void whatTheFuck() {
        listenPolice.setVisibility(View.INVISIBLE);
        quit.setVisibility(View.INVISIBLE);
        outpolice.setVisibility(View.VISIBLE);
    }

    public void searchRoute(BNRoutePlanNode sNode, BNRoutePlanNode eNode) {

        RoutePlanSearch search = RoutePlanSearch.newInstance();
        DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption();
        PlanNode startPlanNode = PlanNode.withLocation(new LatLng(sNode.getLatitude(), sNode.getLongitude()));
        PlanNode endPlanNode = PlanNode.withLocation(new LatLng(eNode.getLatitude(), eNode.getLongitude()));
        drivingRoutePlanOption.from(startPlanNode);
        drivingRoutePlanOption.to(endPlanNode);
        search.drivingSearch(drivingRoutePlanOption);


        search.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                if (walkingRouteResult.getRouteLines() == null) return;
                int duration = walkingRouteResult.getRouteLines().get(0).getDuration();
                Toast.makeText(getActivity(), duration + "米", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {     //驾车路线
                if (drivingRouteResult.getRouteLines() == null) {
                    if (countdownTimer != null){
                        countdownTimer.cancel();
                    }
                    if(getAlarmtimer != null){
                        getAlarmtimer.cancel();
                    }


                    return;
                }
                double duration = drivingRouteResult.getRouteLines().get(0).getDistance();
                distance = duration;
                if (duration > 1000) {
                    alarmdistance.setText(duration / 1000 + "千米");
                    ddis.setText("距离： " + duration / 1000 + "公里");
                } else {
                    alarmdistance.setText(duration + "米");
                    ddis.setText("距离： " + duration + "米");
                }

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocClient != null) {
            mLocClient.stop();
        }
        index = false;
    }
}
