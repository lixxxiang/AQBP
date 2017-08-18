package com.cgwx.yyfwptz.lixiang.AQBP.di.modules;

import com.cgwx.yyfwptz.lixiang.AQBP.presenter.LoginContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yyfwptz on 2017/8/8.
 */

@Module
public class LoginModule {
    private final LoginContract.View lView;

    public LoginModule(LoginContract.View lView) {
        this.lView = lView;
    }

    @Provides
    public LoginContract.View provideLoginView(){
        return lView;
    }
}
