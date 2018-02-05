package com.canplay.repast_wear.mvp.present;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.canplay.repast_wear.base.manager.ApiManager;
import com.canplay.repast_wear.mvp.http.MessageApi;
import com.canplay.repast_wear.mvp.model.ApkUrl;
import com.canplay.repast_wear.mvp.model.DEVICE;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.mvp.model.Resps;
import com.canplay.repast_wear.mvp.model.RespsTable;
import com.canplay.repast_wear.mvp.model.Version;
import com.canplay.repast_wear.net.MySubscriber;
import com.canplay.repast_wear.util.SpUtil;

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
        params.put("state", 0 + "");//1：忽略的消息，2已完成
        subscription = ApiManager.setSubscribe(messageApi.getWatchMessageList(ApiManager.getParameters(params, true)), new MySubscriber<Resps>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(Resps entity) {
                mView.toList(entity.getPushListResps(),entity.getHasNext());
//                mView.toEntity(entity);
            }
        });
    }
    @Override
    public void watchOrderInfo(String detailNo, final Context context) {
        Map<String, String> params = new TreeMap<>();
        params.put("detailNo", detailNo + "");

        subscription = ApiManager.setSubscribe(messageApi.watchOrderInfo(ApiManager.getParameters(params, true)), new MySubscriber<List<Message >>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(List<Message > entity) {
                mView.toList(entity,0);
//                mView.toEntity(entity);
            }
        });
    }


    @Override
    public void getOrderList(int pageNo, final Context context) {
        Map<String, String> params = new TreeMap<>();

        params.put("page", pageNo + "");//当前页 首页传1
        subscription = ApiManager.setSubscribe(messageApi.watchPushOrder(ApiManager.getParameters(params, true)), new MySubscriber<Resps>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(Resps entity) {
                mView.toList(entity.watchOrderLists,entity.getHasNext());
//                mView.toEntity(entity);
            }
        });
    }
    @Override
    public void finishPush(String pushId, final Context context) {
        Map<String, String> params = new TreeMap<>();

        params.put("pushId", pushId + "");//当前页 首页传1
        subscription = ApiManager.setSubscribe(messageApi.finishPush(ApiManager.getParameters(params, true)), new MySubscriber<String>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(String entity) {
                mView.toNextStep(2);
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
    public void watchPushMessage(long pushId, String deviceCode) {
        Map<String, String> params = new TreeMap<>();
        params.put("pushId", pushId + "");
        params.put("deviceCode", deviceCode );
        subscription = ApiManager.setSubscribe(messageApi.watchPushMessage(ApiManager.getParameters(params, true)), new MySubscriber<String>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(String entity) {
                mView.toNextStep(403);
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
                mView.toEntity(entity,3);
            }
        });
    }
    @Override
    public void getInit(String deviceCode) {
        Map<String, String> params = new TreeMap<>();
        params.put("deviceCode", deviceCode + "");
        subscription = ApiManager.setSubscribe(messageApi.getInit(ApiManager.getParameters(params, false)), new MySubscriber<Version>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(Version entity) {
                mView.toEntity(entity,1);
            }
        });
    }
    @Override
    public void deviceSignOut(String deviceCode, String psw, final int type,int types) {
        Map<String, String> params = new TreeMap<>();
        SpUtil  sp = SpUtil.getInstance();

       String businessId = sp.getString("businessId");
        params.put("deviceCode", deviceCode + "");
        params.put("pwd", psw + "");
        params.put("type", types + "");
        params.put("businessId", businessId + "");
            subscription = ApiManager.setSubscribe(messageApi.deviceSignOut(ApiManager.getParameters(params, true)), new MySubscriber<String>() {
                @Override
                public void onError(Throwable e) {
                    mView.toNextStep(4);//密码错误处理
//                super.onError(e);
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
    public void deletePushInfo(long pushId) {
        Map<String, String> params = new TreeMap<>();
        params.put("pushId", pushId + "");
        subscription = ApiManager.setSubscribe(messageApi.deletePushInfo(ApiManager.getParameters(params, true)), new MySubscriber<String>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(String entity) {
                mView.toNextStep(2);
            }
        });
    }
    @Override
    public void getApkInfo() {
        Map<String, String> params = new TreeMap<>();
        subscription = ApiManager.setSubscribe(messageApi.getApkInfo(ApiManager.getParameters(params, true)), new MySubscriber<ApkUrl>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(ApkUrl entity) {
                mView.toEntity(entity,2);
            }
        });
    }


    @Override
    public void getWatchList(String deviceCode, String businessId,int pageSize,int pageIndex, final Context context) {
        Map<String, String> params = new TreeMap<>();
        params.put("deviceCode", deviceCode + "");
        params.put("businessId", businessId + "");
        params.put("pageSize", pageSize + "");
        params.put("pageIndex", pageIndex + "");
        subscription = ApiManager.setSubscribe(messageApi.getWatchList(ApiManager.getParameters(params, true)), new MySubscriber<RespsTable>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
//                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(RespsTable list ) {
//                mView.toEntity(list);
//                mView.toNextStep(list.getHasNext());
                mView.toList(list.getList(),list.getHasNext());
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
