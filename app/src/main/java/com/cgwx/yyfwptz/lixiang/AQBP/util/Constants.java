package com.cgwx.yyfwptz.lixiang.AQBP.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.cgwx.yyfwptz.lixiang.AQBP.R;

/**
 * Created by yyfwptz on 2017/7/17.
 */

public class Constants {
    //    public static final String prefix = "http://10.10.90.11:8086/";
    public static final String prefix = "http://202.111.178.10:28084/";
    private static long lastClickTime;
    public static MediaPlayer mediaPlayer;
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void playVoice(Context context) {
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopVoice() {
        if (null != mediaPlayer) {
            mediaPlayer.stop();
        }
    }



}
