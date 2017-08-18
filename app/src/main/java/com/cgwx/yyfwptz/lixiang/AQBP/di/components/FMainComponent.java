package com.cgwx.yyfwptz.lixiang.AQBP.di.components;

import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.FMainModule;
import com.cgwx.yyfwptz.lixiang.AQBP.view.fragment.MainFragment;

import dagger.Component;

/**
 * Created by yyfwptz on 2017/8/9.
 */
@Component(modules = FMainModule.class)
public interface FMainComponent {
    void inject(MainFragment fragment);
}
