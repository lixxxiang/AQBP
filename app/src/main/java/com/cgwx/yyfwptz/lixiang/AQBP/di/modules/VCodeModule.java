package com.cgwx.yyfwptz.lixiang.AQBP.di.modules;

import com.cgwx.yyfwptz.lixiang.AQBP.presenter.VCodeContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yyfwptz on 2017/8/8.
 */

@Module
public class VCodeModule {
    private final VCodeContract.View vView;

    public VCodeModule(VCodeContract.View vView){
        this.vView = vView;
    }
    @Provides
    public VCodeContract.View provideVcodeView(){
        return vView;
    }
}
