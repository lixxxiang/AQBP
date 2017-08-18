package com.cgwx.yyfwptz.lixiang.AQBP.di.components;

import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.ProcessModule;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.ProcessActivity;

import dagger.Component;

/**
 * Created by yyfwptz on 2017/8/9.
 */
@Component(modules = ProcessModule.class)
public interface ProcessComponent {
    void inject(ProcessActivity activity);
}
