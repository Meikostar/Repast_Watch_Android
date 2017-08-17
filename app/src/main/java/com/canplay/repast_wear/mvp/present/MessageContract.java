package com.canplay.repast_wear.mvp.present;

import android.content.Context;

import com.canplay.repast_wear.base.BasePresenter;
import com.canplay.repast_wear.base.BaseView;

public class MessageContract {
    public interface View extends BaseView {

//        <T> void toList(List<T> list, int type, int... refreshType);
//
//        <T> void toEntity(T entity);
//
//        void toNextStep(int type);

        void showTomast(String msg);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 推送消息
         */
        void pushMessage(long pushId,long tableId, Context context);
        /**
         * 推送状态消息
         */
        void finishPushMessage(long pushId,Context context);

    }
}
