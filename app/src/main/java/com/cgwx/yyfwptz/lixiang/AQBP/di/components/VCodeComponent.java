package com.cgwx.yyfwptz.lixiang.AQBP.di.components;

import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.VCodeModule;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.VCodeActivity;

import dagger.Component;

/**
 * Created by yyfwptz on 2017/8/8.
 */

@Component(modules = VCodeModule.class)
public interface VCodeComponent {
    void inject(VCodeActivity activity);
}
