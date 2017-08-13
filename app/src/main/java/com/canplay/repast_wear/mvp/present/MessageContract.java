package com.canplay.repast_wear.mvp.present;

import android.content.Context;


import com.canplay.repast_wear.base.BasePresenter;
import com.canplay.repast_wear.base.BaseView;

import java.util.List;

public class MessageContract {
    interface View extends BaseView {

        <T> void toList(List<T> list, int type, int... refreshType);

        <T> void toEntity(T entity);

        void toNextStep(int type);

        void showTomast(String msg);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 获得资讯详情
         */
        void getMessage(long newsId, Context context);

        /**
         * 获得资讯列表
         */
        void getMessageList(long lastId, int pageSize, int category, Context context, int refreshTyep);

    }
}
