package com.cgwx.yyfwptz.lixiang.AQBP.di.modules;

import com.cgwx.yyfwptz.lixiang.AQBP.presenter.MainContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yyfwptz on 2017/8/9.
 */
@Module
public class MainModule {
    private final MainContract.View mView;

    public MainModule(MainContract.View mView){
        this.mView = mView;
    }

    @Provides
    public MainContract.View provideMainView(){
        return mView;
    }
}
