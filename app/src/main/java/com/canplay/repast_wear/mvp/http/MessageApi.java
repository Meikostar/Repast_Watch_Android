package com.canplay.repast_wear.mvp.http;

import com.canplay.repast_wear.mvp.model.Message;
import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by qi_fu on 2017/7/27.
 */

public interface MessageApi {
    /**
     * 获得消息数据
     * @param options
     * @return
     */
    @GET("")
    Observable<Message> getMessage(@QueryMap Map<String, String> options);

    @GET("")
    Observable<List<Message>> getMessageList(@QueryMap Map<String, String> options);
}
