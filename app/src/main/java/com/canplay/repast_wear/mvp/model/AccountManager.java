package com.canplay.repast_wear.mvp.model;


import android.os.CountDownTimer;

import com.canplay.repast_wear.mvp.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 临时数据存储
 */
public class AccountManager {
    private static List<Message> messageList = new ArrayList<>();
    private static List<Table> tableList = new ArrayList<>();
    private static CountDownTimer removetimer;
    private static List<Message> haveRespond = new ArrayList<>();
    private static List<Message> noRespond = new ArrayList<>();
    private static MainActivity activity;

    public static void send(MainActivity mainActivity, long time, Message message) {
        messageList.add(message);
        disMissPop(message, time);
        activity = mainActivity;
    }

    public static void disMissPop(final Message passMessage, long time) {
        haveRespond.add(passMessage);
        removetimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                //自动转移到其他设备
                if (activity != null) {
//                    activity.sendMessage(0, passMessage.getPushId());
                }
//                noRespond.add(passMessage);
//                haveRespond.remove(passMessage);
            }
        }.start();
    }

    public static void remove(int positon) {
        messageList.remove(positon);
    }

    public static void addTable(Table table) {
        tableList.add(table);
    }

    public static void addTables(List<Table> tables) {
        tableList = tables;
    }

    public static List<Table> getTableList() {
        return tableList;
    }
    public static List<Message> getHaveRespond() {
        return haveRespond;
    }
    public static List<Message> getNoRespond() {
        return noRespond;
    }

    public static void clearTableList() {
        tableList.clear();
    }

    public static void clearAllList() {
        haveRespond.clear();
        noRespond.clear();
        tableList.clear();
    }

//    private static CountDownTimer timer;
//    private static PopupWindow window;
//    private static Vibrator vibrator;
//    private static TextView toOther;
//    private static TextView form;
//    private static TextView complain;
//    private static Subscription mSubscription;
//    private static Message message;
//
//
//    public static void showJPushDate(Activity activity, View parentView) {
//        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
//            @Override
//            public void call(SubscriptionBean.RxBusSendBean bean) {
//                if (bean == null) return;
//                if (bean.type == SubscriptionBean.CHOOSE) {
//                    message = (Message) bean.content;
//                }
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        });
//        RxBus.getInstance().addSubscription(mSubscription);
//        // 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
//        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
//        vibrator.vibrate(pattern, -1);
//        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
//        //加载子布局
//        View view = inflater.inflate(R.layout.popwindow, null);
//        if (window == null) {
//            window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        }
//        toOther = (TextView) view.findViewById(R.id.to_other);
//        form = (TextView) view.findViewById(R.id.former);
//        complain = (TextView) view.findViewById(R.id.complain);
//        final TextView clockTime = (TextView) view.findViewById(R.id.clock_time);
//        //获取焦点
//        window.setFocusable(true);
//        window.setOutsideTouchable(true);
//        //背景颜色
//        window.setBackgroundDrawable(new ColorDrawable(0xffffff));
//        //动画效果（进入页面和退出页面时的效果）
//        //window.setAnimationStyle(R.style.windows);
//        //显示位置：showAtLocation(主布局所点击的按钮id, 位置, x, y);
//        window.showAtLocation(parentView, Gravity.CENTER, 0, 0);
//        //弹窗消失监听
//        if (timer == null) {
//            timer = new CountDownTimer(30000, 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    clockTime.setText((millisUntilFinished / 1000) + "");
////                    showtime = millisUntilFinished;
//                }
//
//                @Override
//                public void onFinish() {
//                    noRespond.add(message);
//                    window.dismiss();
//                }
//            }.start();
//        } else timer.start();
//        setPopListen(activity, message);
//    }
//
//    private static void setPopListen(final Activity activity, final Message message) {
//        toOther.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                haveRespond.add(message);
//                window.dismiss();
//                Intent intent = new Intent(activity, ToContactActivity.class);
//                activity.startActivity(intent);
//            }
//        });
//        complain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                window.dismiss();
//                haveRespond.add(message);
//            }
//        });
//    }

}
