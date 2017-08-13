package com.canplay.repast_wear.mvp.present;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.canplay.repast_wear.base.manager.ApiManager;
import com.canplay.repast_wear.mvp.http.ContactApi;
import com.canplay.repast_wear.mvp.model.Contact;
import com.canplay.repast_wear.net.MySubscriber;

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.Subscription;


public class ContactPresenter implements ContactContract.Presenter {
    private Subscription subscription;

    private ContactContract.View mView;

    private ContactApi contactApi;

    @Inject
    ContactPresenter(ApiManager apiManager){
        contactApi = apiManager.createApi(ContactApi.class);
    }
    @Override
    public void getContacts(long userId, final Context context) {
        Map<String, String> params = new TreeMap<>();
        params.put("userId", userId + "");
        subscription = ApiManager.setSubscribe(contactApi.getContacts(ApiManager.getParameters(params, false)), new MySubscriber<Contact>(){
            @Override
            public void onError(Throwable e){
                super.onError(e);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Contact entity){
                mView.toEntity(entity);
            }
        });
    }

    @Override
    public void attachView(@NonNull ContactContract.View view){
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
