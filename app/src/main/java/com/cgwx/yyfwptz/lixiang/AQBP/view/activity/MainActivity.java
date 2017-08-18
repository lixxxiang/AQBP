package com.cgwx.yyfwptz.lixiang.AQBP.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.cgwx.yyfwptz.lixiang.AQBApplication;
import com.cgwx.yyfwptz.lixiang.AQBP.FragmentsAdapter;
import com.cgwx.yyfwptz.lixiang.AQBP.R;
import com.cgwx.yyfwptz.lixiang.AQBP.di.components.DaggerMainComponent;
import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.MainModule;
import com.cgwx.yyfwptz.lixiang.AQBP.model.api.ApiService;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.ActivityCollector;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.addAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.initStatus;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.modifyPoliceState;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.FMainPresenter;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.MainContract;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.MainPresenter;
import com.cgwx.yyfwptz.lixiang.AQBP.util.Constants;
import com.cgwx.yyfwptz.lixiang.AQBP.util.NetUtils;
import com.cgwx.yyfwptz.lixiang.AQBP.util.RestartAPPTool;
import com.cgwx.yyfwptz.lixiang.AQBP.view.fragment.MainFragment;
import com.cgwx.yyfwptz.lixiang.AQBP.view.fragment.MineFragment;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Inject
    MainPresenter presenter;

    private MainFragment mainFragment;
    private MineFragment mineFragment;
    public static String infos[];
    String pname;
    String pid;
    String ptel;
    Snackbar snackbar;
    Gson stategson;
    OkHttpClient changeStateClient;
    public static MainActivity mainActivity;
    private static final String LTAG = MainActivity.class.getSimpleName();
    public static String doOnce;
//    private LocationService locationService;
//    private LocationClientOption mOption;
//    private LocationService locService;
//    public static double la;
//    public static double lo;
//    public static Timer getAlarmtimer;
//    private MainActivity.getAlarmhandler gahandler;



    long exitTime = 0;
    public boolean isForeground = false;

    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d(LTAG, "action: " + s);
        }
    }

    private SDKReceiver mReceiver;


//    @SuppressLint("HandlerLeak")
//    private class getAlarmhandler extends Handler {
//
//        public getAlarmhandler(MainActivity mainActivity) {
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            locationService.stop();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            locationService.start();
//            System.out.println("running");
//
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_main);

//        locService =  ((AQBApplication)getApplication()).locationService;
//        mOption = new LocationClientOption();
//        mOption = locService.getDefaultLocationClientOption();
//        mOption.setOpenAutoNotifyMode(); //设置默认值
//        int setSensitivity = LocationClientOption.LOC_SENSITIVITY_HIGHT;
//        mOption.setOpenAutoNotifyMode(1000,1,setSensitivity);
//        locService.setLocationOption(mOption);
//        gahandler = new getAlarmhandler(this);

        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#484E6D"));
        ButterKnife.bind(this);
        mainActivity = this;

        DaggerMainComponent.builder().mainModule(new MainModule(this))
                .build()
                .inject(this);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }


        SharedPreferences sp = getSharedPreferences("Puser", MODE_PRIVATE);

        ptel = sp.getString("pTel", null);
        pid = sp.getString("pId", null);
        pname = sp.getString("pName", null);

//        ptel = "13051744716";
//        pid = "13051744716";
//        pname = "lixiang";

        infos = new String[3];
        infos[0] = pid;
        infos[1] = pname;
        infos[2] = ptel;

//        for (int i = 0; i < infos.length; i++) {
//            Log.e("dddd", infos[i]);
//        }
        notification();

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
        initViews();
    }


    private void initViews() {
        mainFragment = new MainFragment();
        mineFragment = new MineFragment();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(mineFragment);
        fragments.add(mainFragment);

        ArrayList<String> titles = new ArrayList<>();
        titles.add("我的");
        titles.add("                         安全宝");

        FragmentsAdapter mAdapter = new FragmentsAdapter(getSupportFragmentManager(), fragments, titles);
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager, false);
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ff9801"));

        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isForeground = true;
