package com.canplay.repast_wear.mvp.present;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.canplay.repast_wear.base.manager.ApiManager;
import com.canplay.repast_wear.mvp.http.MessageApi;
import com.canplay.repast_wear.mvp.model.DEVICE;
import com.canplay.repast_wear.mvp.model.Resps;
import com.canplay.repast_wear.mvp.model.Table;
import com.canplay.repast_wear.net.MySubscriber;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.Subscription;


public class MessagePresenter implements MessageContract.Presenter {
    private Subscription subscription;

    private MessageContract.View mView;

    private MessageApi messageApi;

    @Inject
    MessagePresenter(ApiManager apiManager) {
        messageApi = apiManager.createApi(MessageApi.class);
    }

    @Override
    public void getWatchMessageList(String deviceCode,int pageSize,int pageNo,int state, final Context context) {
        Map<String, String> params = new TreeMap<>();
        params.put("deviceCode", deviceCode + "");
        params.put("pageSize", pageSize + "");//每页数
        params.put("pageNo", pageNo + "");//当前页 首页传1
        params.put("state", state + "");//1：忽略的消息，2已完成
        subscription = ApiManager.setSubscribe(messageApi.getWatchMessageList(ApiManager.getParameters(params, true)), new MySubscriber<Resps>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(Resps entity) {
                mView.toEntity(entity);
            }
        });
    }

    @Override
    public void finishPushMessage(long pushId, final Context context) {
        Map<String, String> params = new TreeMap<>();
        params.put("pushId", pushId + "");
        subscription = ApiManager.setSubscribe(messageApi.finishPushMessage(ApiManager.getParameters(params, true)), new MySubscriber<String>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String entity) {
                mView.showTomast(entity);
            }
        });
    }

    @Override
    public void watchPushMessage(long pushId,long tableId) {
        Map<String, String> params = new TreeMap<>();
        params.put("pushId", pushId + "");
        params.put("tableId", tableId + "");
        subscription = ApiManager.setSubscribe(messageApi.watchPushMessage(ApiManager.getParameters(params, true)), new MySubscriber<String>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(String entity) {
                mView.toNextStep(1);
            }
        });
    }
    @Override
    public void deviceInfo(String deviceCode) {
        Map<String, String> params = new TreeMap<>();
        params.put("deviceCode", deviceCode + "");
        subscription = ApiManager.setSubscribe(messageApi.deviceInfo(ApiManager.getParameters(params, true)), new MySubscriber<DEVICE>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(DEVICE entity) {
                mView.toEntity(entity);
            }
        });
    }
    @Override
    public void deviceSignOut(String deviceCode, String psw, final int type) {
        Map<String, String> params = new TreeMap<>();
        params.put("deviceCode", deviceCode + "");
        params.put("psw", psw + "");
        subscription = ApiManager.setSubscribe(messageApi.deviceSignOut(ApiManager.getParameters(params, true)), new MySubscriber<String>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(String entity) {
                if (type ==3)
                mView.toNextStep(3);
                else
                    mView.toNextStep(2);
            }
        });
    }
    @Override
    public void getWatchList(String deviceCode, String businessId, final Context context) {
        Map<String, String> params = new TreeMap<>();
        params.put("deviceCode", deviceCode + "");
        params.put("businessId", businessId + "");
        subscription = ApiManager.setSubscribe(messageApi.getWatchList(ApiManager.getParameters(params, true)), new MySubscriber<List<Table>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<Table> list ) {
                mView.toList(list,1);
            }
        });
    }

    @Override
    public void attachView(@NonNull MessageContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        mView = null;
    }
}
