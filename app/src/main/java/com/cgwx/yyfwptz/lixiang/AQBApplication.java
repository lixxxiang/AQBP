package com.cgwx.yyfwptz.lixiang;

import android.app.Application;

//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;

public class AQBApplication extends Application {
//    public LocationService locationService;

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
//        locationService = new LocationService(getApplicationContext());
        SDKInitializer.initialize(this);
    }

}