package com.cgwx.yyfwptz.lixiang.AQBP.presenter;

import android.util.Log;
import android.widget.Toast;
import com.cgwx.yyfwptz.lixiang.AQBP.model.api.ApiService;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.isTelAvailable;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.sendMessage;
import com.cgwx.yyfwptz.lixiang.AQBP.util.Constants;
import com.cgwx.yyfwptz.lixiang.AQBP.util.NetUtils;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.LoginActivity;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by yyfwptz on 2017/8/8.
 */

public class LoginPresenter implements LoginContract.Presenter{
    private LoginContract.View view;
    private String userTel;
    private ApiService apiService;
    private Retrofit retrofit;

    @Inject
    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void test() {
        Log.e("DAGGET2_TAG", "test");
    }

    @Override
    public void sendMessage(String tel) {
        userTel = tel;
        if (Constants.isMobileNO(userTel)) {
            /**
             * 先跳转
             */
            isTelephoneAvailable();
        } else {
            Toast.makeText(LoginActivity.la, "手机号不正确", Toast.LENGTH_SHORT).show();
        }
    }

    private void isTelephoneAvailable(){
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<isTelAvailable> call = apiService.isTelAvailable(userTel);
        call.enqueue(new retrofit2.Callback<isTelAvailable>() {

            @Override
            public void onResponse(retrofit2.Call<isTelAvailable> call, retrofit2.Response<isTelAvailable> response) {
                if (response.body() != null){
                    if (response.body().getMeta().equals("success")){
                        view.getVCodeSuccess(userTel);
                        retrofit = NetUtils.getRetrofit();
                        apiService = retrofit.create(ApiService.class);
                        retrofit2.Call<sendMessage> call2 = apiService.sendMessage(userTel);
                        call2.enqueue(new retrofit2.Callback<sendMessage>() {

                            @Override
                            public void onResponse(retrofit2.Call<sendMessage> call2, retrofit2.Response<sendMessage> response) {
                                System.out.println("vcode return：" + response.body().getMeta());
                                if (response.body().getMeta().equals("success")){

                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<sendMessage> call2, Throwable throwable) {
                                Toast.makeText(LoginActivity.la, "验证码获取失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(LoginActivity.la, "手机号不正确", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.la, "请检查网络", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<isTelAvailable> call, Throwable throwable) {
            }
        });
    }
}
