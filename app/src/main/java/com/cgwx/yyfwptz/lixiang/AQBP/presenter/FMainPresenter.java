package com.cgwx.yyfwptz.lixiang.AQBP.presenter;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cgwx.yyfwptz.lixiang.AQBP.R;
import com.cgwx.yyfwptz.lixiang.AQBP.model.api.ApiService;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.acceptAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.alarmInfo;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.getAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.initStatus;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.modifyPoliceState;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.modifyPosition;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.refuseAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.util.NetUtils;
import com.cgwx.yyfwptz.lixiang.AQBP.view.fragment.MainFragment;
import javax.inject.Inject;
import retrofit2.Retrofit;
import static com.cgwx.yyfwptz.lixiang.AQBP.util.Constants.playVoice;

/**
 * Created by yyfwptz on 2017/8/9.
 */

public class FMainPresenter implements FMainContract.Presenter {
    private FMainContract.View view;
    private Retrofit retrofit;
    private ApiService apiService;
    private alarmInfo ai;
    private Snackbar snackbar;
    private boolean show = false;


    @Inject
    public FMainPresenter(FMainContract.View view) {
        this.view = view;
    }

    @Override
    public void test() {
        Log.e("fragment", "ok");
    }

    @Override
    public void modifyPosition(double str1, final double str2, final String str3, String str4) {
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<modifyPosition> call = apiService.modifyPosition(str1, str2, str3, str4);
        call.enqueue(new retrofit2.Callback<modifyPosition>() {

            @Override
            public void onResponse(retrofit2.Call<modifyPosition> call, retrofit2.Response<modifyPosition> response) {
                if (response.body().getMeta().equals("success")) {
                    System.out.println("modifyPosition succeed");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<modifyPosition> call, Throwable throwable) {
                System.out.println("modifyPosition failed");
                Toast.makeText(MainFragment.mainFragment.getActivity(), "modifyPosition failed", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void modifyPoliceState(String str1, String str2) {
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<modifyPoliceState> call = apiService.modifyPoliceState(str1, str2);
        call.enqueue(new retrofit2.Callback<modifyPoliceState>() {

            @Override
            public void onResponse(retrofit2.Call<modifyPoliceState> call, retrofit2.Response<modifyPoliceState> response) {
                if (response.body().getMeta().equals("success")) {
                }
            }

            @Override
            public void onFailure(retrofit2.Call<modifyPoliceState> call, Throwable throwable) {
                System.out.println("modifyPoliceState failed");
//                Toast.makeText(MainFragment.mainFragment.getActivity(), "modifyPoliceState failed", Toast.LENGTH_SHORT).show();
                cannotAccessToInternet();

            }
        });
    }

    public static void modifyPoliceState3(String str1, String str2) {
        Retrofit retrofit = NetUtils.getRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);
        retrofit2.Call<modifyPoliceState> call = apiService.modifyPoliceState(str1, str2);
        call.enqueue(new retrofit2.Callback<modifyPoliceState>() {

            @Override
            public void onResponse(retrofit2.Call<modifyPoliceState> call, retrofit2.Response<modifyPoliceState> response) {
                if (response.body().getMeta().equals("success")) {
                }
            }

            @Override
            public void onFailure(retrofit2.Call<modifyPoliceState> call, Throwable throwable) {
                System.out.println("modifyPoliceState failed");
//                Toast.makeText(MainFragment.mainFragment.getActivity(), "modifyPoliceState failed", Toast.LENGTH_SHORT).show();
//                cannotAccessToInternet();

            }
        });
    }

    @Override
    public void modifyPoliceState2(String str1, String str2) {
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<modifyPoliceState> call = apiService.modifyPoliceState(str1, str2);
        call.enqueue(new retrofit2.Callback<modifyPoliceState>() {

            @Override
            public void onResponse(retrofit2.Call<modifyPoliceState> call, retrofit2.Response<modifyPoliceState> response) {
                if (response.body().getMeta().equals("success")) {
                    view.whatTheFuck();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<modifyPoliceState> call, Throwable throwable) {
                System.out.println("modifyPoliceState2 failed");

            }
        });
    }

    @Override
    public void initStatus(final String str1) {
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<initStatus> call = apiService.initStatus(str1);
        call.enqueue(new retrofit2.Callback<initStatus>() {

            @Override
            public void onResponse(retrofit2.Call<initStatus> call, retrofit2.Response<initStatus> response) {
                if (response.body().getMeta().equals("true")) {
                    switch (response.body().getState()) {
                        case "1":
                            view.stateIsListenPolice();
                            break;
                        case "2":
                            playVoice(MainFragment.mainFragment.getContext());
                            view.stateIsOutPolice(response.body().getState(), response.body().getReservedAlarmInfo());
                            break;
                        case "4":
                            view.stateIsStop();
                            break;
                        default:
                            System.out.println("wrong state！");
                            break;
                    }
                }else{
                    System.out.println("init status return failed");
                    initStatus(str1);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<initStatus> call, Throwable throwable) {
                System.out.println("init status failed");
//                Toast.makeText(MainFragment.mainFragment.getActivity(), "init status failed, reconnecting", Toast.LENGTH_SHORT).show();
                if (!show){
                    cannotAccessToInternet();
                    show = true;
                }
                initStatus(str1);
            }
        });
    }

    @Override
    public void getAlarm(String str1) {

        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<getAlarm> call = apiService.getAlarm(str1);
        call.enqueue(new retrofit2.Callback<getAlarm>() {

            @Override
            public void onResponse(retrofit2.Call<getAlarm> call, retrofit2.Response<getAlarm> response) {
                if (response.body().getMeta().equals("success")) {
                    playVoice(MainFragment.mainFragment.getContext());
                    ai = response.body().getAlarmInfo();
                    view.gotAlarm(ai);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<getAlarm> call, Throwable throwable) {
                System.out.println("get Alarm failed");
                if (!show){
                    cannotAccessToInternet();
                    show = true;
                }

            }
        });
    }

    @Override
    public void refuseAlarm(String str1, String str2) {
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<refuseAlarm> call = apiService.refuseAlarm(str1, str2);
        call.enqueue(new retrofit2.Callback<refuseAlarm>() {

            @Override
            public void onResponse(retrofit2.Call<refuseAlarm> call, retrofit2.Response<refuseAlarm> response) {
                if (response.body().getMeta().equals("success"))
                    view.alarmRefused();
                else{
                    System.out.println("refuse failed");
                    view.alarmRefused();
                }


            }

            @Override
            public void onFailure(retrofit2.Call<refuseAlarm> call, Throwable throwable) {
                System.out.println("refuse failed, check network");
//                Toast.makeText(MainFragment.mainFragment.getActivity(), "refuse failed", Toast.LENGTH_SHORT).show();
                cannotAccessToInternet();
            }
        });
    }


    @Override
    public void acceptAlarm(String str1, String str2) {
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<acceptAlarm> call = apiService.acceptAlarm(str1, str2);
        call.enqueue(new retrofit2.Callback<acceptAlarm>() {

            @Override
            public void onResponse(retrofit2.Call<acceptAlarm> call, retrofit2.Response<acceptAlarm> response) {
                if (response.body().getMeta().equals("success")) {
                    view.alarmAccpted(response.body().getCivilianTel());
                }else{

                }
            }

            @Override
            public void onFailure(retrofit2.Call<acceptAlarm> call, Throwable throwable) {
                System.out.println("accept alarm failed");
//                Toast.makeText(MainFragment.mainFragment.getActivity(), "accept alarm failed", Toast.LENGTH_SHORT).show();
                cannotAccessToInternet();
            }
        });
    }

    private void cannotAccessToInternet(){
        snackbar = Snackbar.make(MainFragment.mainFragment.getActivity().findViewById(R.id.main_content), "请检查您的网络连接", Snackbar.LENGTH_INDEFINITE).setAction("我知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(Color.parseColor("#ff9801"));
        snackbar.show();
        show = true;
    }
}
