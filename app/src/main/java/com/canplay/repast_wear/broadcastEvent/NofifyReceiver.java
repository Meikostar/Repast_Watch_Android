package com.canplay.repast_wear.broadcastEvent;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.canplay.repast_wear.base.RxBus;
import com.canplay.repast_wear.base.SubscriptionBean;
import com.canplay.repast_wear.base.manager.AppManager;
import com.canplay.repast_wear.mvp.activity.MainActivity;
import com.canplay.repast_wear.mvp.model.Message;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by yg on 2017/6/16.
 */

public class NofifyReceiver extends BroadcastReceiver {
    private static final String TAG = "NofifyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.d(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的通知");
            openNotification(context, bundle);
//            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
//            openNotification(context, bundle);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        AppManager appManager = AppManager.getInstance(context);
        if (appManager.isActivityExist(MainActivity.class)) {
            appManager.finishActivityTop(MainActivity.class);
            Log.e("isExist",true+"");
        } else {
            Intent i = new Intent();  //自定义打开的界面
            i.setClass(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            Log.e("isExist",false+"");
        }
        /*
        * "pushId": "2",
            "menuName": "加水",
            "tableNo": "5"
            "message":{"msg_content":"加水","extras":{"pushId":"6","businessId":"1","menuName":"加水","tableNo":"5"}}
*/
        long pushId = 0;
        String menuName;
        String tableNo;
        try {
            JSONObject extrasJson = new JSONObject(extras);
            pushId = extrasJson.optJSONObject("extras").optLong("pushId");
            menuName = extrasJson.optJSONObject("extras").optString("menuName");
            tableNo = extrasJson.optJSONObject("extras").optString("tableNo");
        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
        Message message = new Message();
        message.setMenuName(menuName);
        message.setPushId(pushId);
        message.setTableNo(tableNo);
        RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHOOSE, message));
    }
}
