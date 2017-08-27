package com.canplay.repast_wear.mvp.http;

import com.canplay.repast_wear.mvp.model.DEVICE;
import com.canplay.repast_wear.mvp.model.PROVINCE;
import com.canplay.repast_wear.mvp.model.Table;

import java.util.List;
import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface TableApi {
    /**
//     * 获得城市列表
//     * @return
//     */
//    @GET("wx/getCityList")
//    Observable<List<CITY>> getCityList(@FieldMap Map<String, String> options);

    /**
     *
     * 获得桌位号码
     * @param options
     * @return
     */
    @POST("wx/getBusinessTableList")
    Observable<List<Table>>getBusinessTableList(@QueryMap Map<String, String> options);

    /**
     * wx/getBusinessNameList
     * @param options
     * @return
     */
    @POST("wx/getBusinessNameList")
    Observable<List<PROVINCE>> getBusinessNameList(@QueryMap Map<String, String> options);
    /**
     * 绑定桌号
     * @param options
     * @return
     */
    @POST("wx/watchBondBusiness")
    Observable<String> bondBusiness(@QueryMap Map<String, String> options);

}
