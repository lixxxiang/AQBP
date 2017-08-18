package com.cgwx.yyfwptz.lixiang.AQBP.presenter;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by yyfwptz on 2017/8/9.
 */

public class MainPresenter implements MainContract.Presenter{
    private MainContract.View view;

    @Inject
    public MainPresenter(MainContract.View view){
        this.view = view;
    }

    @Override
    public void test() {
        Log.e("Main pre","ok");
    }
}
