package com.cgwx.yyfwptz.lixiang.AQBP.di.modules;

import com.cgwx.yyfwptz.lixiang.AQBP.presenter.ProcessContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yyfwptz on 2017/8/9.
 */
@Module
public class ProcessModule {
    private final ProcessContract.View pView;

    public ProcessModule(ProcessContract.View pView) {
        this.pView = pView;
    }

    @Provides
    public ProcessContract.View provideProcessView(){
        return pView;
    }
}
