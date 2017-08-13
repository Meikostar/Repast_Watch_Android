package com.canplay.repast_wear.mvp.present;

import android.content.Context;
import android.support.annotation.NonNull;

import com.canplay.repast_wear.base.manager.ApiManager;
import com.canplay.repast_wear.bean.Contract;
import com.canplay.repast_wear.mvp.http.TableApi;
import com.canplay.repast_wear.mvp.model.PROVINCE;
import com.canplay.repast_wear.mvp.model.Table;
import com.canplay.repast_wear.mvp.model.provinceCityList;
import com.canplay.repast_wear.net.MySubscriber;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.Subscription;


public class TablePresenter implements TableContract.Presenter {
    private Subscription subscription;

    private TableContract.View mView;

    private TableApi tableApi;

    @Inject
    TablePresenter(ApiManager apiManager){
        tableApi = apiManager.createApi(TableApi.class);
    }

    @Override
    public void getCityList() {
        Map<String, String> params = new TreeMap<>();
        subscription=ApiManager.setSubscribe(tableApi.getCityList(ApiManager.getParameters(params, false)), new MySubscriber<List<provinceCityList>>() {
            @Override
            public void onError(Throwable e){
                super.onError(e);
            }
            @Override
            public void onNext(List<provinceCityList> list) {
                mView.toList(list, Contract.CITY_LIST);
            }
        });
    }
    @Override
    public void getBusinessTableList(long businessId, final Context context) {
        Map<String, String> params = new TreeMap<>();
        params.put("businessId", businessId + "");
        subscription = ApiManager.setSubscribe(tableApi.getBusinessTableList(ApiManager.getParameters(params, true)), new MySubscriber<List<Table>>(){
            @Override
            public void onError(Throwable e){
                super.onError(e);
            }

            @Override
            public void onNext(List<Table> entity){
                mView.toList(entity,1);
            }
        });
    }
    @Override
    public void getBusinessNameList(String areaCode) {
        Map<String, String> params = new TreeMap<>();
        params.put("areaCode", areaCode + "");
        subscription = ApiManager.setSubscribe(tableApi.getBusinessNameList(ApiManager.getParameters(params, true)), new MySubscriber<List<PROVINCE>>(){
            @Override
            public void onError(Throwable e){
                super.onError(e);
            }

            @Override
            public void onNext(List<PROVINCE> entity){
               mView.toList(entity,1);
            }
        });
    }

    @Override
    public void attachView(@NonNull TableContract.View view){
        mView = view;
    }


    @Override
    public void detachView(){
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        mView = null;
    }
}
