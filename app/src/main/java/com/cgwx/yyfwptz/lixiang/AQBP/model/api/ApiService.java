package com.cgwx.yyfwptz.lixiang.AQBP.model.api;

import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.acceptAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.checkMessage;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.completeAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.getAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.initStatus;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.isTelAvailable;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.modifyPoliceState;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.modifyPosition;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.refuseAlarm;
import com.cgwx.yyfwptz.lixiang.AQBP.model.entity.sendMessage;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by yyfwptz on 2017/8/8.
 */

public interface ApiService {
    @POST("mobile/common/sendMessage")
    @FormUrlEncoded
    Call<sendMessage> sendMessage(@Field("telephone") String targetSentence);

    @GET("mobile/police/isTelAvailable")
    Call<isTelAvailable> isTelAvailable(@Query("telephone") String tel);

    @POST("mobile/police/checkMessage")
    @FormUrlEncoded
    Call<checkMessage> checkMessage(@Field("telephone") String targetSentence, @Field("code") String targetSentence2);

    @POST("mobile/police/modifyPosition")
    @FormUrlEncoded
    Call<modifyPosition> modifyPosition(@Field("latitude") double targetSentence,
                                        @Field("longitude") double targetSentence2,
                                        @Field("policeId") String targetSentence3,
                                        @Field("time") String targetSentence4);

    @POST("mobile/police/modifyPoliceState")
    @FormUrlEncoded
    Call<modifyPoliceState> modifyPoliceState(@Field("state") String targetSentence,
                                              @Field("policeId") String targetSentence2);


    @POST("mobile/police/getAlarm")
    @FormUrlEncoded
    Call<getAlarm> getAlarm(@Field("policeId") String targetSentence);


    @POST("mobile/police/acceptAlarm")
    @FormUrlEncoded
    Call<acceptAlarm> acceptAlarm(@Field("alarmId") String targetSentence,
                                  @Field("policeId") String targetSentence2);

    @POST("mobile/police/refuseAlarm")
    @FormUrlEncoded
    Call<refuseAlarm> refuseAlarm(@Field("alarmId") String targetSentence,
                                  @Field("policeId") String targetSentence2);

    @POST("mobile/police/initStatus")
    @FormUrlEncoded
    Call<initStatus> initStatus(@Field("policeId") String targetSentence);

    @POST("mobile/police/completeAlarm")
    @FormUrlEncoded
    Call<completeAlarm> completeAlarm(@Field("policeId") String targetSentence,
                                      @Field("alarmId") String targetSentence2);
}
