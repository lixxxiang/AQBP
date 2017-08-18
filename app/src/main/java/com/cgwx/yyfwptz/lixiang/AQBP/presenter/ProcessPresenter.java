package com.cgwx.yyfwptz.lixiang.AQBP.presenter;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.cgwx.yyfwptz.lixiang.AQBP.R;
import com.cgwx.yyfwptz.lixiang.AQBP.model.api.ApiService;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.completeAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.getAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.util.NetUtils;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.ProcessActivity;
import com.cgwx.yyfwptz.lixiang.AQBP.view.fragment.MainFragment;

import javax.inject.Inject;

import retrofit2.Retrofit;


/**
 * Created by yyfwptz on 2017/8/9.
 */

public class ProcessPresenter implements ProcessContract.Presenter{
    private ProcessContract.View view;
    private Retrofit retrofit;
    private ApiService apiService;
    private Snackbar snackbar;
    @Inject
    public ProcessPresenter(ProcessContract.View view){
        this.view = view;
    }

    @Override
    public void completeAlarm(String str1, String str2) {
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<completeAlarm> call = apiService.completeAlarm(str1, str2);
        call.enqueue(new retrofit2.Callback<completeAlarm>() {

            @Override
            public void onResponse(retrofit2.Call<completeAlarm> call, retrofit2.Response<completeAlarm> response) {
                if (response.body().getMeta().equals("success")) {
                    view.completeAlarmSuccess();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<completeAlarm> call, Throwable throwable) {
                cannotAccessToInternet();
                Toast.makeText(ProcessActivity.pa, "网络错误，请检查您的网络连接", Toast.LENGTH_SHORT).show();
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
    }
}
