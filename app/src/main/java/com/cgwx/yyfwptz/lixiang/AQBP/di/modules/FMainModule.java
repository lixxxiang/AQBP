package com.cgwx.yyfwptz.lixiang.AQBP.di.modules;

import com.cgwx.yyfwptz.lixiang.AQBP.presenter.FMainContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yyfwptz on 2017/8/9.
 */
@Module
public class FMainModule {
    private final FMainContract.View fmainView;

    public FMainModule(FMainContract.View fmainView) {
        this.fmainView = fmainView;
    }

    @Provides
    public FMainContract.View provideFMainView(){
        return fmainView;
    }
}
