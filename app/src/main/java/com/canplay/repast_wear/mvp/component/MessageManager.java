package com.canplay.repast_wear.mvp.component;


import android.os.CountDownTimer;
import android.widget.PopupWindow;

import com.canplay.repast_wear.mvp.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private static List<PopupWindow> popupWindowList=new ArrayList<>();
    private static List<Message> messageList=new ArrayList<>();
    private static CountDownTimer removetimer;

    public static void send(PopupWindow pop,long time,Message message){
        messageList.add(message);
        popupWindowList.add(pop);
        disMissPop(popupWindowList.size()+1,time);
    }
    public static void disMissPop(final int positon, long time){
        removetimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //自动转移到其他设备
            }
            @Override
            public void onFinish() {
                remove(positon);
            }
        }.start();
    }
    //判断是否存在PopView
    public static boolean listIsNull(){
        if(popupWindowList.size()!=0){
            return false;
        }
        return true;
    }
    public static void remove(int positon){
        popupWindowList.remove(positon);
        messageList.remove(positon);
    }
}
