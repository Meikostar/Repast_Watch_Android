package com.canplay.repast_wear.mvp.present;

import android.content.Context;

import com.canplay.repast_wear.base.BasePresenter;
import com.canplay.repast_wear.base.BaseView;

import java.util.List;

public class TableContract {
   public interface View extends BaseView {

        <T> void toList(List<T> list, int type, int... refreshType);
        <T> void toEntity(T entity);

        void toNextStep(int type);

        void showTomast(String table);
    }

    interface Presenter extends BasePresenter<View> {

//        /**
//         * 获得绑定省市列表
//         */
//        void getCityList();

        /**
         * 获得商店列表
         */
        void getBusinessNameList(String areaCode);

        /**
         * 获得桌号列表
         */
        void getBusinessTableList(long businessId,String deviceCode, Context context);

        /**
         * 获得绑定桌号
         */
        void bondBusiness(String deviceCode,long businessId,String tableNo, Context context);



    }
}
