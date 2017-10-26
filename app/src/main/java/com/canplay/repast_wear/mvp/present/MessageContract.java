package com.canplay.repast_wear.mvp.present;

import android.content.Context;

import com.canplay.repast_wear.base.BasePresenter;
import com.canplay.repast_wear.base.BaseView;

import java.util.List;

public class MessageContract {
    public interface View extends BaseView {

        <T> void toList(List<T> list, int type, int... refreshType);

        <T> void toEntity(T entity,int type);

        void toNextStep(int type);

        void showTomast(String msg);
    }

    interface Presenter extends BasePresenter<View> {
        /**
         * 获取消息列表
         */
        void getWatchMessageList(String deviceCode, int pageSize, int pageNo, int state, Context context);

        /**
         * 推送状态消息
         */
        void finishPushMessage(long pushId, Context context);

        /**
         * 手表端推送（自动转移和手动转移)
         */
        void watchPushMessage(long pushId, String deviceCode);

        /**
         * 获取转移的其他手表
         */
        void getWatchList(String deviceCode, String businessId,int pageSize,int pageIndex, Context context);

        /**
         * 推送状态消息
         */
        void deviceSignOut(String deviceCode, String pwd, int type);

        /**
         * 获得绑定信息
         */
        void deviceInfo(String deviceCode);

        /**
         * 获得绑定信息
         */
        void getInit(String deviceCode);


        void deletePushInfo(long pushId);

        // apk下载 POST
        void getApkInfo();

    }
}