//        locationService = ((AQBApplication) getApplication()).locationService;
//        locationService.registerListener(mListener);
//        locationService.setLocationOption(locationService.getOption());
//        locationService.start();
//
//        getAlarmtimer = new Timer();
//        getAlarmtimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                Message message = new Message();
//                message.what = 1;
//                gahandler.sendMessage(message);
//                System.gc();
//            }
//        }, 0, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                if (LoginActivity.la != null){
                    LoginActivity.la.finish();
                }
                if (VCodeActivity.va != null){
                    VCodeActivity.va.finish();
                }
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isForeground = false;
//        locationService.unregisterListener(mListener);
//        locationService.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }



    public boolean isForeground() {
        return isForeground;
    }

    private void notification() {
        SharedPreferences setting = getSharedPreferences("isfirst", 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {//第一次
            setting.edit().putBoolean("FIRST", false).apply();
            doOnce = "1";
            snackbar = Snackbar.make(findViewById(R.id.main_content), "为了及时响应听警功能，请打开完全通知权限", Snackbar.LENGTH_INDEFINITE).setAction("我知道了", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                    snackbar = Snackbar.make(findViewById(R.id.main_content), "请自行安装新版百度地图客户端用于案件精确导航功能", Snackbar.LENGTH_INDEFINITE).setAction("我知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                            snackbar = Snackbar.make(findViewById(R.id.main_content), "左划查看个人信息", Snackbar.LENGTH_INDEFINITE).setAction("我知道了", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.setActionTextColor(Color.parseColor("#ff9801"));
                            snackbar.show();
                        }
                    });
                    snackbar.setActionTextColor(Color.parseColor("#ff9801"));
                    snackbar.show();

                }
            });
            snackbar.setActionTextColor(Color.parseColor("#ff9801"));
            snackbar.show();


            Retrofit retrofit = NetUtils.getRetrofit();
            ApiService apiService = retrofit.create(ApiService.class);
            retrofit2.Call<initStatus> call = apiService.initStatus(pid);
            call.enqueue(new retrofit2.Callback<initStatus>() {

                @Override
                public void onResponse(retrofit2.Call<initStatus> call, retrofit2.Response<initStatus> response) {
                    if (response.body().getMeta().equals("true")) {
                        System.out.println("in?");
                        switch (response.body().getState()) {
                            case "1":
                                forceOut();
                                break;
                            case "2":
                                forceOut();
                                break;
                            case "4":
                                break;
                            default:
                                System.out.println("wrong state！");
                                break;
                        }
                    }else{
                        System.out.println("init status return failed");
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<initStatus> call, Throwable throwable) {
                    System.out.println("init status failed");
                }
            });


//            AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
//            localBuilder.setTitle("请注意");
//            localBuilder.setIcon(R.mipmap.ic_launcher);
//            localBuilder.setMessage("您的手机设备暂无法取得精准定位，请点击重启软件");
//            localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener()
//            {
//                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
//                {
//                    if (LoginActivity.la != null){
//                        LoginActivity.la.finish();
//                    }
//                    if (VCodeActivity.va != null){
//                        VCodeActivity.va.finish();
//                    }
//                    doRestart(MainActivity.this);
//                }
//            });
//
//            /***
//             * 设置点击返回键不会消失
//             * */
//            localBuilder.setCancelable(false).create();
//
//            localBuilder.show();

        }
    }
    private void forceOut(){
        stategson = new Gson();
        changeStateClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        RequestBody requestBodyPost = new FormBody.Builder()
                .add("state", "2")
                .add("policeId", pid)
                .build();
        Request requestPost = new Request.Builder()
                .url(Constants.prefix +"mobile/police/modifyPosition/")
                .post(requestBodyPost)
                .build();
        changeStateClient.newCall(requestPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("state:", "2,出警");
                        FMainPresenter.modifyPoliceState3("1", pid);
                        if (MainFragment.getAlarmtimer != null) {
                            MainFragment.getAlarmtimer.cancel();
                            MainFragment.getAlarmtimer.purge();
                        }
                        MainFragment.appear.setVisibility(View.INVISIBLE);
                        MainFragment.done.setVisibility(View.INVISIBLE);
                        MainFragment.listenPolice.setText("听警中");
                        MainFragment.listenPolice.setVisibility(View.INVISIBLE);
                        MainFragment.outpolice.setVisibility(View.VISIBLE);
                        MainFragment.outpolice.setText("出警");
                        MainFragment.outpolice.setEnabled(true);
                        MainFragment.quit.setVisibility(View.INVISIBLE);
                    }
                });
            }

        });
    }

    public void doRestart(Context c) {
        if (Build.VERSION.SDK_INT < 23){
            RestartAPPTool.restartAPP(MainActivity.this);
        }
        else {
            try {
                if (c != null) {
                    PackageManager pm = c.getPackageManager();
                    if (pm != null) {
                        Intent mStartActivity = pm.getLaunchIntentForPackage(
                                c.getPackageName()
                        );
                        if (mStartActivity != null) {
                            mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            int mPendingIntentId = 223344;
                            PendingIntent mPendingIntent = PendingIntent
                                    .getActivity(c, mPendingIntentId, mStartActivity,
                                            PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            finish();
                            System.exit(0);
                        } else {
                            Log.e("TAG", "Was not able to restart application, mStartActivity null");
                        }
                    } else {
                        Log.e("TAG", "Was not able to restart application, PM null");
                    }
                } else {
                    Log.e("TAG", "Was not able to restart application, Context null");
                }
            } catch (Exception ex) {
                Log.e("TAG", "Was not able to restart application");
            }
        }

    }

    public void restart(){
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
//        localBuilder.setTitle("请注意");
        localBuilder.setMessage("抱歉，您的手机设备暂无法取得精准定位，点击确定将关闭软件，请重启尝试重新定位。");
        localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                if (LoginActivity.la != null){
                    LoginActivity.la.finish();
                }
                if (VCodeActivity.va != null){
                    VCodeActivity.va.finish();
                }
//                doRestart(MainActivity.this)
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        /***
         * 设置点击返回键不会消失
         * */
        localBuilder.setCancelable(false).create();

        localBuilder.show();
    }
}
