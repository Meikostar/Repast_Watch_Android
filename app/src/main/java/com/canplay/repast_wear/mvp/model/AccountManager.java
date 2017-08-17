package com.canplay.repast_wear.mvp.component;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.RxBus;
import com.canplay.repast_wear.base.SubscriptionBean;
import com.canplay.repast_wear.mvp.activity.ToContactActivity;
import com.canplay.repast_wear.mvp.model.Message;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MessageManager {
    private static List<PopupWindow> popupWindowList = new ArrayList<>();
    private static List<Message> messageList = new ArrayList<>();
    private static CountDownTimer removetimer;
    private static List<Message> haveRespond = new ArrayList<>();
    private static List<Message> noRespond = new ArrayList<>();

    public static void send(PopupWindow pop, long time, Message message) {
        messageList.add(message);
        popupWindowList.add(pop);
        disMissPop(message, time);
    }

    public static void disMissPop(final Message passMessage, long time) {
        removetimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //自动转移到其他设备
            }
            @Override
            public void onFinish() {
                haveRespond.remove(passMessage);
            }
        }.start();
    }

    //判断是否存在PopView
    public static boolean listIsNull() {
        if (popupWindowList.size() != 0) {
            return false;
        }
        return true;
    }

    public static void remove(int positon) {
        popupWindowList.remove(positon);
        messageList.remove(positon);
    }

    private static CountDownTimer timer;
    private static PopupWindow window;
    private static Vibrator vibrator;
    private static TextView toOther;
    private static TextView form;
    private static TextView complain;
    private static Subscription mSubscription;
    private static Message message;


    public static void showJPushDate(Activity activity, View parentView) {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.CHOOSE) {
                    message = (Message) bean.content;
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        // 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        //加载子布局
        View view = inflater.inflate(R.layout.popwindow, null);
        if (window == null) {
            window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        toOther = (TextView) view.findViewById(R.id.to_other);
        form = (TextView) view.findViewById(R.id.former);
        complain = (TextView) view.findViewById(R.id.complain);
        final TextView clockTime = (TextView) view.findViewById(R.id.clock_time);
        //获取焦点
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        //背景颜色
        window.setBackgroundDrawable(new ColorDrawable(0xffffff));
        //动画效果（进入页面和退出页面时的效果）
        //window.setAnimationStyle(R.style.windows);
        //显示位置：showAtLocation(主布局所点击的按钮id, 位置, x, y);
        window.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        //弹窗消失监听
        if (timer == null) {
            timer = new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    clockTime.setText((millisUntilFinished / 1000) + "");
//                    showtime = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    noRespond.add(message);
                    window.dismiss();
                }
            }.start();
        } else timer.start();
        setPopListen(activity, message);
    }

    private static void setPopListen(final Activity activity, final Message message) {
        toOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haveRespond.add(message);
                window.dismiss();
                Intent intent = new Intent(activity, ToContactActivity.class);
                activity.startActivity(intent);
            }
        });
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                haveRespond.add(message);
            }
        });
    }

}
