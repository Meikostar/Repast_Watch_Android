package com.canplay.repast_wear.mvp.http;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface MessageApi {
    /**
     * 获得消息数据
     * @param options
     * @return
     */
    @POST("wx/watchPushMessage")
    Observable<String> pushMessage(@QueryMap Map<String, String> options);


    @POST("wx/finishPushMessage")
    Observable<String> finishPushMessage(@QueryMap Map<String, String> options);

}
