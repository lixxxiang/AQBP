package com.cgwx.yyfwptz.lixiang.AQBP.presenter;

/**
 * Created by yyfwptz on 2017/8/8.
 */

public interface VCodeContract {
    interface Presenter {
        void test();

        void resendMessage(String tel);

        void checkMessage(String tel, String vcode);
    }

    interface View {
        void recount();

        void checkVcodeSuccess(String id, String name, String tel);
    }
}
