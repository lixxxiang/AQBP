package com.cgwx.yyfwptz.lixiang.AQBP.presenter;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.alarmInfo;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.reserved;

/**
 * Created by yyfwptz on 2017/8/9.
 */

public interface FMainContract {
    interface Presenter{
        void test();
        void modifyPosition(double str1, double str2, String str3, String str4);
        void modifyPoliceState(String str1, String str2);
        void modifyPoliceState2(String str1, String str2);
        void initStatus(String str1);
        void getAlarm(String str1);
        void refuseAlarm(String str1, String str2);
        void acceptAlarm(String str1, String str2);
    };

    interface View{
        void stateIsListenPolice();
        void stateIsOutPolice(String str1, reserved infos);
        void gotAlarm(alarmInfo ai);
        void alarmRefused();
        void stateIsStop();
        void alarmAccpted(String str1);
        void whatTheFuck();
    }
}
