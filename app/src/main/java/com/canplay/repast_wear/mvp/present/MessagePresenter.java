package com.canplay.repast_wear.mvp.present;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.canplay.repast_wear.base.manager.ApiManager;
import com.canplay.repast_wear.mvp.http.MessageApi;
import com.canplay.repast_wear.net.MySubscriber;

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
    public void pushMessage(long pushId, long tableId, final Context context) {
        Map<String, String> params = new TreeMap<>();
        params.put("pushId", pushId + "");
        params.put("tableId", tableId + "");
        subscription = ApiManager.setSubscribe(messageApi.pushMessage(ApiManager.getParameters(params, true)), new MySubscriber<String>() {
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
    public void finishPushMessage(long pushId, final Context context) {
        Map<String, String> params = new TreeMap<>();
        params.put("pushId", pushId + "");
        subscription = ApiManager.setSubscribe(messageApi.pushMessage(ApiManager.getParameters(params, true)), new MySubscriber<String>() {
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
