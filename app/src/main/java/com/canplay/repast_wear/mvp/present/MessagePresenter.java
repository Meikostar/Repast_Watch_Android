package com.canplay.repast_wear.mvp.present;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;


import com.canplay.repast_wear.base.manager.ApiManager;
import com.canplay.repast_wear.mvp.http.MessageApi;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.net.MySubscriber;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by qi_fu on 2017/7/27.
 */

public class MessagePresenter implements MessageContract.Presenter {
    private Subscription subscription;

    private MessageContract.View mView;

    private MessageApi messageApi;

    @Inject
    MessagePresenter(ApiManager apiManager){
        messageApi = apiManager.createApi(MessageApi.class);
    }

    @Override
    public void getMessage(long newsId, final Context context){
        Map<String, String> params = new TreeMap<>();
        params.put("newsId", newsId + "");
        subscription = ApiManager.setSubscribe(messageApi.getMessage(ApiManager.getParameters(params, false)), new MySubscriber<Message>(){
            @Override
            public void onError(Throwable e){
                super.onError(e);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Message entity){
                mView.toEntity(entity);
            }
        });
    }

    @Override
    public void getMessageList(long lastId, int pageSize, final int category, final Context context, final int refreshTyep){
        Map<String, String> params = new TreeMap<>();
        params.put("lastId", lastId + "");
        params.put("pageSize", pageSize + "");
        params.put("category", category + ""); // 0：资讯 1：活动
        subscription = ApiManager.setSubscribe(messageApi.getMessageList(ApiManager.getParameters(params, false)), new MySubscriber<List<Message>>(){
            @Override
            public void onError(Throwable e){
                super.onError(e);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<Message> entityList){
//                if(category == 0){
//                    mView.toList(entityList, Contract.INFORMATION, refreshTyep);
//                }else{
//                    mView.toList(entityList, Contract.EVENT, refreshTyep);
//                }
            }
        });
    }

    @Override
    public void attachView(@NonNull MessageContract.View view){
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
