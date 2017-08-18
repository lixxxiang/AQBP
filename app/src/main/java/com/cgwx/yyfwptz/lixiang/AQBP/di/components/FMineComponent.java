package com.cgwx.yyfwptz.lixiang.AQBP.di.components;

import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.FMainModule;
import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.FMineModule;
import com.cgwx.yyfwptz.lixiang.AQBP.view.fragment.MainFragment;
import com.cgwx.yyfwptz.lixiang.AQBP.view.fragment.MineFragment;

import dagger.Component;

/**
 * Created by yyfwptz on 2017/8/9.
 */
@Component(modules = FMineModule.class)
public interface FMineComponent {
    void inject(MineFragment fragment);
}
