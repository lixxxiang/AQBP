package com.cgwx.yyfwptz.lixiang.AQBP.di.modules;

import com.cgwx.yyfwptz.lixiang.AQBP.presenter.FMainContract;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.FMineContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yyfwptz on 2017/8/9.
 */
@Module
public class FMineModule {
    private final FMineContract.View fmineView;

    public FMineModule(FMineContract.View fmineView) {
        this.fmineView = fmineView;
    }

    @Provides
    public FMineContract.View provideFMineView(){
        return fmineView;
    }
}
