package com.cgwx.yyfwptz.lixiang.AQBP.di.components;


import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.LoginModule;
import com.cgwx.yyfwptz.lixiang.AQBP.view.activity.LoginActivity;

import dagger.Component;

/**
 * Created by yyfwptz on 2017/8/8.
 */

@Component(modules = LoginModule.class)
public interface LoginComponent{
    void inject(LoginActivity activity);
}