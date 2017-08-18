package com.cgwx.yyfwptz.lixiang.AQBP.presenter;

import com.cgwx.yyfwptz.lixiang.AQBP.model.api.ApiService;

import javax.inject.Inject;

import retrofit2.Retrofit;

import static com.cgwx.yyfwptz.lixiang.AQBP.util.Constants.playVoice;

/**
 * Created by yyfwptz on 2017/8/9.
 */

public class FMinePresenter implements FMineContract.Presenter {
    private FMineContract.View view;
    private Retrofit retrofit;
    private ApiService apiService;


    @Inject
    public FMinePresenter(FMineContract.View view) {
        this.view = view;
    }
}
