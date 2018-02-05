package com.canplay.repast_wear.mvp.http;

import com.canplay.repast_wear.mvp.model.ApkUrl;
import com.canplay.repast_wear.mvp.model.DEVICE;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.mvp.model.Resps;
import com.canplay.repast_wear.mvp.model.RespsTable;
import com.canplay.repast_wear.mvp.model.Version;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface MessageApi {


    /**
     * 获取消息列表
     * @param options
     * @return
     */
    @POST("wx/getWatchMessageList")
    Observable<Resps> getWatchMessageList(@QueryMap Map<String, String> options);
    /**
     * 获取消息列表
     * @param options
     * @return
     */
    @POST("wx/watchOrderInfo")
    Observable<List<Message>> watchOrderInfo(@QueryMap Map<String, String> options);




    /**
     * 完成
     * @param options
     * @return
     */
    @POST("wx/finishPushMessage")
    Observable<String> finishPushMessage(@QueryMap Map<String, String> options);

    /**
     * 手表端推送（自动转移和手动转移）
     * @param options
     * @return
     */
    @POST("wx/watchPushMessage")
    Observable<String> watchPushMessage(@QueryMap Map<String, String> options);



    /**
     * 手表端推送（自动转移和手动转移）
     * @param options
     * @returnmerchant/watchPushList
     */
    @POST("wx/watchPushList")
    Observable<Resps> watchPushOrder(@QueryMap Map<String, String> options);

    /**
     * 手表端推送（自动转移和手动转移）
     * @param options
     * @return
     */
    @POST("wx/finishPush")
    Observable<String> finishPush(@QueryMap Map<String, String> options);

    /**
     * 重新绑定
     * @param options
     * @return
     */
    @POST("wx/deviceSignOutV")
    Observable<String> deviceSignOut(@QueryMap Map<String, String> options);

    /**
     * 获取转移的其他手表
     * @param options
     * @return
     */
    @POST("wx/getWatchList")
    Observable<RespsTable> getWatchList(@QueryMap Map<String, String> options);


    /**
     * 绑定信息
     * @param options
     * @return
     */
    @POST("wx/deviceInfo")
    Observable<DEVICE> deviceInfo(@QueryMap Map<String, String> options);

    /**
     * @param options
     * @return
     */
    @GET("wx/getInit")
    Observable<Version> getInit(@QueryMap Map<String, String> options);

    /**
     * @param options
     * @return
     */
    @POST("wx/deletePushInfo")
    Observable<String> deletePushInfo(@QueryMap Map<String, String> options);
    /**
     *   apk下载 POST
     * @return
     */
    @POST("wx/getApkInfo")
    Observable<ApkUrl> getApkInfo(@QueryMap Map<String, String> options);
}
