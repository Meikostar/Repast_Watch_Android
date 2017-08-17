package com.canplay.repast_wear.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.canplay.repast_wear.base.manager.AppManager;
import com.canplay.repast_wear.util.ExceptionHandler;
import com.canplay.repast_wear.util.JPushUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * App基类
 * Created by peter on 2016/9/11.
 */

public class BaseApplication extends Application{
    //全局单例
    AppComponent mAppComponent;
    public static  BaseApplication cplayApplication;
    public static BaseApplication getInstance() {
        if (cplayApplication == null) {
            cplayApplication = new BaseApplication();
        }
        return (BaseApplication) cplayApplication;
    }
    @Override
    public void onCreate(){
        // TODO Auto-generated method stub
        super.onCreate();
        //生成全局单例
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        mAppComponent.inject(this);
        ApplicationConfig.setAppInfo(this);
        //全局异常处理
        new ExceptionHandler().init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.setLatestNotificationNumber(this, 1);
        String androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        JPushUtils.shareInstance().setAlias(androidId);
        Log.e("---androidId----",androidId);
        this.cplayApplication = this;
    }

    /**
     * 退出应用
     */
    public void exit(){
        AppManager.getInstance(this).exitAPP(this);
    }

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }
}
