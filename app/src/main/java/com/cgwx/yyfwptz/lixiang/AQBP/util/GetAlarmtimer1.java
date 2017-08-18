package com.cgwx.yyfwptz.lixiang.AQBP.util;

import java.util.Timer;

/**
 * Created by yyfwptz on 2017/8/9.
 */

public class GetAlarmtimer1 {
    private static Timer getAlarmtimer = null;
    private GetAlarmtimer1(){};
    public static Timer getAlarmtimer(){
        if (getAlarmtimer == null){
            getAlarmtimer = new Timer();
        }
        return getAlarmtimer;
    }
}
