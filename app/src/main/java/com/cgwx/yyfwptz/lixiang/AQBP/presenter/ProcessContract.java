package com.cgwx.yyfwptz.lixiang.AQBP.presenter;

/**
 * Created by yyfwptz on 2017/8/9.
 */

public interface ProcessContract {
    interface View{
        void completeAlarmSuccess();
    }

    interface Presenter{
        void completeAlarm(String str1, String str2);
    }
}
