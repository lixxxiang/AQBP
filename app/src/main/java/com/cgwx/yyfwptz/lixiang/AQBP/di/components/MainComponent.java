package com.cgwx.yyfwptz.lixiang.AQBP.di.components;

import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.MainModule;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.MainActivity;

import dagger.Component;

/**
 * Created by yyfwptz on 2017/8/9.
 */
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
//