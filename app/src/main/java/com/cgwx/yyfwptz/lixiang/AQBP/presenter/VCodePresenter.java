package com.cgwx.yyfwptz.lixiang.AQBP.presenter;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.cgwx.yyfwptz.lixiang.AQBP.model.api.ApiService;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.checkMessage;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.sendMessage;
import com.cgwx.yyfwptz.lixiang.AQBP.util.NetUtils;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.LoginActivity;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.ProcessActivity;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.VCodeActivity;
import com.cgwx.yyfwptz.lixiang.AQBP.view.fragment.MineFragment;

import javax.inject.Inject;

import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yyfwptz on 2017/8/8.
 */

public class VCodePresenter implements VCodeContract.Presenter{
    private VCodeContract.View view;
    private String pTel;
    private String pVcode;
    private String pId;
    private String pName;
    private ApiService apiService;
    private Retrofit retrofit;

    @Inject
    public VCodePresenter(VCodeContract.View view) {
        this.view = view;
    }

    @Override
    public void test() {
        Log.e("TAG", "vcode test");

    }

    @Override
    public void resendMessage(String tel) {
        pTel = tel;
        /**
         * 提前显示
         */
        view.recount();
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<sendMessage> call = apiService.sendMessage(pTel);
        call.enqueue(new retrofit2.Callback<sendMessage>() {

            @Override
            public void onResponse(retrofit2.Call<sendMessage> call, retrofit2.Response<sendMessage> response) {
                System.out.println("vcode return：" + response.body().getMeta());
                if (response.body().getMeta().equals("success")) {
                }
            }

            @Override
            public void onFailure(retrofit2.Call<sendMessage> call, Throwable throwable) {
                Toast.makeText(LoginActivity.la, "验证码获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void checkMessage(String tel, String vcode) {
        pTel = tel;
        pVcode = vcode;
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<checkMessage> call = apiService.checkMessage(pTel, pVcode);
        call.enqueue(new retrofit2.Callback<checkMessage>() {

            @Override
            public void onResponse(retrofit2.Call<checkMessage> call, retrofit2.Response<checkMessage> response) {
                if (response.body().getMeta().equals("success")) {
                    pName = response.body().getPoliceInfo().getName();
                    pId = response.body().getPoliceInfo().getPoliceId();

                    Log.e("PPPUSER","sdfsdfsdfs");
                    SharedPreferences sp = VCodeActivity.va.getSharedPreferences("Puser", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("pTel", pTel);
                    editor.putString("pId", pId);
                    editor.putString("pName", pName);
                    editor.commit();

                    MineFragment.quitting = false;
                    ProcessActivity.index = false;

                    view.checkVcodeSuccess(pId, pName, pTel);
                } else {
                    Toast.makeText(VCodeActivity.va, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<checkMessage> call, Throwable throwable) {
            }
        });
    }
}
