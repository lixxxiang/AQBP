package com.cgwx.yyfwptz.lixiang.AQBP.util;

import java.util.Timer;

/**
 * Created by yyfwptz on 2017/8/9.
 */

public class CountdownTimer1 {
    private static Timer countdownTimer = null;
    private CountdownTimer1(){};
    public static Timer countdownTimer(){
        if (countdownTimer == null){
            countdownTimer = new Timer();
        }
        return countdownTimer;
    }
}
