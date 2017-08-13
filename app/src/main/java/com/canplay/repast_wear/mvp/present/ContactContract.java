package com.canplay.repast_wear.mvp.present;

import android.content.Context;

import com.canplay.repast_wear.base.BasePresenter;
import com.canplay.repast_wear.base.BaseView;

import java.util.List;

public class ContactContract {
    interface View extends BaseView {

        <T> void toList(List<T> list, int type, int... refreshType);
        <T> void toEntity(T entity);

        void toNextStep(int type);

        void showTomast(String msg);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 获得联系人列表
         */
        void getContacts(long userId, Context context);

    }
}
